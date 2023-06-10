package tech.deplant.osiris.contract.test.junit.sequence;

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
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;
import tech.deplant.osiris.template.TestFaucetTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.osiris.contract.test.junit.TestContext.*;

@DisplayName("full token sequence")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.SAME_THREAD)
public class TokenSequenceTest {
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
	public void token_sequence() throws IOException, EverSdkException, InterruptedException {

		// ****************************************************************
		// Owner
		// ****************************************************************

		var tip3OwnerKeys = Credentials.RANDOM(SDK);
		var tip3Owner = new SafeMultisigWalletTemplate()
				.prepareDeploy(SDK,
				               tip3OwnerKeys,
				               new BigInteger[]{tip3OwnerKeys.publicBigInt()},
				               1).deployWithGiver(GIVER, EVER_TEN);

		CONFIG.addKeys("tip3OwnerKeys", tip3OwnerKeys);
		CONFIG.addContract("tip3Owner", tip3Owner);

		logger.log(System.Logger.Level.DEBUG, "Owner: " + tip3Owner.address());

		REFRESH();

		// ****************************************************************
		// Root Deploy
		// ****************************************************************

		var TIP3_NONCE = new BigInteger(String.valueOf(new Random().nextInt()));
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

		logger.log(System.Logger.Level.DEBUG, "TIP3 Root: " + tip3Root.address());

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

		Thread.sleep(1000);

		CONFIG.addContract("tip3OwnerWallet", tip3OwnerWallet);

		logger.log(System.Logger.Level.DEBUG, "TIP3 Wallet for Owner: " + tip3OwnerWallet.address());

		assertEquals(mintedAmount, tip3OwnerWallet.balance().get().value0());

		logger.log(System.Logger.Level.DEBUG, "TIP3 Wallet balance: " + tip3OwnerWallet.balance().get().value0());

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

		logger.log(System.Logger.Level.DEBUG, "Faucet: " + faucet.address());

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

		logger.log(System.Logger.Level.DEBUG, "Faucet Wallet: " + faucetWallet.address());
		logger.log(System.Logger.Level.DEBUG, "Faucet Wallet balance: " + faucetWallet.balance().get().value0());

		REFRESH();

		// ****************************************************************
		// Get from Faucet
		// ****************************************************************

		faucet
				.faucet(false)
				.sendFromTree(tip3Owner,
				              CurrencyUnit.VALUE(EVER, "0.1"),
				              true,
				              MessageFlag.EXACT_VALUE_GAS,
				              true, TIP3TokenRoot.DEFAULT_ABI(), TIP3TokenWallet.DEFAULT_ABI());

		assertEquals(amountToTransfer.subtract(defaultFaucetAmount), faucetWallet.balance().get().value0());

		assertEquals(mintedAmount.subtract(amountToTransfer).add(defaultFaucetAmount),
		             tip3OwnerWallet.balance().get().value0());

		logger.log(System.Logger.Level.DEBUG, "Faucet Wallet balance: " + faucetWallet.balance().get().value0());
		logger.log(System.Logger.Level.DEBUG, "TIP3 Wallet balance: " + tip3OwnerWallet.balance().get().value0());

		REFRESH();
	}


}