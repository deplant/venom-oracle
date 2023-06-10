package tech.deplant.osiris.node.tst;

//import node.concurrency.ActionGraph;
//import node.concurrency.Task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import tech.deplant.osiris.model.exception.ActionProcessingException;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class YankinTests {

	private static final Logger log = LogManager.getLogger(YankinTests.class);
	//private static ActionGraph GRAPH;

//    @BeforeAll
//    public static void beforeCreateGraph() {
//        // Test Graph ASCII
//        //  4   3  7
//        //   \ /   |
//        //    0    |   8
//        //   / \   |  /
//        //  |   \  | /
//        //  |    \ |/
//        //  1     2
//        //  \     /
//        //   \   /
//        //     5
//        //     |
//        //     6
//        var vertices = Map.of(
//                "0", new ActionGraph.Vertex("0", new JsonParseAction("json_parse_action_0", ResponseType.UINT32, "{\"count\":0}", "/count")),
//                "1", new ActionGraph.Vertex("1", new JsonParseAction("json_parse_action_1", ResponseType.UINT32, "{\"count\":1}", "/count")),
//                "2", new ActionGraph.Vertex("2", new JsonParseAction("json_parse_action_2", ResponseType.UINT32, "{\"count\":2}", "/count")),
//                "3", new ActionGraph.Vertex("3", new JsonParseAction("json_parse_action_3", ResponseType.UINT32, "{\"count\":3}", "/count")),
//                "4", new ActionGraph.Vertex("4", new JsonParseAction("json_parse_action_4", ResponseType.UINT32, "{\"count\":4}", "/count")),
//                "5", new ActionGraph.Vertex("5", new JsonParseAction("json_parse_action_5", ResponseType.UINT32, "{\"count\":5}", "/count")),
//                "6", new ActionGraph.Vertex("6", new JsonParseAction("json_parse_action_6", ResponseType.UINT32, "{\"count\":6}", "/count")),
//                "7", new ActionGraph.Vertex("7", new JsonParseAction("json_parse_action_7", ResponseType.UINT32, "{\"count\":7}", "/count")),
//                "8", new ActionGraph.Vertex("8", new JsonParseAction("json_parse_action_8", ResponseType.UINT32, "{\"count\":8}", "/count"))
//        );
//        var edges = List.of(
//                new ActionGraph.Edge("4", "0"),
//                new ActionGraph.Edge("3", "0"),
//                new ActionGraph.Edge("7", "2"),
//                new ActionGraph.Edge("0", "1"),
//                new ActionGraph.Edge("0", "2"),
//                new ActionGraph.Edge("1", "5"),
//                new ActionGraph.Edge("2", "5"),
//                new ActionGraph.Edge("5", "6"),
//                new ActionGraph.Edge("8", "2")
//        );
//
//        GRAPH = new ActionGraph(vertices, edges);
//    }

	/**
	 * Test that goes through whole execution flow with different variants and outcomes.
	 * Mocks almost everything, but should be precise on overall graph and choices.
	 */
