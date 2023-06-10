package tech.deplant.osiris.node.subscription;

import java.util.Set;

public record SubscriptionManagerStatus(boolean isEnabled, Set<String> tasks, Set<String> subscriptionIds, Set<String> badSubs) {
	public static SubscriptionManagerStatus of(SubscriptionManager subscriptionManager) {
		return new SubscriptionManagerStatus(subscriptionManager.isEnabled(), subscriptionManager.subbedTasks(), subscriptionManager.subscriptions(), subscriptionManager.badSubscriptions());
	}
}
