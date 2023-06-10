package tech.deplant.osiris.node.subscription;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.osiris.node.OracleNode;

public record SubscriptionsListHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(SubscriptionsListHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		try {
			//final String str = String.join("\n", node().subscriptionManager().subbedAddresses());
			res.send(node()
					         .mapper()
					         .writerWithDefaultPrettyPrinter()
					         .writeValueAsString(SubscriptionManagerStatus.of(node().subscriptionManager())));
		} catch (final Exception ex) {
			logger.error(ex);
		}
	}
}