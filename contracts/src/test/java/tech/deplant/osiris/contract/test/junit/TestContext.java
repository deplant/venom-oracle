package tech.deplant.osiris.contract.test.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.CurrencyUnit;
import tech.deplant.java4ever.framework.LocalConfig;
import tech.deplant.java4ever.framework.OnchainConfig;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.Solc;
import tech.deplant.java4ever.framework.artifact.TvmLinker;
import tech.deplant.java4ever.framework.contract.*;
import tech.deplant.osiris.contract.Factory;
import tech.deplant.osiris.contract.OracleValidator;
import tech.deplant.osiris.contract.test.template.EverUsdMedianizedTemplate;
import tech.deplant.osiris.contract.test.template.StoredTaskTemplate;

import java.io.IOException;
import java.math.BigInteger;

import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.EVER;
import static tech.deplant.java4ever.framework.CurrencyUnit.Ever.MILLIEVER;
import static tech.deplant.java4ever.framework.LogUtils.warn;

public class TestContext {

	public static final System.Logger logger = System.getLogger(TestContext.class.getName());

	public static final String EXPLORER_VENOM_DEVNET_PATH = System.getProperty("user.home") + "/.deplant/explorer-venom-devnet.json";
	public static final String EXPLORER_LOCAL_NODE_PATH = System.getProperty("user.home") + "/.deplant/explorer-se.json";
	public static final String LOCAL_CONFIG_PATH = System.getProperty("user.home") + "/.deplant/local.json";
	public final static StoredTaskTemplate[] TEMPLATES = {new EverUsdMedianizedTemplate()};
	public final static String CUSTOM_TASK_STATE_1 = """
			{
				"status": "3",
				"failureReason": "0",
				"response": "te6ccgEBAQEAAgAAAA==",
				"commitStartTimestamp": "1667749619",
				"revealStartTimestamp": "1667749636",
				"responseTimestamp": "0",
				"participants": {
					"0:0e4a0f4c457dab5f2ab63f253c33af02b48d8d4a0616da6d261bfcc6f6f0131f": {
						"validator": "0:0e4a0f4c457dab5f2ab63f253c33af02b48d8d4a0616da6d261bfcc6f6f0131f",
						"enterStake": "0",
						"commitHash": "0xaf75f081c8d90d5949a0ef7c2c9d169da7f9ee50a48b7c73893ab69aa2009930",
						"revealResponse": "te6ccgEBAQEAEgAAIAAAAAAAAAAAAAAAAAAAAHs=",
						"bountyValue": "0",
						"slashValue": "0"
					},
					"0:7215f0e071975d65497e2129fb65cfb4edcf675ce3a22c2c95614309f3b64277": {
						"validator": "0:7215f0e071975d65497e2129fb65cfb4edcf675ce3a22c2c95614309f3b64277",
						"enterStake": "0",
						"commitHash": "0xefcd88a7515c30fe5f58322c191fff213ffe7ecd289aed9053f85a796fe9ada7",
						"revealResponse": "te6ccgEBAQEAEgAAIAAAAAAAAAAAAAAAAAAAAHs=",
						"bountyValue": "0",
						"slashValue": "0"
					},
					"0:a168c96b15c8a76561147262dab8cf5d488ace166b14b050b3e3813fda109e79": {
						"validator": "0:a168c96b15c8a76561147262dab8cf5d488ace166b14b050b3e3813fda109e79",
						"enterStake": "0",
						"commitHash": "0x637baa889f39b0da656ccee7b31567598afae059d75d0c8376d59f468cd63663",
						"revealResponse": "te6ccgEBAQEAEgAAIAAAAAAAAAAAAAAAAAAAAHs=",
						"bountyValue": "0",
						"slashValue": "0"
					},
					"0:c4c2e596cf04e79e7e46c749947f375ef4e407ad7e35dc087d16773d9910f61b": {
						"validator": "0:c4c2e596cf04e79e7e46c749947f375ef4e407ad7e35dc087d16773d9910f61b",
						"enterStake": "0",
						"commitHash": "0xc002c012ac6c8d353c424e67fc5f0588d0a5bbbb7e9b283c0d793faa2b7f4f90",
						"revealResponse": "te6ccgEBAQEAEgAAIAAAAAAAAAAAAAAAAAAAAHs=",
						"bountyValue": "0",
						"slashValue": "0"
					},
					"0:eb692749bf78d57e96dfa66dcd5c7f49b1afaeaec53084044afc3c733fa5165c": {
						"validator": "0:eb692749bf78d57e96dfa66dcd5c7f49b1afaeaec53084044afc3c733fa5165c",
						"enterStake": "0",
						"commitHash": "0x8416be047c79787c1b560418f5f492017b48448d85d93abcd409fe111cf61a89",
						"revealResponse": "te6ccgEBAQEAEgAAIAAAAAAAAAAAAAAAAAAAAHs=",
						"bountyValue": "10000000000",
						"slashValue": "0"
					}
				},
				"commitedCount": "5",
				"revealedCount": "5",
				"acceptedCount": "0"
			}
			""";
	public static OnchainConfig CONFIG;
	public static TvmLinker LINKER;
	public static Solc SOLC;
	public static LocalConfig LOCAL_CONFIG;

	public static Sdk SDK_VENOM_DEVNET;
	public static Sdk SDK_LOCAL_NODE;
	public static Sdk SDK;


	public static Factory FACTORY;

	public static TIP3TokenRoot TOKEN_ROOT;

