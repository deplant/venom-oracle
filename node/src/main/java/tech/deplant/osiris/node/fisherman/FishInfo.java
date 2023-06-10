package tech.deplant.osiris.node.fisherman;

import tech.deplant.osiris.node.subscription.SubscriptionDetails;

public record FishInfo(SubscriptionDetails subscriptionDetails, long previousTime, long timeDeviation) {

	public FishInfo withUpdatedPreviousTime(long updatedPreviousTime) {
		return new FishInfo(subscriptionDetails(),updatedPreviousTime,timeDeviation());
	}
}
