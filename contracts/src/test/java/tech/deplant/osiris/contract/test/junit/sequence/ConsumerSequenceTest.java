package tech.deplant.osiris.contract.test.junit.sequence;

import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.incubator.concurrent.StructuredTaskScope;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.TIP3TokenRoot;
import tech.deplant.java4ever.framework.contract.TIP3TokenWallet;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.datatype.TypePrefix;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;
import tech.deplant.osiris.Election;
import tech.deplant.osiris.ElectionPhase;
import tech.deplant.osiris.Elector;
import tech.deplant.osiris.StorageOwnerType;
import tech.deplant.osiris.contract.*;
import tech.deplant.osiris.contract.test.template.EverUsdMedianizedTemplate;
import tech.deplant.osiris.contract.test.template.StoredTaskTemplate;
import tech.deplant.osiris.template.BankStorageTemplate;
import tech.deplant.osiris.template.ExampleConsumerTemplate;
import tech.deplant.osiris.template.TaskPreciseOnRequestTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Future;

import static java.lang.System.Logger.Level.INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.osiris.contract.test.junit.TestContext.*;

@DisplayName("full consumer sequence")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.SAME_THREAD)
public class ConsumerSequenceTest {
	public static final System.Logger logger = System.getLogger(tech.deplant.osiris.contract.test.junit.sequence.TaskSequenceTest.class.getName());
	public final static StoredTaskTemplate[] TEMPLATES = {new EverUsdMedianizedTemplate()};

	@BeforeAll
	public static void getContext() throws IOException {
		INIT();
		SDK = SDK_LOCAL_NODE;
		GIVER = GIVER_LOCAL;
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void consumer_sequence() throws IOException, EverSdkException, InterruptedException {


		var bankRoot = CONFIG.contract(BankRoot.class, SDK, "bankRoot", "bankRootKeys");

		// ****************************************************************
		// Deploy Payer
		// ****************************************************************


		var payerKeys = Credentials.RANDOM(SDK);
		var payer = new SafeMultisigWalletTemplate()
				.prepareDeploy(SDK,
				               payerKeys,
				               new BigInteger[]{payerKeys.publicBigInt()},
				               1).deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER, "30"));

		CONFIG.addKeys("payerKeys", payerKeys);
		CONFIG.addContract("payerOwner", payer);

		REFRESH();

		// ****************************************************************
		// Deploy Storage
		// ****************************************************************

		//var root = CONFIG.contract(BankRoot.class, SDK, "bankRoot");

		var valueDepositedOnDeploy = CurrencyUnit.VALUE(EVER, "2");
		var valueDepositedSeparately = CurrencyUnit.VALUE(EVER, "16");

		//Затем воспользоваться deployStorage для размещения BankStorage
		bankRoot.deployStorage(valueDepositedOnDeploy)
		        .sendFromTree(payer,
		                      valueDepositedOnDeploy.add(CurrencyUnit.VALUE(EVER, "0.5"))
		                                            .add(CurrencyUnit.VALUE(EVER, "0.5")),
		                      true,
		                      MessageFlag.EXACT_VALUE_GAS,
		                      true, BankStorage.DEFAULT_ABI(),
		                      TIP3TokenRootTemplate.DEFAULT_ABI(),
		                      TIP3TokenWalletTemplate.DEFAULT_ABI());

		var bankStorageAddress = bankRoot
				.storageOf(new Address(payer.address()), StorageOwnerType.CONSUMER_STORAGE.value())
				.get()
				.value0()
				.makeAddrStd();

		System.out.println("bankStorageAddress 2: " + bankStorageAddress);

		//assertEquals(bankStorageAddress1, bankStorageAddress2);

		//Разместить EVER/VENOM в BankStorage переводом
		var bankStorage = new BankStorage(SDK, bankStorageAddress); //

		CONFIG.addContract("bankStorage", bankStorage);

		REFRESH();

		// ****************************************************************
		// Deposit Value
		// ****************************************************************

