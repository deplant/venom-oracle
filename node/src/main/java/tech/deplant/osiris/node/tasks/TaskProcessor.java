package tech.deplant.osiris.node.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.osiris.model.exception.ActionProcessingException;
import tech.deplant.osiris.model.request.OracleRequest;
import tech.deplant.osiris.model.task.TaskContext;
import tech.deplant.osiris.node.ListenerControls;
import tech.deplant.osiris.node.OracleNode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class TaskProcessor extends ListenerControls {

	private static final Logger log = LogManager.getLogger(TaskProcessor.class);
	private final OracleNode node;

	private final TaskContext taskContext;

	// RequestsProcessor
	public TaskProcessor(OracleNode node, TaskContext taskContext) {
		this.node = node;
		this.taskContext = taskContext;
	}

	public OracleNode node() {
		return this.node;
	}

	public TaskContext taskContext() {
		return this.taskContext;
	}

	@Override
	protected void mainLoop() {
		while (node().requestQueue().hasNext()) {
			mainLoopIterationWrapper();
		}
		log.info("Requests queue is empty.");
	}

	@Override
	protected void mainLoopIteration() throws ExecutionException, InterruptedException {
		try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			final OracleRequest req = node().requestQueue().poll();

			////TODO If we don't have enough permits in semaphore, we'll lost request ??
			log.info(String.format(
					"Request found on task: %s. Requester address: %s. Starting request processing...",
					req.taskAddress(),
					req.requester()));
			final long newRequestTx = req.requestTimestamp();
			// Get subscription object for further use
			final var subscription = node()
					.subscriptionManager()
					.subscriptionsMap()
					.get(req.taskAddress());
			// Get last request time
			final long doneRequestTx = subscription.lastProcessedRequestTx();

			// Only if new request is newer than saved...
			if (newRequestTx > doneRequestTx) {
				var futureTask = executor.submit(() -> {
					// get task object for further use
					var task = taskContext()
					                 .spawnTask(subscription.template());
					try {
						task.run(req);
						return task;
					} catch (ActionProcessingException e) {
						throw new RuntimeException(e);
					}
				});
				node().completedTasksQueue().addTask(futureTask.get());

			} else {
				log.info("Request already processed.");
			}
		}
	}

}
