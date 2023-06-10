package tech.deplant.osiris.node.queues;

import tech.deplant.osiris.model.task.Task;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CompletedTasksQueue  {

	private final Queue<Task> queue;

	// RequestsProcessor
	public CompletedTasksQueue() {
		this.queue = new ConcurrentLinkedDeque<>();
	}

	public void addTask(Task req) {
		this.queue.add(req);
	}

	public Task[] tasks() {
		return this.queue.toArray(Task[]::new);
	}

	public boolean hasNext() {
		return this.queue.size() > 0;
	}

	public Task poll() {
		return this.queue.poll();
	}

	public int size() {
		return this.queue.size();
	}

}
