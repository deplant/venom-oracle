package tech.deplant.osiris.contract.test.junit.sequence;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.io.JsonResource;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;
import tech.deplant.osiris.StorageOwnerType;
import tech.deplant.osiris.TaskType;
import tech.deplant.osiris.contract.BankRoot;
import tech.deplant.osiris.contract.BankStorage;
import tech.deplant.osiris.contract.Factory;
import tech.deplant.osiris.contract.TaskPreciseOnRequest;
import tech.deplant.osiris.contract.test.template.EverUsdMedianizedTemplate;
import tech.deplant.osiris.contract.test.template.StoredTaskTemplate;
import tech.deplant.osiris.template.*;

import java.io.IOException;
import java.util.Random;

import static java.lang.System.Logger.Level.DEBUG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.osiris.contract.test.junit.TestContext.*;

@DisplayName("full task sequence")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.SAME_THREAD)
public class TaskSequenceTest {
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
	public void task_sequence() throws IOException, EverSdkException, InterruptedException {

		// ****************************************************************
		// Factory Deploy
		// ****************************************************************

		var factoryKeys = CONFIG.addKeys("taskFactoryKeys", Credentials.RANDOM(SDK));
		Factory taskFactory = new FactoryTemplate().prepareDeploy(SDK, factoryKeys)
		                                           .deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER, "0.5"));
		CONFIG.addContract("taskFactory", taskFactory);
		REFRESH();

		// ****************************************************************
		// Set Bank on Factory and Factory on Bank
		// ****************************************************************

		var bank = CONFIG.contract(BankRoot.class, SDK, "bankRoot");
		var bankOwner = CONFIG.contract(SafeMultisigWallet.class, SDK, "bankOwner", "bankOwnerKeys");

		if (bank.account().isActive()) {
			GIVER.give(bankOwner.address(), CurrencyUnit.VALUE(EVER, "5")).call();
			bank.setFactory(new Address(taskFactory.address())).sendFrom(bankOwner, CurrencyUnit.VALUE(EVER, "0.1"));
		}
		taskFactory.setBank(new Address(bank.address())).callTree(true);


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
		REFRESH();

		// ****************************************************************
		// Deploy Precise-OnRequest Task
		// ****************************************************************

		var body1 = new JsonResource("ever_usd_feed_v03_precise_onrequest.json").get();
		String taskName1 = "ETH-USD Precise-OnRequest " + new Random().nextInt();
		taskFactory.publishCustomTask(TaskType.PRECISE_IMMEDIATE.value(),
		                              taskName1,
		                              body1,
		                              3,
		                              CurrencyUnit.VALUE(EVER, "0.2"))
		           .sendFromTree(bankOwner,
		                         CurrencyUnit.VALUE(EVER, "3.2"),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true,
		                         BankRootTemplate.DEFAULT_ABI(),
		                         TaskPreciseOnRequestTemplate.DEFAULT_ABI(),
		                         BankStorageTemplate.DEFAULT_ABI(),
		                         TIP3TokenWalletTemplate.DEFAULT_ABI(),
		                         TIP3TokenRootTemplate.DEFAULT_ABI());
		var taskAddress1 = taskFactory.getTaskAddress(TaskType.PRECISE_IMMEDIATE.value(),
		                                             new Address(bankOwner.address()),
		                                             taskName1).get().value0();

		logger.log(DEBUG, "Task Address:" + taskAddress1.makeAddrStd());
		var task1 = new TaskPreciseOnRequest(SDK, taskAddress1.makeAddrStd());

		CONFIG.addContract("preciseImmediateTask", task1);

		logger.log(DEBUG, "Task Settings:" + task1._taskSettings().get());
		var storageAddr1 = bank.storageOf(taskAddress1, StorageOwnerType.TASK_STORAGE.value()).get().value0();
		logger.log(DEBUG, "Storage of Task:" + storageAddr1.makeAddrStd());
		var taskStorage1 = new BankStorage(SDK, storageAddr1.makeAddrStd());
		var taskStorageBalance1 = taskStorage1._valueBalance().get()._valueBalance();
		logger.log(DEBUG, taskStorageBalance1.toString());

		assertEquals(CurrencyUnit.VALUE(EVER, "0.5"), taskStorageBalance1);
		assertEquals(storageAddr1, task1._storage().get()._storage());

		REFRESH();

		// ****************************************************************
		// Deploy Precise Feed
		// ****************************************************************

		var body2 = new JsonResource("ever_usd_feed_v03_precise_feed.json").get();
		String taskName2 = "ETH-USD Precise-Feed " + new Random().nextInt();
		taskFactory.publishCustomTask(TaskType.PRECISE_FEED.value(),
		                              taskName2,
		                              body2,
		                              3,
		                              CurrencyUnit.VALUE(EVER, "0.2"))
		           .sendFromTree(bankOwner,
		                         CurrencyUnit.VALUE(EVER, "3.2"),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true,
		                         BankRootTemplate.DEFAULT_ABI(),
		                         TaskPreciseOnRequestTemplate.DEFAULT_ABI(),
		                         BankStorageTemplate.DEFAULT_ABI(),
		                         TIP3TokenWalletTemplate.DEFAULT_ABI(),
		                         TIP3TokenRootTemplate.DEFAULT_ABI());
		var taskAddress2 = taskFactory.getTaskAddress(TaskType.PRECISE_FEED.value(),
		                                             new Address(bankOwner.address()),
		                                             taskName2).get().value0();

		logger.log(DEBUG, "Task Address:" + taskAddress2.makeAddrStd());
		var task2 = new TaskPreciseOnRequest(SDK, taskAddress2.makeAddrStd());

		CONFIG.addContract("preciseFeedTask", task2);

		logger.log(DEBUG, "Task Settings:" + task2._taskSettings().get());
		var storageAddr2 = bank.storageOf(taskAddress2, StorageOwnerType.TASK_STORAGE.value()).get().value0();
		logger.log(DEBUG, "Storage of Task:" + storageAddr2.makeAddrStd());
		var taskStorage2 = new BankStorage(SDK, storageAddr2.makeAddrStd());
		var taskStorageBalance2 = taskStorage2._valueBalance().get()._valueBalance();
		logger.log(DEBUG, taskStorageBalance2.toString());

		assertEquals(CurrencyUnit.VALUE(EVER, "0.5"), taskStorageBalance2);
		assertEquals(storageAddr2, task2._storage().get()._storage());

		REFRESH();

		// ****************************************************************
		// Deploy Medianized OnRequest
		// ****************************************************************

		var body3 = new JsonResource("ever_usd_feed_v03_medianized_onrequest.json").get();
		String taskName3 = "ETH-USD Medianized-OnRequest " + new Random().nextInt();
		taskFactory.publishCustomTask(TaskType.MEDIAN_IMMEDIATE.value(),
		                              taskName3,
		                              body3,
		                              3,
		                              CurrencyUnit.VALUE(EVER, "0.2"))
		           .sendFromTree(bankOwner,
		                         CurrencyUnit.VALUE(EVER, "3.2"),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true,
		                         BankRootTemplate.DEFAULT_ABI(),
		                         TaskPreciseOnRequestTemplate.DEFAULT_ABI(),
		                         BankStorageTemplate.DEFAULT_ABI(),
		                         TIP3TokenWalletTemplate.DEFAULT_ABI(),
		                         TIP3TokenRootTemplate.DEFAULT_ABI());
		var taskAddress3 = taskFactory.getTaskAddress(TaskType.MEDIAN_IMMEDIATE.value(),
		                                             new Address(bankOwner.address()),
		                                             taskName3).get().value0();

		logger.log(DEBUG, "Task Address:" + taskAddress3.makeAddrStd());
		var task3 = new TaskPreciseOnRequest(SDK, taskAddress3.makeAddrStd());

		CONFIG.addContract("medianImmediateTask", task3);

		logger.log(DEBUG, "Task Settings:" + task3._taskSettings().get());
		var storageAddr3 = bank.storageOf(taskAddress3, StorageOwnerType.TASK_STORAGE.value()).get().value0();
		logger.log(DEBUG, "Storage of Task:" + storageAddr3.makeAddrStd());
		var taskStorage3 = new BankStorage(SDK, storageAddr3.makeAddrStd());
		var taskStorageBalance3 = taskStorage3._valueBalance().get()._valueBalance();
		logger.log(DEBUG, taskStorageBalance3.toString());

		assertEquals(CurrencyUnit.VALUE(EVER, "0.5"), taskStorageBalance3);
		assertEquals(storageAddr3, task3._storage().get()._storage());

		REFRESH();

		// ****************************************************************
		// Deploy Medianized Feed
		// ****************************************************************

		var body4 = new JsonResource("ever_usd_feed_v03_medianized_feed.json").get();
		String taskName4 = "ETH-USD Medianized-Feed " + new Random().nextInt();
		GIVER.give(bankOwner.address(), CurrencyUnit.VALUE(EVER,"30")).call();
		taskFactory.publishCustomTask(TaskType.MEDIAN_FEED.value(),
		                              taskName4,
		                              body4,
		                              3,
		                              CurrencyUnit.VALUE(EVER, "0.2"))
		           .sendFromTree(bankOwner,
		                         CurrencyUnit.VALUE(EVER, "3.2"),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true,
		                         BankRootTemplate.DEFAULT_ABI(),
		                         TaskPreciseOnRequestTemplate.DEFAULT_ABI(),
		                         BankStorageTemplate.DEFAULT_ABI(),
		                         TIP3TokenWalletTemplate.DEFAULT_ABI(),
		                         TIP3TokenRootTemplate.DEFAULT_ABI());
		var taskAddress4 = taskFactory.getTaskAddress(TaskType.MEDIAN_FEED.value(),
		                                             new Address(bankOwner.address()),
		                                             taskName4).get().value0();

		logger.log(DEBUG, "Task Address:" + taskAddress4.makeAddrStd());
		var task4 = new TaskPreciseOnRequest(SDK, taskAddress4.makeAddrStd());

		CONFIG.addContract("medianFeedTask", task4);

		logger.log(DEBUG, "Task Settings:" + task4._taskSettings().get());
		var storageAddr4 = bank.storageOf(taskAddress4, StorageOwnerType.TASK_STORAGE.value()).get().value0();
		logger.log(DEBUG, "Storage of Task:" + storageAddr4.makeAddrStd());
		var taskStorage4 = new BankStorage(SDK, storageAddr4.makeAddrStd());
		var taskStorageBalance4 = taskStorage4._valueBalance().get()._valueBalance();
		logger.log(DEBUG, taskStorageBalance4.toString());

		assertEquals(CurrencyUnit.VALUE(EVER, "0.5"), taskStorageBalance4);
		assertEquals(storageAddr4, task4._storage().get()._storage());

		REFRESH();
	}
}