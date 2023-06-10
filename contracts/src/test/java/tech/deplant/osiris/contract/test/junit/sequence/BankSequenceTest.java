package tech.deplant.osiris.contract.test.junit.sequence;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.TIP3TokenRoot;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.osiris.StorageOwnerType;
import tech.deplant.osiris.contract.BankStorage;
import tech.deplant.osiris.contract.Factory;
import tech.deplant.osiris.template.BankRootTemplate;
import tech.deplant.osiris.template.BankStorageTemplate;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.osiris.contract.test.junit.TestContext.*;

@DisplayName("full bank sequence")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.SAME_THREAD)
public class BankSequenceTest {
	public static final System.Logger logger = System.getLogger(tech.deplant.osiris.contract.test.junit.sequence.BankSequenceTest.class.getName());

	@BeforeAll
	public static void getContext() throws IOException {
		INIT();
		SDK = SDK_LOCAL_NODE;
		GIVER = GIVER_LOCAL;
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void bank_sequence() throws IOException, EverSdkException {

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
				               new Address(tokenAddress),
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
		var factory = CONFIG.contract(Factory.class, SDK, "taskFactory", "taskFactoryKeys");

		factory.setBank(new Address(bankRoot.address())).callTree(true);
		bankRoot.setFactory(new Address(factory.address())).sendFrom(bankOwner, CurrencyUnit.VALUE(EVER, "0.1"));

		REFRESH();
		assertNotEquals(remoteBoc.cellBoc(), TvmCell.EMPTY().cellBoc());

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


}