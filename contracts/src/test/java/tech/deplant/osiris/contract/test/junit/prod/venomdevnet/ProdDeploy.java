package tech.deplant.osiris.contract.test.junit.prod.venomdevnet;

import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.incubator.concurrent.StructuredTaskScope;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.contract.TIP3TokenRoot;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;
import tech.deplant.osiris.StorageOwnerType;
import tech.deplant.osiris.TaskType;
import tech.deplant.osiris.contract.*;
import tech.deplant.osiris.template.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.System.Logger.Level.DEBUG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.osiris.contract.test.junit.TestContext.*;

//@Disabled
@DisplayName("prod deploy sequences for everything")
@TestMethodOrder(MethodOrderer.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.SAME_THREAD)
public class ProdDeploy {
	public static final System.Logger logger = System.getLogger(ProdDeploy.class.getName());
	public static int VALIDATORS_COUNT;
	@BeforeAll
	public static void getContext() throws IOException {
		INIT();
		SDK = SDK_VENOM_DEVNET;
		var devnetGiverCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");

		var devnetGiverAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";
		GIVER = new SafeMultisigWallet(SDK, devnetGiverAddress, devnetGiverCreds);
		CONFIG = SDK_VENOM_DEVNET.onchainConfig();
		VALIDATORS_COUNT = 4;
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void bank_deploy_and_checks_sequence() throws IOException, EverSdkException {

		// ****************************************************************
		// Owner
		// ****************************************************************

		var bankOwnerKeys = Credentials.RANDOM(SDK);
		var bankOwner = new SafeMultisigWalletTemplate()
				.prepareDeploy(SDK,
				               bankOwnerKeys,
				               new BigInteger[]{bankOwnerKeys.publicBigInt()},
				               1).deployWithGiver(GIVER, EVER_TEN);

		CONFIG.addKeys("bankOwnerKeys", bankOwnerKeys);
		CONFIG.addContract("bankOwner", bankOwner);

		REFRESH();

		// ****************************************************************
		// Bank Root Deploy
		// ****************************************************************

		var keys = CONFIG.addKeys("bankRootKeys", Credentials.RANDOM(SDK));
		CONFIG.addKeys("bankRootKeys", keys);

		var tokenAddress = CONFIG.contract(TIP3TokenRoot.class, SDK, "tip3Root").address();
		var factoryAddress = CONFIG.contract(Factory.class, SDK, "taskFactory").address();

		var bankRoot = new BankRootTemplate()
				.prepareDeploy(SDK,
				               keys,
				               new Address(bankOwner.address()),
				               new Address("0:a3682fd1814f293cbf7ba69f5678533bc8576d226a369b6fa07a66073d1d54b1"),
				               new Address(factoryAddress))
				.deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER, "1"));
		CONFIG.addContract("bankRoot", bankRoot);

		REFRESH();

		// ****************************************************************
		// Set Storage Code
		// ****************************************************************

		//var root = CONFIG.contract(BankRoot.class, SDK, "bankRoot");
		var storageCodeCell = new BankStorageTemplate().tvc().codeCell(SDK);
		//и закинуть в setStorageCode контракта BankRoot
		bankRoot.setStorageCode(storageCodeCell)
		        .sendFromTree(bankOwner,
		                      CurrencyUnit.VALUE(EVER, "0.1"),
		                      true,
		                      MessageFlag.EXACT_VALUE_GAS,
		                      true);
		var remoteBoc = new TvmCell(bankRoot._storageCode().getAsMap().get("_storageCode").toString());

		REFRESH();
		assertNotEquals(remoteBoc.cellBoc(), TvmCell.EMPTY().cellBoc());

		// ****************************************************************
		// Set Bank on Factory and Factory on Bank
		// ****************************************************************
		//var factory = CONFIG.contract(Factory.class, SDK, "taskFactory", "taskFactoryKeys");

		//factory.setBank(new Address(bankRoot.address())).callTree(true);
		//bankRoot.setFactory(new Address(factory.address())).sendFrom(bankOwner, CurrencyUnit.VALUE(EVER, "0.1"));

		//REFRESH();
		//assertNotEquals(remoteBoc.cellBoc(), TvmCell.EMPTY().cellBoc());

		// ****************************************************************
		// Deploy Storage
		// ****************************************************************

		//var root = CONFIG.contract(BankRoot.class, SDK, "bankRoot");