		bankStorage.depositValue(valueDepositedSeparately)
		           .sendFromTree(payer,
		                         valueDepositedSeparately.add(CurrencyUnit.VALUE(EVER, "0.1")),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true);

		//Получить баланс своего BankStorage на вьюхе DepositsView
		var getValueBalance = bankStorage._valueBalance().get()._valueBalance();
		System.out.println("My Storage Value Balance: " + getValueBalance);

		REFRESH();
		assertEquals(valueDepositedOnDeploy.add(valueDepositedSeparately), getValueBalance);

		// ****************************************************************
		// Withdraw Value
		// ****************************************************************

		bankStorage.withdrawValue(valueDepositedOnDeploy)
		           .sendFromTree(payer,
		                         CurrencyUnit.VALUE(EVER, "0.1"),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true);

		//Получить баланс своего BankStorage на вьюхе DepositsView
		var getValueBalance2 = bankStorage._valueBalance().get()._valueBalance();
		System.out.println("My Storage Value Balance: " + getValueBalance2);

		REFRESH();
		assertEquals(valueDepositedSeparately, getValueBalance2);

		// ****************************************************************
		// Get Tokens from Faucet
		// ****************************************************************


		var faucet = CONFIG.contract(TestFaucet.class, SDK, "faucet");
		var tip3Root = CONFIG.contract(TIP3TokenRoot.class, SDK, "tip3Root");
		var tip3PayerWalletAddress = tip3Root.walletOf(new Address(payer.address())).get().value0();
		var tip3PayerWallet = new TIP3TokenWallet(SDK, tip3PayerWalletAddress.makeAddrStd());

		boolean doDeploy = !tip3PayerWallet.account().isActive();
		BigInteger value = doDeploy ? CurrencyUnit.VALUE(EVER, "0.3") : CurrencyUnit.VALUE(EVER, "0.1");

		faucet
				.faucet(doDeploy)
				.sendFromTree(payer,
				              value,
				              true,
				              MessageFlag.EXACT_VALUE_GAS,
				              true, TIP3TokenRoot.DEFAULT_ABI(), TIP3TokenWallet.DEFAULT_ABI());

		// ****************************************************************
		// Deposit Token
		// ****************************************************************

		var tokenDeposited1 = CurrencyUnit.VALUE(EVER, "30.5");

		var tip3StorageWalletAddress = tip3Root.walletOf(new Address(bankStorage.address())).get().value0();
		var tip3StorageWallet = new TIP3TokenWallet(SDK, tip3StorageWalletAddress.makeAddrStd());

		var deployWalletValue = tip3StorageWallet.account().isActive() ? BigInteger.ZERO : CurrencyUnit.VALUE(EVER,"0.2");

        System.out.println("Payer Storage: " + bankStorage.address() );
		var tip3StorageWalletFromGetter = new TIP3TokenWallet(SDK,bankStorage._tokenWallet().get()._tokenWallet().makeAddrStd());
		System.out.println("Payer Storage TIP3 Wallet Value from Getter: " + tip3StorageWalletFromGetter.address());
		System.out.println("Payer Storage TIP3 Wallet Value from Getter: Active: " + tip3StorageWalletFromGetter.account().isActive());
		System.out.println("TIP3 Root: " + tip3Root.address());
		System.out.println("TIP3 Calc of Storage for Payer: " + tip3StorageWalletAddress.makeAddrStd());
		System.out.println("TIP3 Calc of Storage for Payer: Active: " + tip3StorageWallet.account().isActive());



		tip3PayerWallet.transfer(tokenDeposited1,
		                         new Address(bankStorage.address()),
		                         BigInteger.ZERO,
		                         new Address(payer.address()),
		                         true,
		                         TvmCell.EMPTY())
		               .sendFromTree(payer,
		                             deployWalletValue.add(CurrencyUnit.VALUE(EVER, "0.1")),
		                             true,
		                             MessageFlag.EXACT_VALUE_GAS,
		                             true, BankStorageTemplate.DEFAULT_ABI());

