package tech.deplant.osiris.node.tasks;

import io.helidon.config.Config;

public record TaskProcessorConfig(Integer maxTaskThreads,
                                  Integer maxHttpRequests,
                                  Integer maxNetworkQueries,
                                  Integer maxActionThreadsPerTask,
                                  Long taskTimeout) {
	public static TaskProcessorConfig of(final Config config) {
		return new TaskProcessorConfig(config.get("max-task-threads").asInt().orElse(999),
		                           config.get("max-http-requests").asInt().orElse(100),
		                           config.get("max-gql-queries").asInt().orElse(1000),

		                           config.get("max-action-threads").asInt().orElse(449),
		                           config.get("task-timeout").asLong().orElse(60000L));
	}
}
