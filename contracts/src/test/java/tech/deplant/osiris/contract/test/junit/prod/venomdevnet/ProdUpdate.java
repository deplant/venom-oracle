package tech.deplant.osiris.contract.test.junit.prod.venomdevnet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.io.ByteResource;
import tech.deplant.java4ever.framework.Convert;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.MessageFlag;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.contract.TIP3TokenRoot;
import tech.deplant.java4ever.framework.contract.TIP3TokenWallet;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.datatype.Uint;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;
import tech.deplant.osiris.TaskType;
import tech.deplant.osiris.contract.BankRoot;
import tech.deplant.osiris.contract.Factory;
import tech.deplant.osiris.template.*;

import javax.swing.plaf.ColorUIResource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.System.Logger.Level.DEBUG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.osiris.contract.test.junit.TestContext.*;

//@Disabled
@DisplayName("updates for prod deployments")
@TestMethodOrder(MethodOrderer.class)
public class ProdUpdate {

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
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void deploy_bank() throws IOException, EverSdkException {


		// ****************************************************************
		// Bank Root Deploy
		// ****************************************************************

		BankRoot bankRoot;
		var tokenAddress = CONFIG.contract(TIP3TokenRoot.class, SDK, "tip3Root").address();
		var factoryAddress = CONFIG.contract(Factory.class, SDK, "taskFactory").address();

		var keys = CONFIG.addKeys("bankRootKeys", Credentials.RANDOM(SDK));
		CONFIG.addKeys("bankRootKeys", keys);


		bankRoot = new BankRootTemplate()
				.prepareDeploy(SDK,
				               keys,
				               new Address(GIVER.address()),
				               new Address(tokenAddress),
				               new Address(factoryAddress))
				.deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER, "5"));
		CONFIG.addContract("bankRoot", bankRoot);

		System.out.println("Bank: " + bankRoot.address());

		REFRESH();

		// ****************************************************************
		// Set Storage Code
		// ****************************************************************

		//var root = CONFIG.contract(BankRoot.class, SDK, "bankRoot");
		var storageCodeCell = new BankStorageTemplate().tvc().codeCell(SDK);

		var bankOwner = new SafeMultisigWallet(SDK, GIVER.address(), GIVER.credentials());
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
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void update_task_code() throws IOException, EverSdkException {

		var taskFactory = CONFIG.contract(Factory.class,SDK,"taskFactory","taskFactoryKeys");

		GIVER.give(taskFactory.address(),CurrencyUnit.VALUE(EVER,"5"));

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

	}


	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void deploy_factory() throws IOException, EverSdkException {
		// ****************************************************************
		// Factory Deploy
		// ****************************************************************

		var factoryKeys = CONFIG.addKeys("taskFactoryKeys", Credentials.RANDOM(SDK));
		Factory taskFactory = new FactoryTemplate().prepareDeploy(SDK, factoryKeys)
		                                           .deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER, "5"));
		CONFIG.addContract("taskFactory", taskFactory);
		REFRESH();

		// ****************************************************************
		// Set Bank on Factory and Factory on Bank
		// ****************************************************************

		var bank = CONFIG.contract(BankRoot.class, SDK, "bankRoot");
		var bankOwner = new SafeMultisigWallet(SDK, GIVER.address(), GIVER.credentials());
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

	}

	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void deploy_owner() throws IOException, EverSdkException {

		// ****************************************************************
		// Owner
		// ****************************************************************

		var tip3OwnerKeys = new Credentials("a97a80b4dce99070da9abaa7ca49155c31fbbec8cc40b158fc04091f19d30a42",
		                                    "c89f99b6ee581fdb517b43bb021491bb3ff8178e8902997b203fc42dc5019b55");
		var tip3Owner = new SafeMultisigWalletTemplate()
				.prepareDeploy(SDK,
				               tip3OwnerKeys,
				               new BigInteger[]{tip3OwnerKeys.publicBigInt()},
				               1).deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER,"1"));

		CONFIG.addKeys("tip3OwnerKeys", tip3OwnerKeys);
		CONFIG.addContract("tip3Owner", tip3Owner);

		logger.log(DEBUG, "Owner: " + tip3Owner.address());

		REFRESH();
	}

	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void find_nonce() throws IOException, EverSdkException {

		var tip3Owner = CONFIG.contract(SafeMultisigWallet.class, SDK,"tip3Owner", "tip3OwnerKeys");
		logger.log(DEBUG, "Owner: " + tip3Owner.address());

		// ****************************************************************
		// Root Deploy
		// ****************************************************************

		var tip3Root = new TIP3TokenRoot(SDK, "0:6aba2723d0a0ec80a2443469953717a20ffe84ab0edcc3bffc7ac80496078f29");
		var boc = new String(Base64.getEncoder().encode(new ByteResource("message_boc_7f3e640a8779bba2fa9e20449f735390bcf4f082725e923a52ff232124f28e20.boc").get()));
		var data = new String(Base64.getEncoder().encode(new ByteResource("message_data_7f3e640a8779bba2fa9e20449f735390bcf4f082725e923a52ff232124f28e20.boc").get()));

		var message = Abi.decodeMessage(tip3Root.sdk().context(),
		                  tip3Root.abi().ABI(),
		                  boc,
		                  false,
		                  null,
		                  null);

		logger.log(DEBUG,message.value());

		var initData = Abi.decodeInitialData(tip3Root.sdk().context(),
		                                tip3Root.abi().ABI(),
		                                data,
		                                false);

		logger.log(DEBUG,message.value());
		logger.log(DEBUG,initData.initialData());
//
//		IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MAX_VALUE).sequential().forEach(i -> {
//
//
//			String deployStatement = null;
//			var ownerAddress = new Address(tip3Owner.address());
//			TvmCell cell;
//			try {
//				cell = TIP3TokenWalletTemplate.DEFAULT_TVC()
//				                       .codeCell(SDK);
//			} catch (EverSdkException e) {
//				throw new RuntimeException(e);
//			}
//			try {
//				deployStatement = new TIP3TokenRootTemplate().prepareDeploy(SDK,
//				                                                            tip3Owner.credentials(),
//				                                                            "CHIP3 Oracle Token",
//				                                                            "CHIP3",
//				                                                            9,
//				                                                            ownerAddress,
//				                                                            cell,
//				                                                            BigInteger.valueOf(i),
//				                                                            Address.ZERO,
//				                                                            Address.ZERO,
//				                                                            BigInteger.ZERO,
//				                                                            BigInteger.ZERO,
//				                                                            false,
//				                                                            false,
//				                                                            false,
//				                                                            ownerAddress).toAddress();
//			} catch (EverSdkException e) {
//				throw new RuntimeException(e);
//			} catch (JsonProcessingException e) {
//				throw new RuntimeException(e);
//			}
//
//			if (deployStatement.equals("0:6aba2723d0a0ec80a2443469953717a20ffe84ab0edcc3bffc7ac80496078f29")) {
//				System.out.println(i);
//			}
//		});
	}

	@Order(1)
	@Test
	@Execution(ExecutionMode.SAME_THREAD)
	public void deploy_token() throws IOException, EverSdkException {
		var tip3Owner = CONFIG.contract(SafeMultisigWallet.class, SDK,"tip3Owner", "tip3OwnerKeys");

		// ****************************************************************
		// Root Deploy
		// ****************************************************************

		var TIP3_NONCE = Uint.fromJava(256, "0x00000000000000000000000000000000000000000000000000000000138062ca").toJava();
		//var TIP3_NONCE = new BigInteger("256");

		var deployStatement = new TIP3TokenRootTemplate().prepareDeploy(SDK,
		                                                                tip3Owner.credentials(),
		                                                                "CHIP3 Oracle Token",
		                                                                "CHIP3",
		                                                                9,
		                                                                new Address(tip3Owner.address()),
		                                                                TIP3TokenWalletTemplate.DEFAULT_TVC()
		                                                                                       .codeCell(SDK),
		                                                                TIP3_NONCE,
		                                                                Address.ZERO,
		                                                                Address.ZERO,
		                                                                BigInteger.ZERO,
		                                                                BigInteger.ZERO,
		                                                                false,
		                                                                false,
		                                                                false,
		                                                                new Address(tip3Owner.address()));
		var tip3Root = deployStatement.deployWithGiver(tip3Owner, CurrencyUnit.VALUE(CurrencyUnit.Ever.EVER, "1.1"));

		CONFIG.addKeys("tip3RootKeys", tip3Owner.credentials());
		CONFIG.addContract("tip3Root", tip3Root);


		logger.log(DEBUG, "TIP3 Root: " + tip3Root.address());

		REFRESH();

		// ****************************************************************
		// Mint to wallet
		// ****************************************************************

		var mintedAmount = CurrencyUnit.VALUE(EVER, "3000");

		var payload = tip3Root.mint(
				                      mintedAmount,
				                      new Address(tip3Owner.address()),
				                      CurrencyUnit.VALUE(EVER, "0.2"),
				                      new Address(tip3Owner.address()),
				                      false,
				                      TvmCell.EMPTY())
		                      .sendFromTree(tip3Owner,
		                                    CurrencyUnit.VALUE(EVER, "0.4"),
		                                    true,
		                                    MessageFlag.EXACT_VALUE_GAS,
		                                    true, TIP3TokenRoot.DEFAULT_ABI(), TIP3TokenWallet.DEFAULT_ABI());

		var tip3OwnerWallet = new TIP3TokenWallet(SDK,
		                                          tip3Root.walletOf(new Address(tip3Owner.address()))
		                                                  .get()
		                                                  .value0()
		                                                  .makeAddrStd());

		CONFIG.addContract("tip3OwnerWallet", tip3OwnerWallet);

		logger.log(DEBUG, "TIP3 Wallet for Owner: " + tip3OwnerWallet.address());

		assertEquals(mintedAmount, tip3OwnerWallet.balance().get().value0());

		logger.log(DEBUG, "TIP3 Wallet balance: " + tip3OwnerWallet.balance().get().value0());

		REFRESH();

		// ****************************************************************
		// Deploy Faucet
		// ****************************************************************

		var faucetKeys = Credentials.RANDOM(SDK);

		var defaultFaucetAmount = CurrencyUnit.VALUE(EVER, "100");

		var faucet = new TestFaucetTemplate()
				.prepareDeploy(SDK,
				               faucetKeys,
				               new Address(tip3Root.address()),
				               new Address(tip3Root.address()),
				               defaultFaucetAmount) //amount chip3 token
				.deployWithGiver(GIVER, CurrencyUnit.VALUE(EVER, "0.3"));

		CONFIG.addKeys("faucetKeys", faucetKeys);
		CONFIG.addContract("faucet", faucet);

		logger.log(DEBUG, "Faucet: " + faucet.address());

		REFRESH();

		// ****************************************************************
		// Transfer to Faucet
		// ****************************************************************

		var amountToTransfer = CurrencyUnit.VALUE(EVER, "2000");

		tip3OwnerWallet.transfer(
				amountToTransfer, //chip3
				new Address(faucet.address()),
				new BigInteger("0"),
				new Address(faucet.address()),
				false,
				TvmCell.EMPTY()).sendFromTree(tip3Owner,
		                                      CurrencyUnit.VALUE(EVER, "0.1"),
		                                      true,
		                                      MessageFlag.EXACT_VALUE_GAS,
		                                      true, TIP3TokenRoot.DEFAULT_ABI());

		var faucetWallet = new TIP3TokenWallet(SDK,
		                                       tip3Root.walletOf(new Address(faucet.address()))
		                                               .get()
		                                               .value0()
		                                               .makeAddrStd());

		CONFIG.addContract("faucetWallet", faucetWallet);

		logger.log(DEBUG, "Faucet Wallet: " + faucetWallet.address());
		logger.log(DEBUG, "Faucet Wallet balance: " + faucetWallet.balance().get().value0());

		REFRESH();
	}



}
