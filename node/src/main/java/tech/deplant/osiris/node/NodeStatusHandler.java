package tech.deplant.osiris.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record NodeStatusHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(NodeStatusHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		try {
			res.send(node()
					         .mapper()
					         .writerWithDefaultPrettyPrinter()
					         .writeValueAsString(OracleNodeStatus.of(node())));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}