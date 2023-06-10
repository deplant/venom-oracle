package tech.deplant.osiris.node.tst.integration.everscale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventListenerTests {

	private static final Logger log = LogManager.getLogger(EventListenerTests.class);

//
//	@Test
//	public void testLocalConnection() throws IOException, EverSdkException {
//
//		Sdk sdkSE = Sdk.builder()
//		               .networkEndpoints("http://80.78.241.3")
//		               .build(JavaLibraryPathLoader.TON_CLIENT);
//		Msig msig;
//
//		msig = new Msig(sdkSE,
//		                "0:66262b0816376d78bd58e3ba94159955c8229c1d23694f147fc4e54b740d9f5c",
//		                Credentials.NONE,
//		                MsigTemplate.SETCODE_MULTISIG_ABI());
//		log.debug(msig.account().balance());
//	}
//
//	@Test
//	public void testDevnetConnection() throws IOException {
//
//		Sdk sdkDev = Sdk.builder()
//		                .networkEndpoints(System.getenv("DEVNET_ENDPOINT"))
//		                .build(JavaLibraryPathLoader.TON_CLIENT);
//		Msig msig;
//
//		try {
//			msig = new Msig(sdkDev,
//			                "0:66262b0816376d78bd58e3ba94159955c8229c1d23694f147fc4e54b740d9f5c",
//			                Credentials.NONE,
//			                MsigTemplate.SETCODE_MULTISIG_ABI());
//			log.debug(msig.account().balance());
//		} catch (JsonProcessingException | EverSdkException e) {
//			throw new RuntimeException(e);
//		}
//	}
}
