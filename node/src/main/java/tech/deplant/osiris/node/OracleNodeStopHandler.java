package tech.deplant.osiris.node;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record OracleNodeStopHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(OracleNodeStopHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		try {
			node().abort();
			res.send("Subscription updates stopped.");
		} catch (final Exception ex) {
			logger.error(ex);
		}
	}
}
