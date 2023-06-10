package tech.deplant.osiris.node.tasks;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import tech.deplant.osiris.node.OracleNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.osiris.node.subscription.SubscriptionsStartHandler;

public record TasksListHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(SubscriptionsStartHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		try {
			var semaphore = node().taskProcessor().taskContext().maxTaskCountSemaphore();
			var waiting = semaphore.getQueueLength();
			var slotsLeft = semaphore.availablePermits();
			var builder = new StringBuilder();
			builder.append("Requests Queue size: ")
			       .append(node().requestQueue().size())
			       .append("\nTask slots left: " + slotsLeft)
			       .append("\nTask waiting: " + waiting);
			res.send(builder.toString());
		} catch (final Exception ex) {
			logger.error(ex);
		}
	}
}
