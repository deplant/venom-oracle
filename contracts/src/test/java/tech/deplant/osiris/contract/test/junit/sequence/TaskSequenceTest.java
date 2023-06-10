package tech.deplant.osiris.contract.test.junit.sequence;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.contract.TIP3TokenWallet;
import tech.deplant.java4ever.framework.datatype.Address;
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
		                                                  taskName,
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
}