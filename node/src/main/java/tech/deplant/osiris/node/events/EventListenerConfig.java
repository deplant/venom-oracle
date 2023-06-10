package tech.deplant.osiris.node.events;

import io.helidon.config.Config;

public record EventListenerConfig(Long requestCheckInterval,
                                  String subscriptionCodeHash) {
	public static EventListenerConfig of(final Config config) {
		return new EventListenerConfig(config.get("request-check-interval").asLong().orElse(60000L),
		                               config.get("subscription-code-hash").asString().orElse("")
		);
	}
}
