package tech.deplant.osiris.node.consensus;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.osiris.model.task.Task;
import tech.deplant.osiris.node.ListenerControls;
import tech.deplant.osiris.node.OracleNode;

import java.util.concurrent.ExecutionException;
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
			mainLoopIterationWrapper();
		}
		log.info("Completed tasks queue is empty.");
	}

	@Override
	protected void mainLoopIteration() throws ExecutionException, InterruptedException, JsonProcessingException {
		try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			final Task task = node().completedTasksQueue().poll();
			task.sendResponse(node().oracleValidator());
		} catch (EverSdkException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
}