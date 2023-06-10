package tech.deplant.osiris.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.helidon.scheduling.Scheduling;
import io.helidon.scheduling.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.java4ever.utils.Objs;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Parent class for EventListener, SubscriptionManager and TaskProcessor. Implements all needed methods
 * to work with scheduled execution.
 */
public abstract class ListenerControls {

	private static final Logger log = LogManager.getLogger(ListenerControls.class);

	private Task schedulerTask;
	private Long scheduleDelayMillis;

	private Long shutdownTimeoutMillis;

	protected ListenerControls() {
		this(5000L, 10000L);
	}

	protected ListenerControls(Long scheduleDelayMillis, Long shutdownTimeoutMillis) {
		this.scheduleDelayMillis = scheduleDelayMillis;
		this.shutdownTimeoutMillis = shutdownTimeoutMillis;
	}

	protected void setScheduleDelayMillis(Long scheduleDelayMillis) {
		this.scheduleDelayMillis = scheduleDelayMillis;
	}

	protected void setShutdownTimeoutMillis(Long shutdownTimeoutMillis) {
		this.shutdownTimeoutMillis = shutdownTimeoutMillis;
	}

	public Long scheduleDelayMillis() {
		return this.scheduleDelayMillis;
	}

	public Long shutdownTimeoutMillis() {
		return this.shutdownTimeoutMillis;
	}

	protected Task schedulerTask() {
		return this.schedulerTask;
	}

	protected void setSchedulerTask(Task task) {
		this.schedulerTask = task;
	}

	/**
	 * Method for starting a new listener schedule. Uses mainLoop() method to get actual work.
	 */
	public void start() {
		if (!isEnabled()) {
			setSchedulerTask(Scheduling.fixedRateBuilder()
			                           .delay(scheduleDelayMillis())
			                           .timeUnit(TimeUnit.MILLISECONDS)
			                           .task(inv -> mainLoop())
			                           .build());
		} else {
			log.warn("Process already started!");
		}
		log.info("Process started.");
	}

	abstract protected void mainLoop();

	abstract protected void mainLoopIteration() throws ExecutionException, InterruptedException, JsonProcessingException;


	public void mainLoopIterationWrapper() {
		try {
			mainLoopIteration();
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to check if this listener is active.
	 *
	 * @return
	 */
	public boolean isEnabled() {
		return Objs.isNotNull(schedulerTask()) && !schedulerTask().executor().isShutdown();
	}


	/**
	 * Method to abort scheduled execution without waiting for tasks to return.
	 * Internally checks that schedule is enabled or do nothing
	 */
	public void abort() {
		if (isEnabled()) {
				try (final var executor = schedulerTask().executor()) {
					executor.shutdownNow();
				}
			log.info("Process stopped.");
		} else {
			log.warn("Process already stopped!");
		}
	}

	/**
	 * Method to gracefully stop accepting new tasks, but wait for existing tasks to process.
	 * If timeout for waiting will expire or interrupt, method will just abort().
	 */
	public void shutdown() {
		if (isEnabled()) {
				try (final var executor = schedulerTask().executor()) {
					executor.shutdown();
					try {
						if (!executor.awaitTermination(shutdownTimeoutMillis(), TimeUnit.MILLISECONDS)) {
							executor.shutdownNow();
						}
					} catch (InterruptedException e) {
						log.warn("Termination aborted! " + e.getMessage());
						executor.shutdownNow();
					}
				}
		}
	}

}
