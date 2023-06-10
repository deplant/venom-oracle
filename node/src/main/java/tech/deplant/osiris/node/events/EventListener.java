package tech.deplant.osiris.node.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.helidon.scheduling.Scheduling;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.osiris.contract.CustomTaskElector;
import tech.deplant.osiris.model.request.OracleRequest;
import tech.deplant.osiris.model.trigger.ImmediateTrigger;
import tech.deplant.osiris.model.trigger.ScheduleTrigger;
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
		mainLoopIterationWrapper();
	}

	@Override
	protected void mainLoopIteration() throws ExecutionException, InterruptedException, JsonProcessingException {
		var subscriptions = node().subscriptionManager().subscriptionsMap();
		var subbed = subscriptions.keySet();
		if (subbed.isEmpty()) {
			log.debug("As there are no subscribed tasks, no need to check events...");
		} else {
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
			try {
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

						final var requestId = elem.get("id").asText();
						CustomTaskElector taskElector = null;
						try {
							taskElector = new CustomTaskElector(node().oracleValidator().sdk(),
							                                    elem.get("src").asText());
						} catch (final JsonProcessingException e) {
							// TODO Handle errors correctly
							throw new RuntimeException(e);
						}
						Abi.DecodedMessageBody decodedMessage = null;
						try {
							var boc = elem.get("boc").asText();
							decodedMessage = taskElector.decodeMessageBoc(TvmCell.fromJava(boc));
						} catch (final EverSdkException e) {
							// TODO Handle errors correctly
							throw new RuntimeException(e);
						}
						if ("Event".equals(decodedMessage.bodyType().name())) {
							log.debug("Decoded value:" + decodedMessage.value());
							final var requestStr = new OracleRequest(
									new BigInteger(requestId, 16),
									taskElector.address(),
									elem.get("created_at").asLong(),
									decodedMessage.value().get("requester").toString()
							);
							log.debug("Found this event: " + requestStr.toString());

							var template = subscriptions.get(taskElector.address()).template();

							switch (template.trigger()) {
								case ImmediateTrigger it -> node().requestQueue().addRequest(requestStr);
								case ScheduleTrigger sch ->
										node().scheduledTasksMap().addScheduledTask(taskElector.address(),
										                                            Scheduling.cronBuilder()
										                                                      .expression(sch.schedule())
										                                                      //TODO What should we do in scheduled task?
										                                                      .task(inv -> node().requestQueue()
										                                                                         .addRequest(
												                                                                         requestStr))
										                                                      .build());
							}

							// after all actions, write down lastRequest transaction timestamp
							this.lastRequestTx = this.lastRequestTx < elem.get("created_at").asLong() ? elem.get("created_at").asLong() : this.lastRequestTx;
						}
					});
				} else {
					log.debug("No new requests found.");
				}
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
