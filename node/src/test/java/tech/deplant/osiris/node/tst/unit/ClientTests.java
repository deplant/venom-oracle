package tech.deplant.osiris.node.tst.unit;

import io.helidon.common.http.Http;
import io.helidon.nima.webclient.http1.Http1Client;
import io.helidon.nima.webclient.http1.Http1ClientResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class ClientTests {

	private static final Logger log = LogManager.getLogger(ClientTests.class);

	@Test
	public void test_endpoint() {
		String endpoint = "http://185.20.226.96/graphql";
		try (final Http1ClientResponse response = Http1Client.builder()
		                                                     .build().method(Http.Method.POST)
		                                                     .header(Http.Header.ACCEPT, "application/json")
		                                                     .uri(endpoint)
		                                                     .submit("{}")) {

			log.info(response.status());
			log.info(response.entity().as(String.class));
		}
	}
}
