package tech.deplant.osiris.node.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import tech.deplant.osiris.node.OracleNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record RequestsListHandler(OracleNode node) implements Handler {

	private static final Logger logger = LogManager.getLogger(RequestsListHandler.class);

	@Override
	public void accept(final ServerRequest req, final ServerResponse res) {
		try {
			var json = node().mapper().writeValueAsString(node().requestQueue().requests());
			res.send(json);
		} catch (JsonProcessingException e) {
			res.send(e.getMessage());
		}
//			String s = null;
//			if (node().eventListener().isEnabled()) {
//				s = "Validation is active. Requests check interval: " +
//				    node().eventListener().scheduleDelayMillis();
//			} else {
//				s = "Validation is stopped.";
//			}
//			res.send("Validator: " + node().oracleValidator().address() + "\n" + s + "\n"
//			);
	}
}