		//Получить баланс своего BankStorage на вьюхе DepositsView
		var getTokenBalance = bankStorage._tokenBalance().get()._tokenBalance();
		System.out.println("My Storage Token Balance: " + getTokenBalance);

		REFRESH();
		assertEquals(tokenDeposited1, getTokenBalance);

		// ****************************************************************
		// Withdraw Token
		// ****************************************************************

		var tokenWithdraw1 = CurrencyUnit.VALUE(EVER, "4.2");

		bankStorage.withdrawToken(tokenWithdraw1)
		           .sendFromTree(payer,
		                         CurrencyUnit.VALUE(EVER, "0.2"),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true, TIP3TokenWalletTemplate.DEFAULT_ABI());

		//Получить баланс своего BankStorage на вьюхе DepositsView
		var getTokenBalance2 = bankStorage._tokenBalance().get()._tokenBalance();

		System.out.println("My Storage Token Balance: " + getTokenBalance2);

		REFRESH();
		assertEquals(tokenDeposited1.subtract(tokenWithdraw1), getTokenBalance2);

		// ****************************************************************
		// Load Tasks
		// ****************************************************************

		var preciseOnRequestTask = CONFIG.contract(TaskPreciseOnRequest.class, SDK, "preciseImmediateTask");
		var medianOnRequestTask = CONFIG.contract(TaskMedianizedOnRequest.class, SDK, "medianImmediateTask");
		var preciseFeedTask = CONFIG.contract(TaskPreciseValueFeed.class, SDK, "preciseFeedTask");
		var medianFeedTask = CONFIG.contract(TaskMedianizedValueFeed.class, SDK, "medianFeedTask");
//
//		// ****************************************************************
//		// Add Allowance to Precise Immediate Task
//		// ****************************************************************
//
//		var preciseOnRequestTaskConsumerKeys = CONFIG.addKeys("preciseOnRequestTaskConsumerKeys", Credentials.RANDOM(SDK));
//		var preciseOnRequestTaskConsumer = new ExampleConsumerTemplate().prepareDeploy(
//				SDK,
//				preciseOnRequestTaskConsumerKeys).deployWithGiver(GIVER, EVER_FIVE);
//
//		CONFIG.addContract("preciseOnRequestTaskConsumer", preciseOnRequestTaskConsumer);
//
//		assertEquals(BigInteger.ZERO, preciseOnRequestTaskConsumer.getMedianizedResult().get().value0());
//		assertEquals(TvmCell.EMPTY().cellBoc(), preciseOnRequestTaskConsumer.getPreciseResult().getAsMap().get("value0").toString());
//
//		REFRESH();
//
//		var valueAllowance1 = CurrencyUnit.VALUE(EVER, "10");
//		var tokenAllowance1 = CurrencyUnit.VALUE(EVER, "10");
//
//		bankStorage.addAllowance(new Address(preciseOnRequestTaskConsumer.address()),
//		                         new Address(preciseOnRequestTask.address()),
//		                         valueAllowance1,
//		                         tokenAllowance1)
//		           .sendFromTree(payer,
//		                         CurrencyUnit.VALUE(EVER, "0.1"),
//		                         true,
//		                         MessageFlag.EXACT_VALUE_GAS,
//		                         true);
//
//		assertEquals(valueAllowance1, bankStorage._allowanceMap().get()._allowanceMap().get(new Address(preciseOnRequestTaskConsumer.address())).get("maxValue"));
//		assertEquals(valueAllowance1, bankStorage._allowanceMap().get()._allowanceMap().get(new Address(preciseOnRequestTaskConsumer.address())).get("maxToken"));
//
//		REFRESH();

//		// ****************************************************************
//		// Add Allowance to Median Immediate Task
//		// ****************************************************************
//
//		var medianOnRequestTaskConsumerKeys = CONFIG.addKeys("medianOnRequestTaskConsumerKeys", Credentials.RANDOM(SDK));
//		var medianOnRequestTaskConsumer = new ExampleConsumerTemplate().prepareDeploy(
//				SDK,
//				medianOnRequestTaskConsumerKeys).deployWithGiver(GIVER, EVER_FIVE);
//
//		CONFIG.addContract("medianOnRequestTaskConsumer", medianOnRequestTaskConsumer);
//
//		assertEquals(BigInteger.ZERO, medianOnRequestTaskConsumer.getMedianizedResult().get().value0());
//		assertEquals(TvmCell.EMPTY().cellBoc(), medianOnRequestTaskConsumer.getPreciseResult().getAsMap().get("value0").toString());
//
//		REFRESH();
//
//		var valueAllowance2 = CurrencyUnit.VALUE(EVER, "10");
//		var tokenAllowance2 = CurrencyUnit.VALUE(EVER, "10");
//
//		bankStorage.addAllowance(new Address(medianOnRequestTaskConsumer.address()),
//		                         new Address(medianOnRequestTask.address()),
//		                         valueAllowance2,
//		                         tokenAllowance2)
//		           .sendFromTree(payer,
//		                         CurrencyUnit.VALUE(EVER, "0.1"),
//		                         true,
//		                         MessageFlag.EXACT_VALUE_GAS,
//		                         true);
//
//		assertEquals(valueAllowance2, bankStorage._allowanceMap().get()._allowanceMap().get(new Address(medianOnRequestTaskConsumer.address())).get("maxValue"));
//		assertEquals(valueAllowance2, bankStorage._allowanceMap().get()._allowanceMap().get(new Address(medianOnRequestTaskConsumer.address())).get("maxToken"));
//
//		REFRESH();



//
//		// ****************************************************************
//		// Add Allowance to Precise Feed
//		// ****************************************************************
//
//		var preciseFeedTaskConsumerKeys = CONFIG.addKeys("preciseFeedTaskConsumerKeys", Credentials.RANDOM(SDK));
//		var preciseFeedTaskConsumer = new ExampleConsumerTemplate().prepareDeploy(
//				SDK,
//				preciseFeedTaskConsumerKeys).deployWithGiver(GIVER, EVER_FIVE);
//
//		CONFIG.addContract("preciseFeedTaskConsumer", preciseFeedTaskConsumer);
//
//		assertEquals(BigInteger.ZERO, preciseFeedTaskConsumer.getMedianizedResult().get().value0());
//		assertEquals(TvmCell.EMPTY().cellBoc(), preciseFeedTaskConsumer.getPreciseResult().getAsMap().get("value0").toString());
//
//		REFRESH();
//
//		var valueAllowance3 = CurrencyUnit.VALUE(EVER, "10");
//		var tokenAllowance3 = CurrencyUnit.VALUE(EVER, "10");
//
//		bankStorage.addAllowance(new Address(preciseFeedTaskConsumer.address()),
//		                         new Address(preciseFeedTask.address()),
//		                         valueAllowance3,
//		                         tokenAllowance3)
//		           .sendFromTree(payer,
//		                         CurrencyUnit.VALUE(EVER, "0.1"),
//		                         true,
//		                         MessageFlag.EXACT_VALUE_GAS,
//		                         true);
//
//		assertEquals(valueAllowance3, bankStorage._allowanceMap().get()._allowanceMap().get(new Address(preciseFeedTaskConsumer.address())).get("maxValue"));
//		assertEquals(valueAllowance3, bankStorage._allowanceMap().get()._allowanceMap().get(new Address(preciseFeedTaskConsumer.address())).get("maxToken"));
//
//		REFRESH();
//
		// ****************************************************************
		// Add Allowance to Median Feed
		// ****************************************************************

