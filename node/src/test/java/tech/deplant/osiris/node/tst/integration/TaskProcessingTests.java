package tech.deplant.osiris.node.tst.integration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskProcessingTests {

	private static final Logger log = LogManager.getLogger(TaskProcessingTests.class);

//    @Test
//    public void full_execution_from_json() throws TemplateProcessingException {
//        //TODO Check every part
//        var template = TaskTemplate.ofJson(JsonExamples.TASK_VERSION_1_JSON_2);
//        var task = new Task(template, null);
//        task.execute();
//    }

//	private static RunnableTask CTX;
//
//	@BeforeEach
//	public void init_context() throws IOException {
//		CTX = MockRunnableTask.get();
//	}

//	@Test
//	public void test_find_final_consensus() throws TemplateProcessingException, ActionProcessingException, JsonProcessingException {
//
//		//TODO Remove null
//		var template = TaskTemplate.deserialize(null,JsonTemplateParseTests.MULTIPLY_2_AND_ENCODE.replaceAll("[\u0000-\u001f]",
//		                                                                                           ""));
//		Action[] actions = template.actions();
//		long count = Arrays
//				.stream(actions)
//				.filter(action ->
//						        action.actionType().equals(ActionType.PRECISE_CONSENSUS) ||
//						        action.actionType().equals(ActionType.MEDIAN_CONSENSUS))
//				.count();
//		if (count == 1) {
//			var consensusAction = Arrays
//					.stream(actions)
//					.filter(action -> action.actionType().equals(ActionType.PRECISE_CONSENSUS))
//					.findAny().get();
//			log.debug("ID: " + consensusAction.id());
//		} else if (count > 1) {
//			log.warn("Wrong task template, more than one consensus action!");
//		} else {
//			log.warn("Wrong task template, no consensus action found!");
//		}
//
//	}
//
//	@Test
//	public void test_multiply_and_tvmcell() throws TemplateProcessingException, ActionProcessingException, JsonProcessingException {
//
//		var template = TaskTemplate.ofJson(JsonTemplateParseTests.MULTIPLY_2_AND_ENCODE.replaceAll("[\u0000-\u001f]",
//		                                                                                           ""));
//		try (var scope = new TaskContext(template)) {
//			scope.run();
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		} catch (EverSdkException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Test
//	public void test_ever_usd() throws TemplateProcessingException, ActionProcessingException, JsonProcessingException {
//
//		var template = TaskTemplate.ofJson(JsonTemplateParseTests.ORTODOX_JSON_2.replaceAll("[\u0000-\u001f]", ""));
//
//		try (var scope = new TaskContext(template)) {
//			scope.run();
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		} catch (EverSdkException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Test
//	public void actions_galore() throws TemplateProcessingException, ActionProcessingException {
//
//		var act1 = new HttpAction("http_01",
//		                          ResponseType.STRING,
//		                          new ConstStringAction("GET"),
//		                          new ConstStringAction("https://cex.io/api/ticker/EVER/USD"),
//		                          new ConstStringAction("GET"));
//		var act2 = new HttpAction("http_02",
//		                          ResponseType.STRING,
//		                          new ConstStringAction("GET"),
//		                          new ConstStringAction("https://cex.io/api/ticker/EVER/USD"),
//		                          new ConstStringAction("GET"));
//		var act3 = new JsonParseAction("json_03",
//		                               ResponseType.UINT256,
//		                               act1,
//		                               new ConstStringAction("/last"));
//		var act4 = new JsonParseAction("json_04",
//		                               ResponseType.UINT256,
//		                               act2,
//		                               new ConstStringAction("/last"));
//		var act5 = new MedianAction(
//				"median_05",
//				ResponseType.STRING,
//				act3,
//				act4,
//				new ConstStringAction("0.0715"),
//				new ConstStringAction("0.0716"),
//				new ConstStringAction("0.0716")
//		);
//
//		var template = new StandardTaskTemplate(
//				1,
//				"bad_name",
//				new ImmediateTrigger(),
//				"ext0",
//				act1, act2, act3, act4, act5
//		);
//
//		try (var scope = new TaskContext(template)) {
//			scope.run();
//		} catch (JsonProcessingException e) {
//			throw new RuntimeException(e);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		} catch (EverSdkException e) {
//			throw new RuntimeException(e);
//		}
//	}

}