		var valueDepositedOnDeploy = CurrencyUnit.VALUE(EVER, "0.1");
		var valueDepositedSeparately = CurrencyUnit.VALUE(EVER, "0.2");

		//Затем воспользоваться deployStorage для размещения BankStorage
		bankRoot.deployStorage(valueDepositedOnDeploy)
		        .sendFromTree(bankOwner,
		                      valueDepositedOnDeploy.add(CurrencyUnit.VALUE(EVER, "0.1"))
		                                            .add(CurrencyUnit.VALUE(EVER, "0.2")),
		                      true,
		                      MessageFlag.EXACT_VALUE_GAS,
		                      true, BankStorage.DEFAULT_ABI());

		var bankStorageAddress = bankRoot
				.storageOf(new Address(bankOwner.address()), StorageOwnerType.CONSUMER_STORAGE.value())
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
		           .sendFromTree(bankOwner,
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
		           .sendFromTree(bankOwner,
		                         CurrencyUnit.VALUE(EVER, "0.1"),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true);

		//Получить баланс своего BankStorage на вьюхе DepositsView
		var getValueBalance2 = bankStorage._valueBalance().get()._valueBalance();
		System.out.println("My Storage Value Balance: " + getValueBalance2);

		REFRESH();
		assertEquals(valueDepositedSeparately, getValueBalance2);
	}