		var medianFeedTaskConsumerKeys = CONFIG.addKeys("medianFeedTaskConsumerKeys", Credentials.RANDOM(SDK));
		var medianFeedTaskConsumer = new ExampleConsumerTemplate().prepareDeploy(
				SDK,
				medianFeedTaskConsumerKeys).deployWithGiver(GIVER, EVER_FIVE);

		CONFIG.addContract("medianFeedTaskConsumer", medianFeedTaskConsumer);

		assertEquals(BigInteger.ZERO, medianFeedTaskConsumer.getMedianizedResult().get().value0());
		assertEquals(TvmCell.EMPTY().cellBoc(), medianFeedTaskConsumer.getPreciseResult().getAsMap().get("value0").toString());

		REFRESH();

		var valueAllowance4 = CurrencyUnit.VALUE(EVER, "10");
		var tokenAllowance4 = CurrencyUnit.VALUE(EVER, "10");

		bankStorage.addAllowance(new Address(medianFeedTaskConsumer.address()),
		                         new Address(medianFeedTask.address()),
		                         valueAllowance4,
		                         tokenAllowance4)
		           .sendFromTree(payer,
		                         CurrencyUnit.VALUE(EVER, "0.1"),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true);

		assertEquals(valueAllowance4, bankStorage._allowanceMap().get()._allowanceMap().get(new Address(medianFeedTaskConsumer.address())).get("maxValue"));
		assertEquals(valueAllowance4, bankStorage._allowanceMap().get()._allowanceMap().get(new Address(medianFeedTaskConsumer.address())).get("maxToken"));

