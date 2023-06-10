package tech.deplant.osiris.node.subscription;

import io.helidon.config.Config;

public record SubscriptionConfig(Long autoSubscriptionCheckInterval,
                                 String feedMedianizedCodeHash,
                                 String feedPreciseCodeHash,
                                 String requestMedianizedCodeHash,
                                 String requestPreciseCodeHash,
                                 String scheduledMedianizedCodeHash,
                                 String scheduledPreciseCodeHash) {
	public static SubscriptionConfig of(final Config config) {
		return new SubscriptionConfig(config.get("auto-sub-check-interval").asLong().orElse(60000L),
		                               config.get("feed-medianized-code-hash").asString().orElse(""),
		                               config.get("feed-precise-code-hash").asString().orElse(""),
		                               config.get("request-medianized-code-hash").asString().orElse(""),
		                               config.get("request-precise-code-hash").asString().orElse(""),
		                               config.get("scheduled-medianized-code-hash").asString().orElse(""),
		                               config.get("scheduled-precise-code-hash").asString().orElse("")
		);
	}
}
