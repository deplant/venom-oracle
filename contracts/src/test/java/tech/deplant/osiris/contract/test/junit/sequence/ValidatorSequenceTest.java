package tech.deplant.osiris.contract.test.junit.sequence;

import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.incubator.concurrent.StructuredTaskScope;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.osiris.contract.OracleValidator;
import tech.deplant.osiris.template.OracleValidatorTemplate;
import tech.deplant.osiris.template.TaskSubscriptionTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static tech.deplant.osiris.contract.test.junit.TestContext.*;

@DisplayName("full validator sequence")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.SAME_THREAD)
public class ValidatorSequenceTest {
	public static final System.Logger logger = System.getLogger(tech.deplant.osiris.contract.test.junit.sequence.ValidatorSequenceTest.class.getName());
	public static final int VALIDATORS_COUNT = 4;

	@BeforeAll
	public static void getContext() throws IOException {
		INIT();
		SDK = SDK_LOCAL_NODE;
		GIVER = GIVER_LOCAL;
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void validator_sequence() throws IOException, InterruptedException {

		// ****************************************************************
		// Deploy Owners
		// ****************************************************************

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			Future<SafeMultisigWallet>[] contracts = new Future[VALIDATORS_COUNT];
			for (int i = 0; i < VALIDATORS_COUNT; i++) {
				final int fi = i;
				contracts[i] = scope.fork(() -> {
					var msigKeys = CONFIG.addKeys("validator" + fi + "OwnerKeys", Credentials.RANDOM(SDK));
					var msig = new SafeMultisigWalletTemplate()
							.prepareDeploy(SDK,
							               msigKeys,
							               new BigInteger[]{msigKeys.publicBigInt()},
							               1).deployWithGiver(GIVER, EVER_TEN);
					return msig;
				});
			}
			scope.join();
			for (int i = 0; i < VALIDATORS_COUNT; i++) {
				CONFIG.addContract("validator" + i + "Owner", contracts[i].resultNow());
			}
		}

		REFRESH();

		// ****************************************************************
		// Bank Root Deploy
		// ****************************************************************

		OracleValidator[] validators = new OracleValidator[4];

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			Future<OracleValidator>[] contracts = new Future[VALIDATORS_COUNT];
			for (int i = 0; i < VALIDATORS_COUNT; i++) {
				final int fi = i;
				contracts[i] = scope.fork(() -> {
					var validatorKeys = CONFIG.addKeys("validator" + fi + "Keys", Credentials.RANDOM(SDK));
					return new OracleValidatorTemplate()
							.prepareDeploy(
									SDK,
									validatorKeys,
									TvmCell.EMPTY(),
									"Deplant Test Oracle Validator #" + fi,
									"aaaa")
							.deployWithGiver(GIVER, EVER_TEN);
				});
			}
			scope.join();

			for (int i = 0; i < VALIDATORS_COUNT; i++) {
				validators[i] = contracts[i].resultNow();
				CONFIG.addContract("validator" + i, validators[i]);
			}
		}
		REFRESH();

		// ****************************************************************
		// Set Subscription Code
		// ****************************************************************

		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (var validator : validators) {
				executor.submit(() -> {
					try {
						validator.setSubscriptionCode(TvmCell.fromJava(new TaskSubscriptionTemplate().tvc()
						                                                                             .code(SDK)))
						         .call();
						//assertNotEquals(remoteBoc.cellBoc(), TvmCell.EMPTY().cellBoc());
					} catch (EverSdkException | JsonProcessingException e) {
						throw new RuntimeException(e);
					}
				});
			}
		}

		REFRESH();

		// ****************************************************************
		// Subscribe
		// ****************************************************************

		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (var validator : validators) {
				executor.submit(() -> validator.subscribe(Address.fromJava(CONFIG.address(TEMPLATES[0].name())),
				                                          0L));
			}
		}

		REFRESH();
		//assertEquals(valueDepositedSeparately, getValueBalance2);
	}


}