	public static MultisigWallet DAO_WALLET;
	public static OracleValidator[] VALIDATOR = new OracleValidator[5];
	public static MultisigWallet[] MSIG = new MultisigWallet[3];

	public static Giver GIVER_LOCAL;

	public static Giver GIVER;

	//public static CustomTaskElector CUSTOM_TASK_EXAMPLE_01;

	public static BigInteger EVER_ZERO = CurrencyUnit.VALUE(EVER, "0");
	public static BigInteger MILLI_100 = CurrencyUnit.VALUE(MILLIEVER, "100");
	public static BigInteger EVER_ONE = CurrencyUnit.VALUE(EVER, "1");
	public static BigInteger EVER_TWO = CurrencyUnit.VALUE(EVER, "2");
	public static BigInteger EVER_FIVE = CurrencyUnit.VALUE(EVER, "5");
	public static BigInteger EVER_TEN = CurrencyUnit.VALUE(EVER, "10");

	public static BigInteger EVER_20 = CurrencyUnit.VALUE(EVER, "10");

	public static void INIT() throws IOException {

		OnchainConfig explorerLocalNodeConf = OnchainConfig.LOAD_IF_EXISTS(EXPLORER_LOCAL_NODE_PATH);

		OnchainConfig explorerVenomDevNetConf = OnchainConfig.LOAD_IF_EXISTS(EXPLORER_VENOM_DEVNET_PATH);

		LocalConfig environmentConfig = null;
		try {
			environmentConfig = LocalConfig.LOAD(LOCAL_CONFIG_PATH);
		} catch (Exception e) {
			warn(logger, e.getMessage());
			environmentConfig = LocalConfig.EMPTY(LOCAL_CONFIG_PATH,
			                                            "C:/everscale/TON-Solidity-Compiler/build/solc/Release/solc",
			                                            "C:/everscale/TVM-linker/target/release/tvm_linker",
			                                            "C:/everscale/TON-Solidity-Compiler/lib/stdlib_sol.tvm",
			                                            "C:/idea_git/iris-contracts-core/src/main/solidity",
			                                            "C:/idea_git/iris-contracts-core/src/main/resources/artifacts");

		}
		SDK_LOCAL_NODE = Sdk.builder()
		         .explorerConfig(explorerLocalNodeConf)
		         .environmentConfig(environmentConfig)
		         .networkEndpoints(System.getenv("LOCAL_NODE_ENDPOINT"))
		         .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));


		SDK_VENOM_DEVNET = Sdk.builder()
		                      .explorerConfig(explorerVenomDevNetConf)
		                      .environmentConfig(environmentConfig)
		                      .networkEndpoints("https://gql-devnet.venom.network/graphql")
		                      .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));

		GIVER_LOCAL = EverOSGiver.V2(SDK_LOCAL_NODE);

		SDK = SDK_LOCAL_NODE;
		GIVER = GIVER_LOCAL;

		REFRESH();

	}

	/**
	 * Reloads Java classes of contracts from ExplorerConfig.
	 * Should be rerun after deployments and other addresses & abi changes.
	 */
	public static void REFRESH() throws JsonProcessingException {
		CONFIG = SDK.onchainConfig();
		LOCAL_CONFIG = SDK.localConfig();
		SOLC = LOCAL_CONFIG.info().compiler();
		LINKER = LOCAL_CONFIG.info().linker();

		// Loading task factory
		try {
			FACTORY = CONFIG.contract(Factory.class,
							SDK,
							"taskFactory",
							"taskFactoryKeys"

					);
			logger.log(System.Logger.Level.TRACE, "Task factory:" + FACTORY.address());
		} catch (NullPointerException e) {
			logger.log(System.Logger.Level.WARNING, "Task factory not found in config");
		}

		// Loading DAO Wallet
		try {
			DAO_WALLET = CONFIG.contract(SafeMultisigWallet.class,
			                          SDK,
			                          "daoWallet",
			                          "daoWalletKeys");

			logger.log(System.Logger.Level.TRACE, "DAO Wallet:" + DAO_WALLET.address());
		} catch (NullPointerException e) {
			logger.log(System.Logger.Level.WARNING, "DAO Wallet not found in config");
		}

		// Loading Token Root
		try {
			TOKEN_ROOT = CONFIG.contract(TIP3TokenRoot.class,
			                          SDK,
			                          "tokenRoot",
			                          "tokenRootKeys"

			);
			logger.log(System.Logger.Level.TRACE, "Token root:" + TOKEN_ROOT.address());
		} catch (NullPointerException e) {
			logger.log(System.Logger.Level.WARNING, "Token root not found in config");
		}

		// Loading validators
		for (int i = 0; i < VALIDATOR.length; i++) {
			try {
				VALIDATOR[i] = CONFIG.contract(OracleValidator.class,SDK, "validator" + i, "validator" + i + "Keys");
				logger.log(System.Logger.Level.TRACE, "Validator " + i + ":" + VALIDATOR[i].address());
			} catch (NullPointerException e) {
				logger.log(System.Logger.Level.WARNING, "Validator " + i + " not found in config");
			}
		}

		// Loading Multisig wallets
		for (int i = 0; i < MSIG.length; i++) {
			try {
				MSIG[i] = CONFIG.contract(SafeMultisigWallet.class, SDK, "msig" + i, "msig" + i + "Keys");
			} catch (NullPointerException e) {
				logger.log(System.Logger.Level.WARNING, "Multisig wallet " + i + " not found in config");
			}
		}
	}
}
