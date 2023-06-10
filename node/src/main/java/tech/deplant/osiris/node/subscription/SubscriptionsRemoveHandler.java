package tech.deplant.osiris.node.subscription;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import tech.deplant.osiris.node.OracleNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record SubscriptionsRemoveHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(SubscriptionsRemoveHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		final String taskAddress = req.path().param("task");
		try {
			logger.debug(taskAddress);
			node().subscriptionManager().unsubscribe(taskAddress);
			res.send("Unsibscribed!");
		} catch (final Exception ex) {
			logger.error(ex);
		}
	}
}