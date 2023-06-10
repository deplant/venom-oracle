package tech.deplant.osiris.node.tst.integration.requests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestEventListenerTests {
	private static final Logger log = LogManager.getLogger(RequestEventListenerTests.class);

//    /**
//     * Test that goes through whole execution flow with different variants and outcomes.
//     * Mocks almost everything, but should be precise on overall graph and choices.
//     */
//    @Test
//    @Disabled
//    public void true_on_check_new_mocked_requests() {
//        var requestsListener = new RequestEventListener(new SdkBuilder().create(JavaLibraryPathLoader.TON_CLIENT),
//                Address.ZERO,
//                Instant.now());
//        requestsListener.pingEvents();
//
//        assertTrue(requestsListener.hasNewRequest());
//
//        log.debug(requestsListener.getNextRequest().requester().makeAddrStd());
//        log.debug(requestsListener.getNextRequest().taskTemplateId().makeAddrStd());
//        log.debug(requestsListener.getNextRequest().oracleId().makeAddrStd());
//        log.debug(requestsListener.getNextRequest().requestTimestamp().toString());
//        log.debug(requestsListener.getNextRequest().requestId().toString(10));
//
//    }
}
