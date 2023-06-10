package tech.deplant.osiris.node.queues;

import io.helidon.scheduling.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScheduledTasksMap {

		private final Map<String, Task> map;

		// RequestsProcessor
		public ScheduledTasksMap() {
			this.map = new ConcurrentHashMap<>();
		}

		public void addScheduledTask(String taskId, Task scheduledTask) {
			this.map.put(taskId, scheduledTask);
		}

		public Task[] tasks() {
			return this.map.values().toArray(Task[]::new);
		}

		public boolean hasNext() {
			return this.map.size() > 0;
		}

		public Task get(String taskId) {
			return this.map.get(taskId);
		}

		public int size() {
			return this.map.size();
		}

}