		REFRESH();

		BankStorage taskStorage;






//		// ****************************************************************
//		// Request to Precise Immediate Task
//		// ****************************************************************
//
//		taskStorage = new BankStorage(SDK, preciseOnRequestTask._storage().get()._storage().makeAddrStd());
//
//		System.out.println("TaskStorage VENOMs: " + taskStorage._valueBalance().get()._valueBalance());
//		System.out.println("TaskStorage Tokens: " + taskStorage._tokenBalance().get()._tokenBalance());
//		System.out.println("PayerStorage VENOMs: " + bankStorage._valueBalance().get()._valueBalance());
//		System.out.println("PayerStorage Tokens: " + bankStorage._tokenBalance().get()._tokenBalance());
//
//		BigInteger executionFee1 = new BigInteger(preciseOnRequestTask._taskSettings()
//		                                             .get()
//		                                             ._taskSettings()
//		                                             .get("executionFee")
//		                                             .toString());
//
//		System.out.println("Task Execution Fee: " + executionFee1);
//
//		preciseOnRequestTaskConsumer.calculatePreciseOnRequest(new Address(preciseOnRequestTask.address()),
//		                                   new Address(payer.address()),
//		                                   TvmCell.EMPTY())
//		        .callTree(true, TIP3TokenWalletTemplate.DEFAULT_ABI(), BankStorageTemplate.DEFAULT_ABI(),
//		                  TaskPreciseOnRequestTemplate.DEFAULT_ABI());
//
//		System.out.println("TaskStorage VENOMs: " + taskStorage._valueBalance().get()._valueBalance());
//		System.out.println("TaskStorage Tokens: " + taskStorage._tokenBalance().get()._tokenBalance());
//		System.out.println("PayerStorage VENOMs: " + bankStorage._valueBalance().get()._valueBalance());
//		System.out.println("PayerStorage Tokens: " + bankStorage._tokenBalance().get()._tokenBalance());
//
//		assertEquals(new BigInteger(String.valueOf(ElectionPhase.COMMIT.value())),
//		             preciseOnRequestTask._currElectionState().get()._currElectionState().get("status"));
//
//		REFRESH();
//
//		// ****************************************************************
//		// Good Try
//		// ****************************************************************
//
//		Map<String, OracleValidator> oracleValidators1 = new HashMap<>();
//
//		oracleValidators1.put("validator0", CONFIG.contract(OracleValidator.class, SDK, "validator0", "validator0Keys"));
//		oracleValidators1.put("validator1", CONFIG.contract(OracleValidator.class, SDK, "validator1", "validator1Keys"));
//		oracleValidators1.put("validator2", CONFIG.contract(OracleValidator.class, SDK, "validator2", "validator2Keys"));
//		oracleValidators1.put("validator3", CONFIG.contract(OracleValidator.class, SDK, "validator3", "validator3Keys"));
//
//		Map<String, TvmCell> responsesGoodTry1 = new HashMap<>();
//
//		responsesGoodTry1.put("validator0", TvmCell.builder().store(TypePrefix.UINT, 128, "1550000").toCell(SDK));
//		responsesGoodTry1.put("validator1", TvmCell.builder().store(TypePrefix.UINT, 128, "1550000").toCell(SDK));
//		responsesGoodTry1.put("validator2", TvmCell.builder().store(TypePrefix.UINT, 128, "1550000").toCell(SDK));
//		responsesGoodTry1.put("validator3", TvmCell.builder().store(TypePrefix.UINT, 128, "1550000").toCell(SDK));
//
//
//		election_sequence(new Elector(preciseOnRequestTask), oracleValidators1,
//		                  responsesGoodTry1);
//
//		var result = preciseOnRequestTaskConsumer.getPreciseResult().getAsMap().get("value0").toString();
//
//		String[] outTypeNames = new String[]{"uint128"};
//		var decoded = new TvmCell(result).decodeAndGet(SDK, outTypeNames, 0);
//
//		logger.log(INFO, decoded);
//		assertEquals("1550000", decoded.toString());

