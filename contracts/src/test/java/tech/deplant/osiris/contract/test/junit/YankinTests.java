package tech.deplant.osiris.contract.test.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Net;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.contract.EverOSGiver;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.TIP3TokenRootTemplate;
import tech.deplant.java4ever.framework.template.TIP3TokenWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;

import static tech.deplant.osiris.contract.test.junit.TestContext.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class YankinTests {

	public static final System.Logger logger = System.getLogger(YankinTests.class.getName());

	@BeforeAll
	public static void getContext() throws IOException {
		INIT();
	}

	@Test
	public void get_signature_id() throws JsonProcessingException, EverSdkException {
		logger.log(System.Logger.Level.DEBUG,String.valueOf(Net.getSignatureId(SDK.context()).signatureId()));
		logger.log(System.Logger.Level.DEBUG,String.valueOf(Net.getSignatureId(SDK_VENOM_DEVNET.context()).signatureId()));
		//EverOSGiver.V2(SDK).give("0:856f54b9126755ce6ecb7c62b7ad8c94353f7797c03ab82eda63d11120ed3ab7", new BigInteger("55000000000")).call();
	}

	@Test
	public void deploy_dao_and_tip3_root() throws IOException, EverSdkException, InterruptedException {
		var msigCreds = new Credentials(
				"7253cc1f3187647207bf18253ce8ecd6951209065073b5e4d0eea7f8c97f811a",
				"f3583ed4d4a99db3586431d41f5c1478cc28d6f7ab556ac6144e7a78b299d2ba");

		var msigAddress = "0:6b02864de6aac2310fa41a0a7b94c497f61206fd7479cbced6bf47ef31aac761";
		var msigWallet = new SafeMultisigWallet(SDK_VENOM_DEVNET, msigAddress, msigCreds);
		System.out.println("Config msig: " + msigWallet);

		var TIP3_NONCE = new BigInteger("256");

		var deployStatement = new TIP3TokenRootTemplate().prepareDeploy(SDK_VENOM_DEVNET,
		                                                                msigWallet.credentials(),
		                                                                "CHIP3 Oracle Token",
		                                                                "CHIP3",
		                                                                9,
		                                                                new Address(msigAddress),
		                                                                TIP3TokenWalletTemplate.DEFAULT_TVC().codeCell(SDK_VENOM_DEVNET),
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
//		0:cda4cad94f6a29f924162d0d38fabafda27fa8d40b512cd5e9d5490ebe372669
	}

	@Test
	public void give_to_wallet_1() throws JsonProcessingException, EverSdkException {
		EverOSGiver.V2(SDK).give("0:856f54b9126755ce6ecb7c62b7ad8c94353f7797c03ab82eda63d11120ed3ab7", new BigInteger("55000000000")).call();
	}

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	public void compile_contract() throws JsonProcessingException, EverSdkException {
		LOCAL_CONFIG.compileTemplate("validator/TaskSubscription.tsol", "TaskSubscription");
	}


}
