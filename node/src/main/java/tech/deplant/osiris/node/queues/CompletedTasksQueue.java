package tech.deplant.osiris.node.queues;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CompletedTasksQueue  {

	private final Queue<CompletedTaskInfo> queue;

	// RequestsProcessor
	public CompletedTasksQueue() {
		this.queue = new ConcurrentLinkedDeque<>();
	}

	public void addTask(CompletedTaskInfo info) {
		this.queue.add(info);
	}

	public CompletedTaskInfo[] tasks() {
		return this.queue.toArray(CompletedTaskInfo[]::new);
	}

	public boolean hasNext() {
		return this.queue.size() > 0;
	}

	public CompletedTaskInfo poll() {
		return this.queue.poll();
	}

	public int size() {
		return this.queue.size();
	}

}