		// ****************************************************************
		// Bad Try - Bad answers
		// ****************************************************************

		// ****************************************************************
		// Bad Try - Timeout
		// ****************************************************************

		// ****************************************************************
		// Good Try
		// ****************************************************************

//		// ****************************************************************
//		// Request to Medianized Immediate Task
//		// ****************************************************************
//
//		var taskStorageMedianOnRequest = new BankStorage(SDK, medianOnRequestTask._storage().get()._storage().makeAddrStd());
//
//		System.out.println("TaskStorage VENOMs: " + taskStorageMedianOnRequest._valueBalance().get()._valueBalance());
//		System.out.println("TaskStorage Tokens: " + taskStorageMedianOnRequest._tokenBalance().get()._tokenBalance());
//		System.out.println("PayerStorage VENOMs: " + bankStorage._valueBalance().get()._valueBalance());
//		System.out.println("PayerStorage Tokens: " + bankStorage._tokenBalance().get()._tokenBalance());
//
//		var executionFee2 = new BigInteger(medianOnRequestTask._taskSettings()
//		                                                      .get()
//		                                                      ._taskSettings()
//		                                                      .get("executionFee")
//		                                                      .toString());
//
//		System.out.println("Task Execution Fee: " + executionFee2);
//
//		medianOnRequestTaskConsumer.calculateMedianizedOnRequest(new Address(medianOnRequestTask.address()),
//		                                                       new Address(payer.address()),
//		                                                       TvmCell.EMPTY())
//		                            .callTree(true, TIP3TokenWalletTemplate.DEFAULT_ABI(), BankStorageTemplate.DEFAULT_ABI(),
//		                                      TaskMedianizedOnRequest.DEFAULT_ABI());
//
//		System.out.println("TaskStorage VENOMs: " + taskStorageMedianOnRequest._valueBalance().get()._valueBalance());
//		System.out.println("TaskStorage Tokens: " + taskStorageMedianOnRequest._tokenBalance().get()._tokenBalance());
//		System.out.println("PayerStorage VENOMs: " + bankStorage._valueBalance().get()._valueBalance());
//		System.out.println("PayerStorage Tokens: " + bankStorage._tokenBalance().get()._tokenBalance());
//
//		assertEquals(new BigInteger(String.valueOf(ElectionPhase.COMMIT.value())),
//		             medianOnRequestTask._currElectionState().get()._currElectionState().get("status"));
//
//		REFRESH();
//
//		// ****************************************************************
//		// Good Try
//		// ****************************************************************
//
//		Map<String, OracleValidator> oracleValidators2 = new HashMap<>();
//
//		oracleValidators2.put("validator0", CONFIG.contract(OracleValidator.class, SDK, "validator0", "validator0Keys"));
//		oracleValidators2.put("validator1", CONFIG.contract(OracleValidator.class, SDK, "validator1", "validator1Keys"));
//		oracleValidators2.put("validator2", CONFIG.contract(OracleValidator.class, SDK, "validator2", "validator2Keys"));
//		oracleValidators2.put("validator3", CONFIG.contract(OracleValidator.class, SDK, "validator3", "validator3Keys"));
//
//		Map<String, TvmCell> responsesGoodTry2 = new HashMap<>();
//
//		responsesGoodTry2.put("validator0", TvmCell.builder().store(TypePrefix.UINT, 128, "1850000").toCell(SDK));
//		responsesGoodTry2.put("validator1", TvmCell.builder().store(TypePrefix.UINT, 128, "1550000").toCell(SDK));
//		responsesGoodTry2.put("validator2", TvmCell.builder().store(TypePrefix.UINT, 128, "1650000").toCell(SDK));
//		responsesGoodTry2.put("validator3", TvmCell.builder().store(TypePrefix.UINT, 128, "1950000").toCell(SDK));
//
//
//		election_sequence(new Elector(medianOnRequestTask), oracleValidators2,
//		                  responsesGoodTry2);
//
//		var resultOfMedianOnRequestTask = medianOnRequestTaskConsumer.getMedianizedResult().getAsMap().get("value0").toString();
//
//		//String[] outTypeNames = new String[]{"uint128"};
//		//var decoded = new TvmCell(result).decodeAndGet(SDK, outTypeNames, 0);
//
//		logger.log(INFO, resultOfMedianOnRequestTask);
//		assertEquals("1850000", resultOfMedianOnRequestTask);

