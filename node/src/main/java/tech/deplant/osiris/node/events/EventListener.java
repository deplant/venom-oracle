package tech.deplant.osiris.node.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.utils.Objs;
import tech.deplant.osiris.Elector;
import tech.deplant.osiris.TaskType;
import tech.deplant.osiris.model.request.OracleRequest;
import tech.deplant.osiris.node.ListenerControls;
import tech.deplant.osiris.node.OracleNode;
import tech.deplant.osiris.node.subscription.GraphQLQuery;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class EventListener extends ListenerControls {

	private static final Logger log = LogManager.getLogger(EventListener.class);

	private final OracleNode node;
	private Long lastRequestTx = 0L;

	// RequestsProcessor
	public EventListener(OracleNode node) {
		this.node = node;
	}

	public OracleNode node() {
		return this.node;
	}

	@Override
	protected void mainLoop() {
		mainLoopIterationWrapper(null);
	}

	@Override
	protected void mainLoopIteration(Object obj) throws ExecutionException, InterruptedException, JsonProcessingException {
		var subscriptions = node().subscriptionManager().subscriptionsMap();
		var subbed = subscriptions.keySet();
		if (subbed.isEmpty()) {
			return;
		}
		//TODO Split subbed addresses to separate queries by 25-100 elements
		final String eventQuery = String.format("""
				                                        {
				                                          messages(
				                                          filter:
				                                          {
				                                         msg_type:{eq:2},
				                                         src:{
				                                           in: ["%s",]
				                                         },
				                                         created_at: {gt:%d}
				                                          })
				                                            { id body boc src created_at }
				                                        }
				                                        """,
		                                        String.join("\",\"", subbed),
		                                        lastRequestTx);
		log.debug(eventQuery);
		var mapper = node().mapper();
		final String jsonRequest;
		jsonRequest = mapper.writeValueAsString(new GraphQLQuery(eventQuery));
		final var jsonResponse = node().webClient().postJSON(node().endpoint(), jsonRequest);

		//final var jsonResponse = HttpAction.httpRequest(graphQLEndpoint(), jsonRequest, "POST");

		//TODO If we added events to queue, but we selected more GraphQL events than our limit (last:5), make another select

		final var jsonElement = mapper
				.readTree(jsonResponse)
				.at("/data/messages")
				.elements();

		if (jsonElement.hasNext()) {

			jsonElement.forEachRemaining(elem -> {
				try {
					//final var requestId = elem.get("id").asText();

					var subscriptionDetails = node().subscriptionManager().subscriptionsMap().get(elem.get("src").asText());

					var taskElector = Elector.ofType(subscriptionDetails.taskType(),
					                                 node().oracleValidator().sdk(),
					                                 elem.get("src").asText());

					switch (subscriptionDetails.taskType()) {
						case MEDIAN_IMMEDIATE, PRECISE_IMMEDIATE -> {
							var request = decodeMessageBocOnRequest(subscriptionDetails.taskType(), taskElector,
							                                        elem.get("boc").asText(),
							                                        elem.get("id").asText(),
							                                        elem.get("created_at").asLong());
							if (Objs.isNotNull(request)) {
								node().requestQueue().addRequest(request);
							}
						}
//							case ScheduleTrigger sch ->
//									node().scheduledTasksMap().addScheduledTask(taskElector.address(),
//									                                            Scheduling.cronBuilder()
//									                                                      .expression(sch.schedule())
//									                                                      //TODO What should we do in scheduled task?
//									                                                      .task(inv -> node().requestQueue()
//									                                                                         .addRequest(
//											                                                                         requestStr))
//									                                                      .build());
						case MEDIAN_FEED, PRECISE_FEED -> {
							var request = decodeMessageBocOnRequest(subscriptionDetails.taskType(), taskElector,
							                                        elem.get("boc").asText(),
							                                        elem.get("id").asText(),
							                                        elem.get("created_at").asLong());
							if (Objs.isNotNull(request)) {
								node().requestQueue().addRequest(request);
							}
						}
					}
					this.lastRequestTx =
							this.lastRequestTx < elem.get("created_at").asLong() ? elem.get("created_at")
							                                                           .asLong() : this.lastRequestTx;
				} catch (JsonProcessingException | EverSdkException e) {
					log.error("Error on Event Boc decode! Error: " + e);
				}
				// after all actions, write down lastRequest transaction timestamp
			});
		} else {
			log.debug("No new requests found.");
		}
	}

	private OracleRequest decodeMessageBocOnRequest(TaskType type, Elector elector,
	                                                String boc,
	                                                String requestId,
	                                                long requestCreated) throws EverSdkException {
		Abi.DecodedMessageBody decodedMessage = null;
		if (type.equals(TaskType.PRECISE_FEED) || type.equals(TaskType.MEDIAN_FEED)) {
			decodedMessage = elector.decodeEventBoc("refreshConsensus",boc);
			if ("Event".equals(decodedMessage.bodyType().name())) {
				final var requestStr = new OracleRequest(
						new BigInteger(requestId, 16),
						elector.address(),
						requestCreated,
						decodedMessage.value().get("requester").toString()
				);
				log.debug("Found new request event! Id: " + requestStr.toString());
				return requestStr;
			} else {
				return null;
			}
		} else {
			decodedMessage = elector.decodeEventBoc("requestReceived",boc);
			if ("Event".equals(decodedMessage.bodyType().name())) {
				final var requestStr = new OracleRequest(
						new BigInteger(requestId, 16),
						elector.address(),
						requestCreated,
						decodedMessage.value().get("requester").toString()
				);
				log.debug("Found new request event! Id: " + requestStr.toString());
				return requestStr;
			} else {
				return null;
			}
		}
	}
}
