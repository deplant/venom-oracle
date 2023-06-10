package tech.deplant.osiris.contract.test.junit.sequence;

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
import tech.deplant.osiris.contract.BankRoot;
import tech.deplant.osiris.contract.Factory;
import tech.deplant.osiris.template.BankRootTemplate;
import tech.deplant.osiris.template.BankStorageTemplate;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.osiris.contract.test.junit.TestContext.*;

@DisplayName("full bank sequence")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.SAME_THREAD)
public class BankSequenceTest {
	public static final System.Logger logger = System.getLogger(tech.deplant.osiris.contract.test.junit.sequence.BankSequenceTest.class.getName());
	public static boolean redeploy;

	@BeforeAll
	public static void getContext() throws IOException {
		INIT();
		SDK = SDK_LOCAL_NODE;
		GIVER = GIVER_LOCAL;
		redeploy = true;
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void bank_sequence() throws IOException, EverSdkException {

		SafeMultisigWallet bankOwner;

		if (redeploy) {

			// ****************************************************************
			// Owner
			// ****************************************************************

			var bankOwnerKeys = Credentials.RANDOM(SDK);
			bankOwner = new SafeMultisigWalletTemplate()
					.prepareDeploy(SDK,
					               bankOwnerKeys,
					               new BigInteger[]{bankOwnerKeys.publicBigInt()},
					               1).deployWithGiver(GIVER, EVER_TEN);

			CONFIG.addKeys("bankOwnerKeys", bankOwnerKeys);
			CONFIG.addContract("bankOwner", bankOwner);

		} else {
			bankOwner = CONFIG.contract(SafeMultisigWallet.class, SDK, "bankOwner", "bankOwnerKeys");
		}

		// ****************************************************************
		// Bank Root Deploy
		// ****************************************************************

		BankRoot bankRoot;
		var tokenAddress = CONFIG.contract(TIP3TokenRoot.class, SDK, "tip3Root").address();
		var factoryAddress = CONFIG.contract(Factory.class, SDK, "taskFactory").address();

		if (redeploy) {
			var keys = CONFIG.addKeys("bankRootKeys", Credentials.RANDOM(SDK));
			CONFIG.addKeys("bankRootKeys", keys);


			bankRoot = new BankRootTemplate()
					.prepareDeploy(SDK,
					               keys,
					               new Address(bankOwner.address()),
					               new Address(tokenAddress),
					               new Address(factoryAddress))
					.deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER, "1"));
			CONFIG.addContract("bankRoot", bankRoot);

			REFRESH();

		} else {
			bankRoot = CONFIG.contract(BankRoot.class, SDK, "bankRoot", "bankRootKeys");
		}

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

		if (factory.account().isActive()) {
			GIVER.give(factory.address(), CurrencyUnit.VALUE(EVER, "5")).call();
			factory.setBank(new Address(bankRoot.address())).callTree(true);
		}
		bankRoot.setFactory(new Address(factory.address())).sendFrom(bankOwner, CurrencyUnit.VALUE(EVER, "0.1"));

		REFRESH();
		assertNotEquals(remoteBoc.cellBoc(), TvmCell.EMPTY().cellBoc());


	}


}