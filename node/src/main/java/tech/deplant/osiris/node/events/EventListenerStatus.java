package tech.deplant.osiris.node.events;

public record EventListenerStatus(boolean isEnabled) {
	public static EventListenerStatus of(EventListener eventListener) {
		return new EventListenerStatus(eventListener.isEnabled());
	}
}