		// ****************************************************************
		// Bad Try - Bad answers
		// ****************************************************************

		// ****************************************************************
		// Bad Try - Timeout
		// ****************************************************************

		// ****************************************************************
		// Good Try
		// ****************************************************************

		// ****************************************************************
		// Refresh Medianized Feed
		// ****************************************************************

		Map<String, OracleValidator> oracleValidators2 = new HashMap<>();

		oracleValidators2.put("validator0", CONFIG.contract(OracleValidator.class, SDK, "validator0", "validator0Keys"));
		oracleValidators2.put("validator1", CONFIG.contract(OracleValidator.class, SDK, "validator1", "validator1Keys"));
		oracleValidators2.put("validator2", CONFIG.contract(OracleValidator.class, SDK, "validator2", "validator2Keys"));
		oracleValidators2.put("validator3", CONFIG.contract(OracleValidator.class, SDK, "validator3", "validator3Keys"));

		oracleValidators2.get("validator0").refreshFeed(new Address(medianFeedTask.address())).callTree(true, TaskMedianizedValueFeed.DEFAULT_ABI());

		REFRESH();

		// ****************************************************************
		// Good Try
		// ****************************************************************


		Map<String, TvmCell> responsesGoodTry2 = new HashMap<>();

		responsesGoodTry2.put("validator0", TvmCell.builder().store(TypePrefix.UINT, 128, "1850000").toCell(SDK));
		responsesGoodTry2.put("validator1", TvmCell.builder().store(TypePrefix.UINT, 128, "1550000").toCell(SDK));
		responsesGoodTry2.put("validator2", TvmCell.builder().store(TypePrefix.UINT, 128, "1650000").toCell(SDK));
		responsesGoodTry2.put("validator3", TvmCell.builder().store(TypePrefix.UINT, 128, "1950000").toCell(SDK));


		election_sequence(new Elector(medianFeedTask), oracleValidators2,
		                  responsesGoodTry2);

		// ****************************************************************
		// Request to Medianized Feed
		// ****************************************************************

		var executionFee2 = new BigInteger(medianFeedTask._taskSettings()
		                                                      .get()
		                                                      ._taskSettings()
		                                                      .get("executionFee")
		                                                      .toString());

