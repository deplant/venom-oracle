package tech.deplant.osiris.node.consensus;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.osiris.Election;
import tech.deplant.osiris.Elector;
import tech.deplant.osiris.node.ListenerControls;
import tech.deplant.osiris.node.OracleNode;
import tech.deplant.osiris.node.queues.CompletedTaskInfo;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsensusParticipator extends ListenerControls {

	private static final Logger log = LogManager.getLogger(ConsensusParticipator.class);

	private final OracleNode node;
	private final Long consensusTimeoutMillis;

	public ConsensusParticipator(OracleNode node, Long consensusTimeoutMillis) {
		this.node = node;
		this.consensusTimeoutMillis = consensusTimeoutMillis;
	}

	public OracleNode node() {
		return this.node;
	}

	@Override
	protected void mainLoop() {
		while (node().completedTasksQueue().hasNext()) {
			try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
				mainLoopIterationWrapper(executor);
			}
		}
		log.trace("Completed tasks queue is empty.");
	}

	@Override
	protected void mainLoopIteration(Object obj) throws ExecutionException, InterruptedException, JsonProcessingException {

		if (obj instanceof ExecutorService e) {
			final CompletedTaskInfo taskInfo = node().completedTasksQueue().poll();
			var future = e.submit(() -> vote(taskInfo));
		}
	}

	public void vote(CompletedTaskInfo taskInfo) {
		try {
			Election election;
			election = new Election(node().oracleValidator(),
			                        Elector.ofType(taskInfo.details().taskType(), node().oracleValidator().sdk(),
			                                       taskInfo.details().taskId()),
			                        taskInfo.task().response());
			if (election.vote()) {
				node().subscriptionManager().requestDone(taskInfo.details().subscriptionId(), Instant.now().getEpochSecond());
			} else {
				log.error("Failed consensus! Address: %s, Response: %s".formatted(taskInfo.details().taskId(),
				                                                                  taskInfo.task().response()));
			}
		} catch (EverSdkException | JsonProcessingException e) {
			log.error(e);
		}
	}
}