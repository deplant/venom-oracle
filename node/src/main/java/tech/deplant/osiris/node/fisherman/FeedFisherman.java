package tech.deplant.osiris.node.fisherman;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.utils.Objs;
import tech.deplant.osiris.ElectionPhase;
import tech.deplant.osiris.Elector;
import tech.deplant.osiris.node.ListenerControls;
import tech.deplant.osiris.node.OracleNode;
import tech.deplant.osiris.template.TaskMedianizedValueFeedTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class FeedFisherman extends ListenerControls {

	private static final Logger log = LogManager.getLogger(FeedFisherman.class);

	private final OracleNode node;

	public Map<String, FishInfo> pond = new ConcurrentHashMap<>();

	public FeedFisherman(OracleNode node) {
		super(20000L, 10000L);
		this.node = node;
	}

	@Override
	protected void mainLoop() {
		for (var fish : pond.entrySet()) {
			log.info("Updating fish: " + fish.getKey());
			mainLoopIterationWrapper(fish.getValue());
		}
	}

	@Override
	protected void mainLoopIteration(Object obj) throws ExecutionException, InterruptedException, JsonProcessingException, EverSdkException {
		if ( obj instanceof FishInfo fish && Objs.isNotNull(fish.subscriptionDetails())) {
			var currentTime = Instant.now().getEpochSecond();
			if (currentTime > fish.previousTime() + fish.timeDeviation()) {
				var elector = Elector.ofType(fish.subscriptionDetails().taskType(),node().oracleValidator().sdk(),fish.subscriptionDetails().taskId());
				if (elector.currentPhase().equals(ElectionPhase.READY)) {
					node().oracleValidator().refreshFeed(Address.fromJava(fish.subscriptionDetails().taskId())).callTree(true,
					                                                                                                     TaskMedianizedValueFeedTemplate.DEFAULT_ABI());
					this.pond.put(fish.subscriptionDetails().taskId(),fish.withUpdatedPreviousTime(currentTime));
				}
			}
		}
	}

	public OracleNode node() {
		return node;
	}
}
