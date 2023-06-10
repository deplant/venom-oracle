package tech.deplant.osiris.contract.test.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.contract.*;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;
import tech.deplant.osiris.StorageOwnerType;
import tech.deplant.osiris.contract.*;
import tech.deplant.osiris.template.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.osiris.contract.test.junit.TestContext.*;


public class SeregaTests {

	static Sdk SDK;

	static {
		try {
			SDK = Sdk.builder()
					.networkEndpoints(new String[]{"https://gql-devnet.venom.network/graphql"})
					.build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@Test
	public void make_seed_and_keys() throws Throwable {
		var seed = Seed.RANDOM(SDK);
		System.out.println(seed.toString());
		System.out.println(Credentials.ofSeed(SDK,
				seed).toString());
		System.out.println("CheckIn seed creds: "
				+ Credentials.ofSeed(SDK,new Seed("lonely stand unfair regular usage unaware employ wear avoid wage heart risk", 12)));
	}

//	New creds the best
//	Seed[phrase=dumb open fold surprise level solid tenant calm stand idle tower ketchup, words=12]
//	Credentials[
//	publicKey=507259d1570fe93d1e4d01cfd13a47ae0bed0eb9ce536f5c8c95edad41eb8057
//	secretKey=2644dc4c8908b84c57628ad090df9cc4f29ac63c565970d4d3acd49b83d08de2
//	]

	@Test
		public void deploy_msigs_rich() throws IOException, EverSdkException, InterruptedException {
			var msigCreds = new Credentials(
					"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
					"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");
			var msig = new SafeMultisigWalletTemplate()
					.prepareDeploy(SDK,
							msigCreds,
							new BigInteger[]{msigCreds.publicBigInt()},
							1).deploy();
					//.deployWithGiver(EverOSGiver.V2(SDK), CurrencyUnit.VALUE(EVER, "1000000"));
		    System.out.println(msig.address());

			//Msig address: 0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761
		}

	@Test
	public void deploy_dao_and_tip3_root() throws IOException, EverSdkException, InterruptedException {
		var msigCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");

		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";
		var msigWallet = new SafeMultisigWallet(SDK, msigAddress, msigCreds);
		System.out.println("Config msig: " + msigWallet);

		//var TIP3_NONCE = new BigInteger(String.valueOf(new Random().nextInt()));
		var TIP3_NONCE = new BigInteger("256");

		var deployStatement = new TIP3TokenRootTemplate().prepareDeploy(SDK,
				msigWallet.credentials(),
				"CHIP3 Oracle Token",
				"CHIP3",
				9,
				new Address(msigAddress),
				TIP3TokenWalletTemplate.DEFAULT_TVC().codeCell(SDK),
				TIP3_NONCE,
				Address.ZERO,
				Address.ZERO,
				BigInteger.ZERO,
				BigInteger.ZERO,
				false,
				false,
				false,
				new Address(msigAddress));
		var addressRoot = deployStatement.deployWithGiver(msigWallet, CurrencyUnit.VALUE(CurrencyUnit.Ever.EVER,"1.1")).address();

		System.out.println("tip3TokenRoot: " + addressRoot);

//		CHIP3 Oracle Token root
//		0:a3682fd1814f293cbf7ba69f5678533bc8576d226a369b6fa07a66073d1d54b1
	}

	@Test
	//@Execution(ExecutionMode.CONCURRENT)
	public void mint_tip3_to_wallet() throws IOException, EverSdkException {
		var msigCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");
		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";
		var addressRoot = "0:a3682fd1814f293cbf7ba69f5678533bc8576d226a369b6fa07a66073d1d54b1";
		var TOKEN_ROOT = new TIP3TokenRoot(SDK, addressRoot, msigCreds);

		var msigWallet = new SafeMultisigWallet(SDK, msigAddress, msigCreds);

		System.out.println(TOKEN_ROOT.address());

		//fabric tokens 0:65063ff6f66b5587dfefaf4729853b1aa23b2bbd0b66ed28aae68387d5fbc209
		var payload = TOKEN_ROOT.mint(
						new BigInteger("499800").multiply(new BigInteger("1000000000")),
						new Address(msigAddress),
						CurrencyUnit.VALUE(EVER, "0"),
						new Address(msigAddress),
						false,
						TvmCell.EMPTY())
				.toPayload();
		var tokenAddress = msigWallet.sendTransaction(new Address(TOKEN_ROOT.address()),
						CurrencyUnit.VALUE(EVER, "0.1"),
						true,
						MessageFlag.EXACT_VALUE_GAS.flag(),
						payload)
				.callTree(true, TIP3TokenRoot.DEFAULT_ABI(), TIP3TokenWallet.DEFAULT_ABI());

		//System.out.println("tokenAddress: " + tokenAddress.extractDeployAddress(msigAddress));
	}

	@Test
	public void tip3_wallet_get_balance_and_tip3_address() throws JsonProcessingException, EverSdkException {

		var msigCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");

		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";

		var addressRoot = "0:a3682fd1814f293cbf7ba69f5678533bc8576d226a369b6fa07a66073d1d54b1";
		var walletRoot = new TIP3TokenRoot(SDK, addressRoot, msigCreds);
		var addressTip3Wallet = walletRoot.walletOf(new Address(msigAddress)).get().value0().makeAddrStd();
		var tip3Wallet = new TIP3TokenWallet(SDK, addressTip3Wallet);
		var ownerTip3Wallet = tip3Wallet.owner().get().value0().toJava();
		System.out.println("addressTip3Wallet: " + addressTip3Wallet);
		var tip3WalletBalance = tip3Wallet.balance().get().value0().divide(new BigInteger("1000000000"));
		System.out.println("tip3WalletBalance: " + tip3WalletBalance);

//		addressTip3Wallet: 0:b19e1973d0c07562f3da8e829bd8d37b82a736c419d56edf9326a2c921a55380
//		tip3WalletBalance: 200
	}

	@Test
	public void tip3_wallet_faucet_get_balance() throws JsonProcessingException, EverSdkException {
		//the same type 3 token wallet

		var faucetAddress = "0:58921218bb3ae1668f9b7b601cf763c15fddb70352333c14404be268766c3432";
		var faucetWallet = new TIP3TokenWallet(SDK, faucetAddress);
		var faucetBalance = faucetWallet.balance().get().value0().divide(new BigInteger("1000000"));
		System.out.println("Faucet Balance: " + faucetBalance);
		var ownerFaucet = faucetWallet.owner().get().value0().toJava();
		System.out.println("Owner faucet: " + ownerFaucet);
	}

	@Test
	public void deploy_faucet() throws JsonProcessingException, EverSdkException {
//		the new creds to faucetDeployCreds
//		publicKey=5148cf94995a5914d8373961ebed0db5377bd82ee2afceabda8bc5e05ecae1ec
//		secretKey=edb338aa5ca37c6a88d80cab05a19e51e0adcece9924e424ca068ec3c44dc58d

		var faucetDeployCreds = new Credentials(
				"5148cf94995a5914d8373961ebed0db5377bd82ee2afceabda8bc5e05ecae1ec",
				"edb338aa5ca37c6a88d80cab05a19e51e0adcece9924e424ca068ec3c44dc58d");

		var tokenRootAddress = new Address("0:a3682fd1814f293cbf7ba69f5678533bc8576d226a369b6fa07a66073d1d54b1");

		var msigCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");

		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";
		var msigWallet = new SafeMultisigWallet(SDK, msigAddress, msigCreds);

		var faucetDeployment = new TestFaucetTemplate()
				.prepareDeploy(SDK, faucetDeployCreds, tokenRootAddress, tokenRootAddress, CurrencyUnit.VALUE(EVER,"100")) //amount chip3 token
				.deployWithGiver(msigWallet, CurrencyUnit.VALUE(EVER,"0.3"));

		System.out.println("Faucet address: " + faucetDeployment.address());

		var faucetWalletAddress = faucetDeployment._tokenWallet().get()._tokenWallet().toJava();

		System.out.println("Faucet new wallet address: " + faucetWalletAddress);

//		Faucet address: 0:9cae4d2963605798616a325f2d8a19603acc1d98990be664a8a39a19798f0f5e
//		Faucet new wallet address: 0:4b1ef82d64226593fe755458c2a9525451c7ef2194ff2066c2709ecf3ea163ac
	}

	@Test
	public void wallet_faucet_transfer() throws JsonProcessingException, EverSdkException {

		var msigCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");

		var tip3WalletAddress = "0:b19e1973d0c07562f3da8e829bd8d37b82a736c419d56edf9326a2c921a55380"; //balance - 500000 chip3
		var tip3Wallet = new TIP3TokenWallet(SDK, tip3WalletAddress);
		//New wallet for user address
		var faucetAddress = new Address("0:9cae4d2963605798616a325f2d8a19603acc1d98990be664a8a39a19798f0f5e");

		var walletTransfer = tip3Wallet.transfer(
				CurrencyUnit.VALUE(EVER,"499900"), //chip3
				faucetAddress,
				new BigInteger("0"),
				faucetAddress,
				false,
				TvmCell.EMPTY()).toPayload();

		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";
		var msigWallet = new SafeMultisigWallet(SDK, msigAddress, msigCreds);

		msigWallet.sendTransaction(new Address(tip3Wallet.address()),
						CurrencyUnit.VALUE(EVER,"0.3"),
						true,
						MessageFlag.EXACT_VALUE_GAS.flag(),
						walletTransfer)
				.callTree(true,TIP3TokenRoot.DEFAULT_ABI(), TIP3TokenWallet.DEFAULT_ABI());
	}

	@Test
	public void wallet_faucet_get_balance() throws JsonProcessingException, EverSdkException {
		var faucetAddress = "0:9cae4d2963605798616a325f2d8a19603acc1d98990be664a8a39a19798f0f5e";
		var faucetWalletAddress = new TestFaucet(SDK, faucetAddress)._tokenWallet().get()._tokenWallet();
		var faucetWallet = new TIP3TokenWallet(SDK, faucetWalletAddress.toJava());
		var faucetBalance = faucetWallet.balance().get().value0().divide(new BigInteger("1000000000"));
		System.out.println("Balance faucet wallet: " + faucetBalance);
	}

	@Test
	public void wallet_faucet_exec() throws JsonProcessingException, EverSdkException {
		var faucetAddress = "0:9cae4d2963605798616a325f2d8a19603acc1d98990be664a8a39a19798f0f5e";
		var faucetPayload = new TestFaucet(SDK, faucetAddress).faucet(false).toPayload();

		var msigCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");
		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";
		var msigWallet = new SafeMultisigWallet(SDK, msigAddress, msigCreds);

		msigWallet.sendTransaction(new Address(faucetAddress),
						CurrencyUnit.VALUE(EVER,"0.1"),
						true,
						MessageFlag.EXACT_VALUE_GAS.flag(),
						faucetPayload)
				.callTree(true,TIP3TokenRoot.DEFAULT_ABI(), TestFaucet.DEFAULT_ABI(), TIP3TokenWallet.DEFAULT_ABI());
		var faucetWalletAddress = "0:4b1ef82d64226593fe755458c2a9525451c7ef2194ff2066c2709ecf3ea163ac";
		var faucetWallet = new TIP3TokenWallet(SDK, faucetWalletAddress);
		var faucetBalance = faucetWallet.balance().get().value0().divide(new BigInteger("1000000000"));
		System.out.println("Balance faucet wallet: " + faucetBalance);
		//Balance faucet wallet: 499800

	}

//	//Banks tests
//	@Test
//	public void bank_root_and_storage_deploy_with_send_transaction() throws JsonProcessingException, EverSdkException {
//		var bankCreds = new Credentials(
//				"9a173db07c6e9fa90b84e73466318c208bfa64d752656ccd117098a40b04ff97",
//				"e0047efafabd39c8f670aff99afe19f975e209a8bf94e13f7f754d5ddb0520e0");
//
//		var msigCreds = new Credentials(
//				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
//				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");
//		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";
//
//		var abiBankRoot = List.of(BankRoot.DEFAULT_ABI(), BankStorage.DEFAULT_ABI(), SafeMultisigWallet.DEFAULT_ABI());
//
//		var msigWallet = new SafeMultisigWallet(SDK, msigAddress, msigCreds);
//
//
//
//		//Попробовать деплой контракта BankRoot / Template
//		var bankRoot = new BankRootTemplate()
//				.prepareDeploy(SDK, bankCreds, new Address(msigAddress))
//				.deployWithGiver(EverOSGiver.V2(SDK), CurrencyUnit.VALUE(EVER, "1000"));
//
//		System.out.println("bankRootAddress: " + bankRoot.address());
//
//		// Взять код из BankStorageTemplate (codeCell)
//		var bankStorageTvm = new BankStorageTemplate().tvc().codeCell(SDK);
//		//и закинуть в setStorageCode контракта BankRoot
//		var storageTvm = bankRoot.setStorageCode(bankStorageTvm).toPayload();
//		var bankRootAddress = bankRoot.address();
//		//Размещаем банкрута
//		msigWallet.sendTransaction(
//				          new Address(bankRootAddress),
//				          CurrencyUnit.VALUE(EVER,"0.1"),
//				          true,
//				          MessageFlag.EXACT_VALUE_GAS.flag(),
//				          storageTvm)
//		          .callTree(true, BankRoot.DEFAULT_ABI(), BankStorage.DEFAULT_ABI(), SafeMultisigWallet.DEFAULT_ABI());
//
//		System.out.println("bankRootAddress: " + bankRootAddress);
//
//		var valueDepositedOnDeploy = CurrencyUnit.VALUE(EVER,"0.1");
//		var valueDepositedSeparately = CurrencyUnit.VALUE(EVER,"0.2");
//
//		//Затем воспользоваться deployStorage для размещения BankStorage
//		var bankRootTvm = bankRoot.deployStorage(valueDepositedOnDeploy).toPayload();
//		//var bankStorageAddress =
//		var bankStorageAddress1 = msigWallet.sendTransaction(
//				                                    new Address(bankRootAddress),
//													CurrencyUnit.VALUE(EVER,"0.1"),
//				                                    true,
//				                                    MessageFlag.EXACT_VALUE_GAS.flag(),
//				                                    bankRootTvm)
//		                                    .callTree(true, BankRoot.DEFAULT_ABI(), BankStorage.DEFAULT_ABI(), SafeMultisigWallet.DEFAULT_ABI())
//		                                    .extractDeployAddress(bankRootAddress);
//
//		var bankStorageAddress2 = bankRoot.storageOf(new Address(msigAddress)).get().value0().makeAddrStd();
//
//		System.out.println("bankStorageAddress 1: " + bankStorageAddress1);
//		System.out.println("bankStorageAddress 2: " + bankStorageAddress2);
//
//		assertEquals(bankStorageAddress1, bankStorageAddress2);
//
//		//Разместить EVER/VENOM в BankStorage переводом
//		var bankStorage = new BankStorage(SDK, bankStorageAddress2); //
//
//		var payload = bankStorage.depositValue(valueDepositedSeparately).toPayload();
//
//		msigWallet.sendTransaction(
//				          new Address(bankStorageAddress2),
//						  CurrencyUnit.VALUE(EVER,"0.1"),
//				          true,
//				          MessageFlag.EXACT_VALUE_GAS.flag(),
//				          payload)
//		          .callTree(true, BankRoot.DEFAULT_ABI(), BankStorage.DEFAULT_ABI(), SafeMultisigWallet.DEFAULT_ABI());
//
//		//Получить баланс своего BankStorage на вьюхе DepositsView
//		var getValueBalance = bankStorage._valueBalance().get()._valueBalance();
//		System.out.println("My Storage Value Balance: " + getValueBalance);
//
//		assertEquals(valueDepositedOnDeploy.add(valueDepositedSeparately), getValueBalance);
//	}

	@Test
	public void test_withdraw_ever() throws JsonProcessingException, EverSdkException {

		var msigCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");

		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";

		var abiBankRoot = List.of();

		var msigWallet = new SafeMultisigWallet(SDK, msigAddress, msigCreds);

		var bankRootAddress = "0:aa403ff60abf0bf79dd19c0ccefbeb324534cf8e98d395fddd2bc7ec89ed881b";

		var bankStorageAddress = new BankRoot(SDK, bankRootAddress).storageOf(new Address(msigAddress),
		                                                                      StorageOwnerType.CONSUMER_STORAGE.value()).get().value0().makeAddrStd();

		var valueWithdrawedSeparately = CurrencyUnit.VALUE(EVER,"0.1");

		var payload = new BankStorage(SDK, bankStorageAddress).withdrawValue(valueWithdrawedSeparately).toPayload();

		msigWallet.sendTransaction(
						new Address(bankStorageAddress),
						CurrencyUnit.VALUE(EVER,"0.1"),
						true,
						MessageFlag.EXACT_VALUE_GAS.flag(),
						payload)
				.callTree(true, BankRoot.DEFAULT_ABI(), BankStorage.DEFAULT_ABI(), SafeMultisigWallet.DEFAULT_ABI());

		var getValueBalance = new BankStorage(SDK, bankStorageAddress)._valueBalance().get()._valueBalance();
		System.out.println("My Storage Value Balance: " + getValueBalance);

	}

	@Test
	public void test_deposit_token() {

	}

	@Test
	public void test_withdraw_token() {

	}
}