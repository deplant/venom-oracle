package tech.deplant.osiris.node;

import tech.deplant.osiris.node.consensus.ConsensusParticipatorStatus;
import tech.deplant.osiris.node.events.EventListenerStatus;
import tech.deplant.osiris.node.subscription.SubscriptionManagerStatus;
import tech.deplant.osiris.node.tasks.TaskProcessorStatus;
import tech.deplant.osiris.node.validator.ValidatorStatus;

public record OracleNodeStatus(Integer subscriptionsCount,
                               Integer requestQueueCount,
                               Integer completedTasksCount,
                               ValidatorStatus validator,
                               SubscriptionManagerStatus subscriptionManager,
                               TaskProcessorStatus taskProcessor,

                               EventListenerStatus eventListener,
                               ConsensusParticipatorStatus consensusParticipator) {
	public static OracleNodeStatus of(OracleNode node) {
		return new OracleNodeStatus(node.subscriptionManager().subbedTasks().size(),
									node.requestQueue().size(),
									node.completedTasksQueue().size(),
		                            ValidatorStatus.of(node.oracleValidator()),
		                            SubscriptionManagerStatus.of(node.subscriptionManager()),
		                            TaskProcessorStatus.of(node.taskProcessor()),
		                            EventListenerStatus.of(node.eventListener()),
		                            ConsensusParticipatorStatus.of(node.consensusParticipator()));
	}
}