		System.out.println("Task Execution Fee: " + executionFee2);

		medianFeedTaskConsumer.calculateMedianizedFeed(new Address(medianFeedTask.address()),
		                                                         new Address(payer.address()))
		                           .callTree(true, TIP3TokenWalletTemplate.DEFAULT_ABI(), BankStorageTemplate.DEFAULT_ABI(),
		                                     TaskMedianizedValueFeed.DEFAULT_ABI());

		var resultOfMedianFeed = medianFeedTaskConsumer.getMedianizedResult().getAsMap().get("value0").toString();

		//String[] outTypeNames = new String[]{"uint128"};
		//var decoded = new TvmCell(result).decodeAndGet(SDK, outTypeNames, 0);

		logger.log(INFO, resultOfMedianFeed);
		assertEquals("1850000", resultOfMedianFeed);

		REFRESH();



	}

	public void election_sequence(Elector elector, Map<String, OracleValidator> validators,
	                              Map<String, TvmCell> responses) throws JsonProcessingException {



		List<Future<Boolean>> futures = new ArrayList<>();

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			futures.add(scope.fork(() -> new Election(validators.get("validator0"),
			                                          elector,
			                                          responses.get("validator0")).vote()));
			futures.add(scope.fork(() -> new Election(validators.get("validator1"),
			                                          elector,
			                                          responses.get("validator1")).vote()));
			futures.add(scope.fork(() -> new Election(validators.get("validator2"),
			                                          elector,
			                                          responses.get("validator2")).vote()));
			futures.add(scope.fork(() -> new Election(validators.get("validator3"),
			                                          elector,
			                                          responses.get("validator3")).vote()));
			scope.join();
			futures.stream().filter(fut -> Future.State.FAILED.equals(fut.state())).forEach(fut ->
			                                                                                {
				                                                                                StringBuilder bld = new StringBuilder();
				                                                                                Arrays.stream(fut.exceptionNow()
				                                                                                                 .getStackTrace())
				                                                                                      .toList()
				                                                                                      .forEach(trace -> bld.append(
						                                                                                      trace.toString()));
				                                                                                logger.log(System.Logger.Level.ERROR,
				                                                                                           fut.exceptionNow()
				                                                                                              .toString() +
				                                                                                           bld.toString());
			                                                                                });
			futures.stream().filter(fut -> Future.State.SUCCESS.equals(fut.state())).forEach(Future::resultNow);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}


	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void get_allowance() throws IOException, EverSdkException, InterruptedException {
		var payerStorage = CONFIG.contract(BankStorage.class, SDK, "payerStorage");
		var allowanceMap = payerStorage._allowanceMap().get()._allowanceMap();
		for (var entry : allowanceMap.entrySet()) {
			var consumerAddress = entry.getKey().makeAddrStd();
			//         address task;
			//        uint128 maxValue;
			//        uint256 maxToken;
			var taskAddress = entry.getValue().get("task").toString();
			var maxValue = new BigInteger(entry.getValue().get("maxValue").toString());
			var maxToken = new BigInteger(entry.getValue().get("maxToken").toString());
			logger.log(System.Logger.Level.DEBUG, taskAddress + " " + maxValue + " " + maxToken);
		}
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void get_transactions() throws IOException, EverSdkException, InterruptedException {
		var payerStorage = CONFIG.contract(BankStorage.class, SDK, "payerStorage");
		var allowanceMap = payerStorage._allowanceMap().get()._allowanceMap();
		for (var entry : allowanceMap.entrySet()) {
			var consumerAddress = entry.getKey().makeAddrStd();
			//         address task;
			//        uint128 maxValue;
			//        uint256 maxToken;
			var taskAddress = entry.getValue().get("task").toString();
			var maxValue = new BigInteger(entry.getValue().get("maxValue").toString());
			var maxToken = new BigInteger(entry.getValue().get("maxToken").toString());
			logger.log(System.Logger.Level.DEBUG, taskAddress + " " + maxValue + " " + maxToken);
		}
	}

}
