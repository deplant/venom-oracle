package tech.deplant.osiris.node;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record OracleNodeStartHandler(OracleNode node)  implements Handler {

	private static final Logger logger = LogManager.getLogger(OracleNodeStartHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		try {
			node().start();
			res.send("Subscription updates started.");
		} catch (final Exception ex) {
			logger.error(ex);
		}
	}
}

