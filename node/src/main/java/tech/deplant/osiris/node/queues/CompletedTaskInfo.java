package tech.deplant.osiris.node.queues;

import tech.deplant.osiris.model.task.Task;
import tech.deplant.osiris.node.subscription.SubscriptionDetails;

public record CompletedTaskInfo(SubscriptionDetails details, Task task) {
}
