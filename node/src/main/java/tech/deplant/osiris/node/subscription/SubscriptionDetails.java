package tech.deplant.osiris.node.subscription;

import tech.deplant.osiris.model.template.TaskTemplate;

public record SubscriptionDetails(String taskId,
                                  String subscriptionId,
                                  long lastProcessedRequestTx,
                                  TaskTemplate template) {
}
