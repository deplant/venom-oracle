package tech.deplant.osiris.node.tasks;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import tech.deplant.osiris.node.OracleNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.osiris.node.subscription.SubscriptionsStartHandler;

public record TasksStartHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(SubscriptionsStartHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		try {
			node().taskProcessor().start();
			node().consensusParticipator().start();
			res.send("Task processing started...");
		} catch (final Exception ex) {
			logger.error(ex);
		}
	}
}

