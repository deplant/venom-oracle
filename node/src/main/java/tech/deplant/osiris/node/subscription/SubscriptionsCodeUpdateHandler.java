package tech.deplant.osiris.node.subscription;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import tech.deplant.osiris.node.OracleNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record SubscriptionsCodeUpdateHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(SubscriptionsCodeUpdateHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		try {
			if (node().subscriptionManager().isEnabled()) {
				node().subscriptionManager().abort();

			}
			node().subscriptionManager().updateSubscriptionCode();
			res.send("Code for task subscriptions updated! Task monitoring stopped.");
		} catch (final Exception ex) {
			logger.error(ex);
		}
	}
}