//	@Test
//	public void testMockedExcutionFlow() throws IOException, EverSdkException {
//
//		Sdk sdk = Sdk.builder()
//		             .networkEndpoints("https://devnet.evercloud.dev/032a23e8f6254ca0b4ae4046819e7ac1/graphql")
//		             .timeout(50L)
//		             .build(JavaLibraryPathLoader.TON_CLIENT);
//
//		var abi = Tip31RootTemplate.DEFAULT_ABI();
//
//		OwnedContract contract = new OwnedContract(
//				sdk,
//				"0:b85c736fda69c66f7ca4084c04e9f186d00d352474dc4ffe797c31a4dcdbce05",
//				abi,
//				Credentials.RANDOM(sdk)
//		);
//		var inputs = new HashMap<String, Object>();
//		inputs.put("answerId", "1");
//		log.debug("TIP3 Token Name: " + contract.runGetter("name", inputs, null).get("value0").toString());
//		log.debug("TIP3 Token Root Owner: " + contract.runGetter("rootOwner", inputs, null).get("value0").toString());
//
//        //TODO 0 Job description
//
//        //TODO 0.1
//        // Put job descriptions to memory
//        Map<String, TaskTemplate> taskTemplatesCache = new HashMap<>();
//        //taskTemplatesCache.put("0001", taskImmediate);
//        //taskTemplatesCache.put("0002", taskScheduled);
//
//        //TODO 1 Request
//
//        // Currently mocked in RequestEventListener
//
//        //TODO 2 Getting request from chain
//        var requestsListener = new RequestEventListener(new SdkBuilder().create(JavaLibraryPathLoader.TON_CLIENT),
//                Address.ZERO,
//                Instant.now());
//        requestsListener.pingEvents();
//
//        Request request = null;
//
//        while (requestsListener.hasNewRequest()) {
//            request = requestsListener.getNextRequest();
//
//            //TODO 3.1.1 Getting task & trigger details from cache
//            TaskTemplate taskTemplate = taskTemplatesCache.get(request.taskTemplateId()); // getting actual task by it's id
//
//            //TODO 3.1.2 if task not found, we need to load new tasks... ?
////			try (var scope = new TaskScope()) {
////
////				var task = new Task(taskTemplate, request);
////
//////                switch (taskTemplate.triggerType()) {
//////                    //TODO 3.2.1 Immediate execution variant
//////                    case IMMEDIATE -> scope.immediate(task.runnable());
//////                    //TODO 3.2.2 Scheduled execution variant
//////
//////                    // "CRON_TZ=UTC * */20 * * * *"
//////                    case SCHEDULE -> scope.schedule(task.runnable());
//////                }
////			}
//
//        }

		// IN RUNNABLE: {

		//    fetch    [type="http" method=GET url="https://chain.link/ETH-USD"]
		//    parse    [type="jsonparse" path="data,price"]
		//    multiply [type="multiply" times=100]

		//TODO 4 Execution of Job's action graph

		//TODO 5 Writing commit hash part of the result to chain

		//TODO 6 Writing reveal part of the result to chain

		//TODO 7 Exclusion of request thread from pipeline

		// }

	//}

	@Test
	public void testActionSequence() throws ActionProcessingException {

//        var action1 = new HttpAction("https://cex.io/api/ticker/EVER/USD", Map.of("Accept", "application/json"));
//        var action2 = new JsonParseAction(resp, "pair");
//
//        var resp = String.valueOf(new HttpAction("https://cex.io/api/ticker/EVER/USD",
//                Map.of("Accept", "application/json")).result(ResponseType.JSON).unwrap());
//        //{"timestamp":"1660129183","low":"0.0761","high":"0.0905","last":"0.077","volume":"15464.057249","volume30d":"55111.873010","bid":0.08,"ask":0.0857,"priceChange":"-0.0134","priceChangePercentage":"-14.82","pair":"EVER:USD"}
//        var parsed = String.valueOf(new JsonParseAction(resp, "pair").result(ResponseType.JSON).unwrap());
//        log.debug("pair: " + parsed);
	}

//    @Test
//    public void testGraphCorrectness() throws ActionProcessingException {
//
//        try (var scope = new TaskScope()) {
//            var queue = GRAPH.getActionQueue();
//            while (queue.size() > 0) {
//                scope.actionHandling(queue.pollLast());
//            }
//            GRAPH.process("6");
//            scope.actionResponses().entrySet().forEach(entry -> {
//                try {
//                    log.debug("Action: " + entry.getKey() + " Result: " + entry.getValue().unwrap());
//                } catch (ActionProcessingException e) {
//                    log.error("Action: " + entry.getKey() + " Error: " + e.getMessage());
//                }
//            });
//        }
//    }

	@Test
	public void testSchedule() {
		ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor();
		service.schedule(() -> {
			System.out.println("Done!");
		}, 30L, TimeUnit.SECONDS);
	}

	/**
	 * Test for checking limit for threads (Semaphore)
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void testSemaphore() throws InterruptedException {
		// WITH SEMAPHORE
		Semaphore DB_SEMAPHORE = new Semaphore(16);
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			DB_SEMAPHORE.acquire();
			try {
				executor.submit(() -> {
					System.out.println("Done!");
				});
			} finally {
				DB_SEMAPHORE.release();
			}
		} finally {
			log.info("Timestamp (Task Cycle Ended): " + Instant.now().toString());
		}
	}


}
