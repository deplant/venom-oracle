package tech.deplant.osiris.node.tasks;

public record TaskProcessorStatus(boolean isEnabled) {
	public static TaskProcessorStatus of(TaskProcessor taskProcessor) {
		return new TaskProcessorStatus(taskProcessor.isEnabled());
	}
}
