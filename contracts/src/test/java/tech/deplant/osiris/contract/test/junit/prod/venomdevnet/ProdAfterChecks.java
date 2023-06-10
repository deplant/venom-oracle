package tech.deplant.osiris.contract.test.junit.prod.venomdevnet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.osiris.contract.BankRoot;
import tech.deplant.osiris.contract.BankStorage;
import tech.deplant.osiris.contract.Factory;
import tech.deplant.osiris.contract.TestFaucet;

import java.io.IOException;

import static tech.deplant.osiris.contract.test.junit.TestContext.*;

public class ProdAfterChecks {


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

	@Test
	public void test_token_wallet_on_storage() throws JsonProcessingException, EverSdkException {
		var storage = new BankStorage(SDK, "0:789c77709ae3b5ba4ecb3c105fc3899d3d0d72901476e7c48d7be5eb4d0bc284");
		System.out.println(storage._tokenWallet().get()._tokenWallet().makeAddrStd());
		System.out.println(storage._token().get()._token().makeAddrStd());
		var bank = new BankRoot(SDK, "0:235949b2994c930e5eedf162d8f55bdc50ee5e52b5a3f30a668aec2c6ddd87b5");
		System.out.println(bank._token().get()._token().makeAddrStd());
		var factory = new Factory(SDK, "0:70c0d898e6418b003f152cf14fb9b10137b35d2fa37566aeba9fb9adb9450f24");
		System.out.println(factory._token().get()._token().makeAddrStd());
		var faucet = new TestFaucet(SDK, "0:9cae4d2963605798616a325f2d8a19603acc1d98990be664a8a39a19798f0f5e");
		System.out.println(faucet._token().get()._token().makeAddrStd());
	}

}
