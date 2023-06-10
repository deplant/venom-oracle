package tech.deplant.osiris.node;

import io.helidon.config.Config;
import tech.deplant.osiris.node.events.EventListenerConfig;
import tech.deplant.osiris.node.subscription.SubscriptionConfig;
import tech.deplant.osiris.node.tasks.TaskProcessorConfig;
import tech.deplant.osiris.node.validator.ValidatorConfig;

public record OracleNodeConfig(ValidatorConfig validatorConfig,
                               EventListenerConfig eventListenerConfig,
                               SubscriptionConfig subscriptionConfig,
                               TaskProcessorConfig taskConfig) {

	public static OracleNodeConfig of(final Config config) {
		return new OracleNodeConfig(ValidatorConfig.of(config.get("validator")),
		                            EventListenerConfig.of(config.get("listener")),
		                            SubscriptionConfig.of(config.get("subscription")),
		                            TaskProcessorConfig.of(config.get("task-processor")));
	}
}