	@Order(2)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void factory_and_tasks_deploy_and_checks_sequence() throws IOException, EverSdkException, InterruptedException {

		// ****************************************************************
		// Factory Deploy
		// ****************************************************************

		var factoryKeys = CONFIG.addKeys("taskFactoryKeys", Credentials.RANDOM(SDK));
		Factory taskFactory = new FactoryTemplate().prepareDeploy(SDK, factoryKeys)
		                                           .deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER, "2.5"));
		CONFIG.addContract("taskFactory", taskFactory);
		REFRESH();

		// ****************************************************************
		// Set Bank on Factory and Factory on Bank
		// ****************************************************************

		var bank = CONFIG.contract(BankRoot.class, SDK, "bankRoot");
		var bankOwner = CONFIG.contract(SafeMultisigWallet.class, SDK, "bankOwner", "bankOwnerKeys");

		taskFactory.setBank(new Address(bank.address())).callTree(true);
		bank.setFactory(new Address(taskFactory.address())).sendFrom(bankOwner, CurrencyUnit.VALUE(EVER, "0.1"));

		REFRESH();

		// ****************************************************************
		// Set Task Code
		// ****************************************************************

		//var root = CONFIG.contract(BankRoot.class, SDK, "bankRoot");
		var preciseOnRequestCell = TaskPreciseOnRequestTemplate.DEFAULT_TVC().codeCell(SDK);
		var medianizedOnRequestCell = TaskMedianizedOnRequestTemplate.DEFAULT_TVC().codeCell(SDK);
		var preciseFeedCell = TaskPreciseValueFeedTemplate.DEFAULT_TVC().codeCell(SDK);
		var medianizedFeedCell = TaskMedianizedValueFeedTemplate.DEFAULT_TVC().codeCell(SDK);

		//и закинуть в setStorageCode контракта BankRoot
		taskFactory.setTaskCode(TaskType.PRECISE_IMMEDIATE.value(),
		                        preciseOnRequestCell).callTree(true);
		taskFactory.setTaskCode(TaskType.MEDIAN_IMMEDIATE.value(),
		                        medianizedOnRequestCell).callTree(true);
		taskFactory.setTaskCode(TaskType.PRECISE_FEED.value(),
		                        preciseFeedCell).callTree(true);
		taskFactory.setTaskCode(TaskType.MEDIAN_FEED.value(),
		                        medianizedFeedCell).callTree(true);

		//var remoteBoc = new TvmCell(taskFactory._taskCodes().getAsMap().get("_taskCodes").toString());

		REFRESH();
		//assertNotEquals(remoteBoc.cellBoc(), TvmCell.EMPTY().cellBoc());

		// ****************************************************************
		// Deploy Task
		// ****************************************************************

		String taskName = TEMPLATES[0].name() + new Random().nextInt();
		var publishHandle = taskFactory.publishCustomTask(TaskType.PRECISE_IMMEDIATE.value(),
		                                                  "ETH/USD Price (On Request)",
		                                                  TEMPLATES[0].json(),
		                                                  3,
		                                                  CurrencyUnit.VALUE(EVER, "0.2"))
		                               .sendFromTree(bankOwner,
		                                             CurrencyUnit.VALUE(EVER, "2.2"),
		                                             true,
		                                             MessageFlag.EXACT_VALUE_GAS,
		                                             true,
		                                             BankRootTemplate.DEFAULT_ABI(),
		                                             TaskPreciseOnRequestTemplate.DEFAULT_ABI(),
		                                             BankStorageTemplate.DEFAULT_ABI(),
		                                             TIP3TokenWalletTemplate.DEFAULT_ABI());
		var taskAddress = taskFactory.getTaskAddress(TaskType.PRECISE_IMMEDIATE.value(),
		                                             new Address(bankOwner.address()),
		                                             taskName).get().value0();
		logger.log(DEBUG, "Task Address:" + taskAddress.makeAddrStd());
		var task = new TaskPreciseOnRequest(SDK, taskAddress.makeAddrStd());
		CONFIG.addContract("preciseImmediateTask",task);
		logger.log(DEBUG, "Task Settings:" + task._taskSettings().get());
		var storageAddr = bank.storageOf(taskAddress, StorageOwnerType.TASK_STORAGE.value()).get().value0();
		logger.log(DEBUG, "Storage of Task:" + storageAddr.makeAddrStd());
		var taskStorage = new BankStorage(SDK, storageAddr.makeAddrStd());
		var taskStorageBalance = taskStorage._valueBalance().get()._valueBalance();
		logger.log(DEBUG, taskStorageBalance.toString());
		assertEquals(CurrencyUnit.VALUE(EVER, "0.5"), taskStorageBalance);

		REFRESH();
	}


	@Order(2)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void validator_deploy() throws IOException, EverSdkException, InterruptedException {

		// ****************************************************************
		// Deploy Validators
		// ****************************************************************

		var validatorKeys0 = CONFIG.addKeys("validator0Keys", Credentials.RANDOM(SDK));
		var validator0 = new OracleValidatorTemplate()
				.prepareDeploy(
						SDK,
						validatorKeys0,
						TvmCell.EMPTY(),
						"Deplant Test Oracle Validator #0",
						"aaaa")
				.deployWithGiver(GIVER, EVER_TEN);

		CONFIG.addContract("validator0", validator0);
		REFRESH();

		var validatorKeys1 = CONFIG.addKeys("validator1Keys", Credentials.RANDOM(SDK));
		var validator1 = new OracleValidatorTemplate()
				.prepareDeploy(
						SDK,
						validatorKeys1,
						TvmCell.EMPTY(),
						"Deplant Test Oracle Validator #1",
						"aaaa")
				.deployWithGiver(GIVER, EVER_TEN);

		CONFIG.addContract("validator1", validator1);
		REFRESH();

		var validatorKeys2 = CONFIG.addKeys("validator2Keys", Credentials.RANDOM(SDK));
		var validator2 = new OracleValidatorTemplate()
				.prepareDeploy(
						SDK,
						validatorKeys2,
						TvmCell.EMPTY(),
						"Deplant Test Oracle Validator #2",
						"aaaa")
				.deployWithGiver(GIVER, EVER_TEN);

		CONFIG.addContract("validator2", validator2);
		REFRESH();

		var validatorKeys3 = CONFIG.addKeys("validator3Keys", Credentials.RANDOM(SDK));
		var validator3 = new OracleValidatorTemplate()
				.prepareDeploy(
						SDK,
						validatorKeys3,
						TvmCell.EMPTY(),
						"Deplant Test Oracle Validator #3",
						"aaaa")
				.deployWithGiver(GIVER, EVER_TEN);

		CONFIG.addContract("validator3", validator3);
		REFRESH();

		// ****************************************************************
		// Set Subscription Code
		// ****************************************************************

		validator0.setSubscriptionCode(TvmCell.fromJava(new TaskSubscriptionTemplate().tvc()
		                                                                             .code(SDK)))
		         .call();
		validator1.setSubscriptionCode(TvmCell.fromJava(new TaskSubscriptionTemplate().tvc()
		                                                                              .code(SDK)))
		          .call();
		validator2.setSubscriptionCode(TvmCell.fromJava(new TaskSubscriptionTemplate().tvc()
		                                                                              .code(SDK)))
		          .call();
		validator3.setSubscriptionCode(TvmCell.fromJava(new TaskSubscriptionTemplate().tvc()
		                                                                              .code(SDK)))
		          .call();


		REFRESH();
	}

}