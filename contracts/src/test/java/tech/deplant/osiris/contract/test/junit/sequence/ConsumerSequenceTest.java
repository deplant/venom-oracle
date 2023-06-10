package tech.deplant.osiris.contract.test.junit.sequence;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.osiris.StorageOwnerType;
import tech.deplant.osiris.contract.BankRoot;
import tech.deplant.osiris.contract.BankStorage;
import tech.deplant.osiris.contract.TaskPreciseOnRequest;
import tech.deplant.osiris.contract.test.template.EverUsdMedianizedTemplate;
import tech.deplant.osiris.contract.test.template.StoredTaskTemplate;
import tech.deplant.osiris.template.ExampleConsumerTemplate;

import java.io.IOException;
import java.math.BigInteger;

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

		// ****************************************************************
		// Deploy Consumer
		// ****************************************************************

		var consumerKeys = CONFIG.addKeys("exampleConsumerKeys", Credentials.RANDOM(SDK));
		var consumer = new ExampleConsumerTemplate().prepareDeploy(
				SDK,
				consumerKeys).deployWithGiver(GIVER, EVER_FIVE);

		CONFIG.addContract("exampleConsumer", consumer);

		assertEquals(BigInteger.ZERO, consumer.getMedianizedResult().get().value0());
		assertEquals(TvmCell.EMPTY().cellBoc(), consumer.getPreciseResult().getAsMap().get("value0").toString());

		REFRESH();

		// ****************************************************************
		// Deploy Payer
		// ****************************************************************


		var payerKeys = Credentials.RANDOM(SDK);
		var payer = new SafeMultisigWalletTemplate()
				.prepareDeploy(SDK,
				               payerKeys,
				               new BigInteger[]{payerKeys.publicBigInt()},
				               1).deployWithGiver(GIVER, EVER_TEN);

		CONFIG.addKeys("payerKeys", payerKeys);
		CONFIG.addContract("payerOwner", payer);

		REFRESH();


		// ****************************************************************
		// Deploy Payer Storage
		// ****************************************************************

		var bankRoot = CONFIG.contract(BankRoot.class, SDK, "bankRoot");

		var valueDepositedOnDeploy = CurrencyUnit.VALUE(EVER, "5");
		var valueDepositedSeparately = CurrencyUnit.VALUE(EVER, "2");

		//Затем воспользоваться deployStorage для размещения BankStorage
		bankRoot.deployStorage(valueDepositedOnDeploy)
		        .sendFromTree(payer,
		                      valueDepositedOnDeploy.add(CurrencyUnit.VALUE(EVER, "0.1"))
		                                            .add(CurrencyUnit.VALUE(EVER, "0.2")),
		                      true,
		                      MessageFlag.EXACT_VALUE_GAS,
		                      true, BankStorage.DEFAULT_ABI());

		var bankStorageAddress = bankRoot
				.storageOf(new Address(payer.address()), StorageOwnerType.CONSUMER_STORAGE.value())
				.get()
				.value0()
				.makeAddrStd();

		System.out.println("bankStorageAddress 2: " + bankStorageAddress);

		//assertEquals(bankStorageAddress1, bankStorageAddress2);

		//Разместить EVER/VENOM в BankStorage переводом
		var payerStorage = new BankStorage(SDK, bankStorageAddress); //

		CONFIG.addContract("payerStorage", payerStorage);

		REFRESH();

		// ****************************************************************
		// Deposit Value
		// ****************************************************************

		payerStorage.depositValue(valueDepositedSeparately)
		           .sendFromTree(payer,
		                         valueDepositedSeparately.add(CurrencyUnit.VALUE(EVER, "0.1")),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true);

		//Получить баланс своего BankStorage на вьюхе DepositsView
		var getValueBalance = payerStorage._valueBalance().get()._valueBalance();
		System.out.println("My Storage Value Balance: " + getValueBalance);

		assertEquals(valueDepositedOnDeploy.add(valueDepositedSeparately), getValueBalance);

		REFRESH();

		// ****************************************************************
		// Add Allowance
		// ****************************************************************

		var preciseTask = CONFIG.contract(TaskPreciseOnRequest.class,SDK,"preciseImmediateTask");
		var valueAllowance = CurrencyUnit.VALUE(EVER, "1");
		var tokenAllowance = CurrencyUnit.VALUE(EVER, "10");

		payerStorage.addAllowance(new Address(consumer.address()), new Address(preciseTask.address()), valueAllowance, tokenAllowance)
		           .sendFromTree(payer,
		                         valueDepositedSeparately.add(CurrencyUnit.VALUE(EVER, "0.1")),
		                         true,
		                         MessageFlag.EXACT_VALUE_GAS,
		                         true);

		assertEquals(valueDepositedOnDeploy.add(valueDepositedSeparately), getValueBalance);

		REFRESH();

	}
}
