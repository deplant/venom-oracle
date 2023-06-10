package tech.deplant.osiris.node.queues;

import tech.deplant.osiris.model.request.OracleRequest;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RequestQueue {

	private final Queue<OracleRequest> queue;

	// RequestsProcessor
	public RequestQueue() {
		this.queue = new ConcurrentLinkedDeque<>();
	}

	public void addRequest(OracleRequest req) {
		this.queue.add(req);
	}

	public OracleRequest[] requests() {
		return this.queue.toArray(OracleRequest[]::new);
	}

	public boolean hasNext() {
		return this.queue.size() > 0;
	}

	public OracleRequest poll() {
		return this.queue.poll();
	}

	public int size() {
		return this.queue.size();
	}

}
