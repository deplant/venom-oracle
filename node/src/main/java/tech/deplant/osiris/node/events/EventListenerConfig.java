package tech.deplant.osiris.node.events;

import io.helidon.config.Config;

public record EventListenerConfig(Long requestCheckInterval,
                                  String subscriptionCodeHash) {
	public static EventListenerConfig of(final Config config) {
		return new EventListenerConfig(config.get("subscription-code-hash").asLong().orElse(60000L),
		                              config.get("request-check-interval").asString().orElse("")
		);
	}
}
