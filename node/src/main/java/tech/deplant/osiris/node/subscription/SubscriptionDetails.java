package tech.deplant.osiris.node.subscription;

import tech.deplant.osiris.TaskType;
import tech.deplant.osiris.model.template.TaskTemplate;
import tech.deplant.osiris.model.trigger.TriggerType;

public record SubscriptionDetails(String taskId,
                                  String subscriptionId,

								  TaskType taskType,
                                  long lastProcessedRequestTx,
                                  TaskTemplate template) {

	public TriggerType triggerType() {
		return switch(taskType()) {
			case MEDIAN_IMMEDIATE, PRECISE_IMMEDIATE -> TriggerType.IMMEDIATE;
			case MEDIAN_FEED, PRECISE_FEED -> TriggerType.VALUE_FEED;
		};
	}

	public SubscriptionDetails withLastProcessedRequestTx(long lastProcessedRequestTx) {
		return new SubscriptionDetails(taskId(),subscriptionId(),taskType(),lastProcessedRequestTx(),template());
	}
}
