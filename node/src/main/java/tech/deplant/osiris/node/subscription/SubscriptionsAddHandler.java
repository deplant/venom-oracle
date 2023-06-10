package tech.deplant.osiris.node.subscription;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.osiris.TaskType;
import tech.deplant.osiris.node.OracleNode;

public record SubscriptionsAddHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(SubscriptionsAddHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		final String taskAddress = req.path().param("task");
		final String type = req.path().param("type");
		try {
			logger.debug("Address:" + taskAddress);
			node().subscriptionManager().subscribe(taskAddress, TaskType.valueOf(type.toUpperCase()));
			res.send("Subscribed. Type: %s. Address: %s".formatted(type, taskAddress));
		} catch (final Exception ex) {
			logger.error(ex);
			res.send("Error! " + ex);
		}
	}
}