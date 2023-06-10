package tech.deplant.osiris.node.events;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import tech.deplant.osiris.node.OracleNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record RequestsStopHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(RequestsStopHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		if (node().eventListener().isEnabled()) {
			node().eventListener().abort();
			res.send("Requests listening stopped.");
		} else {
			res.send("Requests listening is already stopped.");
		}
	}
}