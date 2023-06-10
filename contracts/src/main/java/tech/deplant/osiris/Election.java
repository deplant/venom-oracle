package tech.deplant.osiris;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.datatype.TypePrefix;
import tech.deplant.java4ever.utils.Objs;
import tech.deplant.osiris.contract.OracleValidator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static tech.deplant.java4ever.framework.LogUtils.info;

public class Election {

	public static final System.Logger logger = System.getLogger(Election.class.getName());

	private OracleValidator validator;
	private Elector elector;
	private Object response;
	private BigInteger commitSalt;
	private BigInteger commitHash;

	private Long electionTimeout;

	private Long requestDelay;

	public Election(OracleValidator validator,
	                Elector elector, Object response, Long requestDelay) throws EverSdkException {
		this.validator = validator;
		this.elector = elector;
		this.requestDelay = requestDelay;
		this.electionTimeout = Long.valueOf(elector.factorySettingsProperty("electionTimeout").toString());
		if (elector.type().equals(TaskType.MEDIAN_FEED) || elector.type().equals(TaskType.MEDIAN_IMMEDIATE)) {
			if (response instanceof BigDecimal bdResp) {
				this.response = TvmCell.builder()
				                       .store(TypePrefix.UINT, 128, bdResp.toBigInteger())
				                       .toCell(elector.sdk())
				                       .cellBoc();
			} else {
				logger.log(ERROR, "Bad response for Medianized Consensus!");
				throw new RuntimeException();
			}
		} else {
			if (response instanceof String str) {
				this.response = TvmCell.builder()
				                       .store(TypePrefix.STRING, 128, str)
				                       .toCell(elector.sdk())
				                       .cellBoc();
			} else if (response instanceof BigDecimal bdResp) {
				this.response = TvmCell.builder()
				                       .store(TypePrefix.UINT, 256, bdResp.toBigInteger())
				                       .toCell(elector.sdk())
				                       .cellBoc();
			} else {
				logger.log(ERROR, "Bad response for Precise Consensus!");
				throw new RuntimeException();
			}
		}

	}

	public Election(OracleValidator validator,
	                Elector elector, Object response) throws EverSdkException {
		this(validator, elector, response, Long.valueOf("8000"));
	}


	private BigInteger salt() {
		return BigInteger.valueOf(new Random().nextInt());
	}

	public Election prepareCommit(BigInteger salt) throws EverSdkException {
		this.commitSalt = salt;
		this.commitHash = validator.hash(TvmCell.fromJava(this.response), this.commitSalt);
		logger.log(INFO, "Value hash: " + this.commitHash);
		return this;
	}

	public Election prepareCommit() throws EverSdkException {
		return prepareCommit(salt());
	}

	public Election awaitReveal() throws EverSdkException, InterruptedException {

		long stepStart = Instant.now().getEpochSecond();

		while (!Objs.equals(elector.currentPhase(), ElectionPhase.REVEAL) && Instant.now().getEpochSecond() < stepStart + 240L) {
			logger.log(System.Logger.Level.TRACE, "Still not reveal....");
			// we sleep for standard delay as phase is still not ready
			Thread.sleep(this.requestDelay);
		}

		logger.log(INFO, "Reveal phase started!");
		validator.reveal(Address.fromJava(this.elector.address()),
		                 TvmCell.fromJava(this.response),
		                 this.commitSalt).callTree(true, this.elector.abi());
		Thread.sleep(this.electionTimeout);

		while (Objs.equals(elector.currentPhase(), ElectionPhase.REVEAL) && Instant.now().getEpochSecond() < stepStart + 240L) {
			// ***************** IV.D SWITCH TO FINAL
			logger.log(INFO, "Still reveal....");
			logger.log(INFO, elector.currentStateProperty("revealedCount"));
			try {
				validator.requestFinalPhase(Address.fromJava(elector.address())).callTree(true, this.elector.abi());
			} catch (EverSdkException e) {
				if (e.errorResponse().code() == 308 || e.errorResponse().code() == 304) {

				} else {
					throw e;
				}
			}
			Thread.sleep(this.requestDelay);
		}
		return this;
	}

	public Election awaitFinalize() throws EverSdkException, InterruptedException {

		long stepStart = Instant.now().getEpochSecond();

		while (!Objs.equals(elector.currentPhase(), ElectionPhase.READY) && Instant.now().getEpochSecond() < stepStart + 240L) {
			logger.log(System.Logger.Level.TRACE, "Still not finalized....");
			Thread.sleep(this.requestDelay);
		}
		logger.log(INFO, "Success phase!");
		return this;
	}

	public Election awaitCommit() throws EverSdkException, InterruptedException {


		long stepStart = Instant.now().getEpochSecond();

		// launches every 5 sec, until we get to
		while (!Objs.equals(elector.currentPhase(), ElectionPhase.COMMIT) && Instant.now().getEpochSecond() < stepStart + 240L) {
			logger.log(System.Logger.Level.TRACE, "Still not commit...");
			// we sleep for standard delay as phase is still not ready
			Thread.sleep(this.requestDelay);
		}

		logger.log(INFO, "Commit phase started!");
		validator.commit(Address.fromJava(elector.address()), commitHash).callTree(true, this.elector.abi());
		Thread.sleep(electionTimeout);

		// now we can sleep for timeout
		while (Objs.equals(elector.currentPhase(), ElectionPhase.COMMIT)  && Instant.now().getEpochSecond() < stepStart + 240L) {
			logger.log(INFO, elector.currentStateProperty("commitedCount"));
			//***************** IV.B SWITCH TO REVEAL
			logger.log(INFO, "Still commit....");
			try {
				validator.requestRevealPhase(Address.fromJava(elector.address())).callTree(true, this.elector.abi());
			} catch (EverSdkException e) {
				if (e.errorResponse().code() == 308 || e.errorResponse().code() == 304) {
				} else {
					throw e;
				}
			}
			Thread.sleep(this.requestDelay);
		}
		return this;
	}

	public boolean vote() throws EverSdkException {

		boolean result = false;

		//if (response instanceof BigDecimal bd) {

		//var elector = new (validator.sdk(), elector);
		//var election = new Election(validator, elector, bd);

		info(logger, "Election start! Validator: " + validator.address());

		logger.log(INFO, "Commit prepare! Validator: " + validator.address());

		prepareCommit();

		// ***************** IV.A COMMIT phase
		logger.log(INFO, "Await commit! Validator: " + validator.address());

		try {
			awaitCommit();

			logger.log(INFO, "Await reveal! Validator: " + validator.address());

			// ***************** IV.C REVEAL phase
			awaitReveal();

			logger.log(INFO, "Await finalize! Validator: " + validator.address());

			// ***************** IV.E SUCCESS/FAILURE check
			// here, we can check if it was success or not
			// for this validator
			awaitFinalize();

			// now we can return to main loop
			result = Objs.equals(elector.currentPhase(), ElectionPhase.SUCCESS);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		result = true;
//		} else {
//			logger.log(System.Logger.Level.INFO, "Consensus failed. Incorrect response object type!");
//			result = false;
//		}
		return result;
	}

}
