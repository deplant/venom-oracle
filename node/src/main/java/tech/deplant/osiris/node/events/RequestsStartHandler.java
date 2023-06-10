package tech.deplant.osiris.node.events;

import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import tech.deplant.osiris.node.OracleNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record RequestsStartHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(RequestsStartHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		if (node().eventListener().isEnabled()) {
			res.send("Requests listening is already started.");
//		} else if (Address.ZERO.makeAddrStd().equals(node().oracleValidator().address())) {
//			res.send("Validator address is empty, please add it in properties " +
//			         "as \"osiris.validator-address=0:youraddress\". " +
//			         "If you didn't deploy validator contract before, use /deploy API request with your keys as parameters.");
//		} else if (listener().subscriptions().size() < 1) {
//			res.send(
//					"No valid tasks subscriptions found! Run /tasks/update if tasks were subscribed asynchronously " +
//					"or subscribe to new tasks with /tasks/subscribe/{task_address}");
		} else {
			node().eventListener().start();
			res.send(
					"VRequests listening started. Requests are checked at each " +
					node().eventListener().scheduleDelayMillis().toString() + " seconds rate.");
		}
	}
}