package tech.deplant.osiris.node.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.utils.Objs;
import tech.deplant.osiris.ConsensusType;
import tech.deplant.osiris.Elector;
import tech.deplant.osiris.GraphQLUtils;
import tech.deplant.osiris.TaskType;
import tech.deplant.osiris.contract.TaskMedianizedOnRequest;
import tech.deplant.osiris.contract.TaskPreciseOnRequest;
import tech.deplant.osiris.contract.TaskSubscription;
import tech.deplant.osiris.model.exception.TemplateProcessingException;
import tech.deplant.osiris.model.template.TaskTemplate;
import tech.deplant.osiris.model.trigger.TriggerType;
import tech.deplant.osiris.node.ListenerControls;
import tech.deplant.osiris.node.OracleNode;
import tech.deplant.osiris.node.fisherman.FishInfo;
import tech.deplant.osiris.template.TaskSubscriptionTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SubscriptionManager extends ListenerControls {

	private final Logger log = LogManager.getLogger(SubscriptionManager.class);
	private final Map<String, SubscriptionDetails> subscriptionsMap = new ConcurrentHashMap<>();
	private final Set<String> badSubscriptions = new HashSet<>();
	private final Set<String> badTasks = new HashSet<>();
	private final OracleNode node;
	private String codeHash = "";

	private String preciseRequestCodeHash;
	private String medianRequestCodeHash;
	private String preciseFeedCodeHash;
	private String medianFeedCodeHash;

	// RequestsProcessor
	public SubscriptionManager(OracleNode node, SubscriptionConfig config) {
		super(config.autoSubscriptionCheckInterval(), 10000L);
		this.preciseRequestCodeHash = config.requestPreciseCodeHash();
		this.medianRequestCodeHash = config.requestMedianizedCodeHash();
		this.preciseFeedCodeHash = config.feedPreciseCodeHash();
		this.medianFeedCodeHash = config.feedMedianizedCodeHash();
		this.node = node;
		this.badSubscriptions.add("0:0ec8be8a704598ac90ad09cacb5f34e5eac261bccaf3d1b3d783fab4208fcd77");
	}

	public static TaskType taskTypeOf(TriggerType trigger, ConsensusType consensus) {
		if (trigger.equals(TriggerType.IMMEDIATE) && consensus.equals(ConsensusType.MEDIAN)) {
			return TaskType.MEDIAN_IMMEDIATE;
		} else if (trigger.equals(TriggerType.IMMEDIATE) && consensus.equals(ConsensusType.PRECISE)) {
			return TaskType.PRECISE_IMMEDIATE;
		} else if (trigger.equals(TriggerType.VALUE_FEED) && consensus.equals(ConsensusType.MEDIAN)) {
			return TaskType.MEDIAN_FEED;
		} else if (trigger.equals(TriggerType.VALUE_FEED) && consensus.equals(ConsensusType.PRECISE)) {
			return TaskType.PRECISE_FEED;
		} else {
			return TaskType.MEDIAN_FEED;
		}
	}

	public SubscriptionDetails parseSubscribedTask(final Sdk sdk,
	                                               final Account accountOfSubscription) throws JsonProcessingException, TemplateProcessingException, EverSdkException {
		var subscription = new TaskSubscription(sdk, accountOfSubscription.id());
		final String taskId = subscription.getLocalTaskAddress(accountOfSubscription.boc());
		var task = new TaskPreciseOnRequest(sdk, taskId);
		var elector = new Elector(task);
		long lastResponseTx = Long.parseLong(elector.previousStateProperty("responseTimestamp").toString());
		final TaskTemplate template = TaskTemplate.deserialize(node().mapper(), elector.jsonBody());
		return new SubscriptionDetails(taskId,
		                               accountOfSubscription.id(),
		                               taskTypeOf(template.trigger().triggerType(), template.consensus().type()),
		                               lastResponseTx,
		                               template);
	}

	public OracleNode node() {
		return this.node;
	}

	public Set<String> subbedTasks() {
		return subscriptionsMap().keySet();
	}

	public Set<String> subscriptions() {
		return subscriptionsMap().values()
		                         .stream()
		                         .map(SubscriptionDetails::subscriptionId)
		                         .collect(Collectors.toSet());
	}

	public SubscriptionDetails subscribe(String taskId, TaskType type) {
		if (subscriptionsMap().containsKey(taskId) || this.badTasks.contains(taskId)) {
			return null;
		}
		log.debug("Found new unsubscribed task! Type: %s. Id: %s".formatted(type.toString(),
		                                                                    taskId));
		try {
			var addr = new Address(taskId);
			var elector = new Elector(new TaskMedianizedOnRequest(node().oracleValidator().sdk(), taskId));
			var lastResponseTx = Long.valueOf(elector.previousStateProperty("responseTimestamp").toString());
			var subAddress = node().oracleValidator()
			                       .subscribe(addr, lastResponseTx)
			                       .call()
			                       .subscriptionAddress();
			final TaskTemplate template = TaskTemplate.deserialize(node().mapper(), elector.jsonBody());
			var subscriptionDetails = new SubscriptionDetails(taskId,
			                                                  subAddress.makeAddrStd(),
			                                                  type,
			                                                  lastResponseTx,
			                                                  template);
			node().subscriptionManager().addSubscribedTask(subscriptionDetails);
			return subscriptionDetails;
		} catch (EverSdkException | TemplateProcessingException | JsonProcessingException e) {
			log.warn("Failed to process task subscription! " + e);
			this.badTasks.add(taskId);
			return null;
		}
	}

//	public void subscribe(String taskId) throws EverSdkException, TemplateProcessingException, JsonProcessingException {
//		new T
//	}

	public void unsubscribe(String taskAddress) throws EverSdkException {
		removeSubscribedTask(taskAddress);
		node().oracleValidator().unsubscribe(new Address(taskAddress)).call();
	}

	public Map<String, SubscriptionDetails> subscriptionsMap() {
		return this.subscriptionsMap;
	}

	public String codeHash() {
		return this.codeHash;
	}

	public void addSubscribedTask(final SubscriptionDetails subscriptionDetails) {
		subscriptionsMap().put(subscriptionDetails.taskId(), subscriptionDetails);
	}

	public void removeSubscribedTask(final String taskAddress) {
		subscriptionsMap().remove(taskAddress);
	}

	public void updateSubscriptionCode() throws EverSdkException {
		final String pathStr = System.getProperty("user.dir") + "/TaskSubscription.tvc";
		if (Files.exists(Paths.get(pathStr))) {
			node().oracleValidator().updateSubscriptionCode(Tvc.ofFile(pathStr));
		} else {
			node().oracleValidator().updateSubscriptionCode(TaskSubscriptionTemplate.DEFAULT_TVC());
		}
	}

	@Override
	public void start() {
		try {
			// Get subscription contract code hash for checking subs
			this.codeHash = node().oracleValidator().getSubscriptionCodeHash().get().codeHash().toString(16);
			log.info("Started checking contracts with code_hash: " + this.codeHash);
			// Check for existing subscriptions
			loadDeployedSubscriptions();
			super.start();
		} catch (EverSdkException | JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void mainLoop() {
		mainLoopIterationWrapper(null);
		//log.debug("Checking for deployed task subscriptions!");
	}

	@Override
	protected void mainLoopIteration(Object obj) throws JsonProcessingException, EverSdkException {

		// find all tasks, categorize them and subscribe to all new ones

		// MEDIAN_IMMEDIATE Tasks
		var medianizedOnRequestTaskArray = node.mapper()
		                                       .readValue(gqlRequest(new String[]{this.medianRequestCodeHash}),
		                                                  GraphQLAnswer.class)
		                                       .data()
		                                       .accounts();
		Arrays.stream(medianizedOnRequestTaskArray)
		      .forEach(task -> subscribe(task.get("id").toString(), TaskType.MEDIAN_IMMEDIATE));

		// PRECISE_IMMEDIATE Tasks
		var preciseOnRequestTaskArray = node.mapper()
		                                    .readValue(gqlRequest(new String[]{this.preciseRequestCodeHash}),
		                                               GraphQLAnswer.class)
		                                    .data()
		                                    .accounts();
		Arrays.stream(preciseOnRequestTaskArray)
		      .forEach(task -> subscribe(task.get("id").toString(), TaskType.PRECISE_IMMEDIATE));

		// MEDIAN_FEED Tasks
		var medianizedFeedTaskArray = node.mapper()
		                                  .readValue(gqlRequest(new String[]{this.medianFeedCodeHash}),
		                                             GraphQLAnswer.class)
		                                  .data()
		                                  .accounts();
		Arrays.stream(medianizedFeedTaskArray)
		      .forEach(task -> {
			      var taskId = task.get("id").toString();
			      var subscriptionDetails = subscribe(taskId, TaskType.MEDIAN_FEED);
			      if (Objs.isNotNull(subscriptionDetails)) {
				      node().feedFisherman().pond.put(taskId, new FishInfo(subscriptionDetails, 0L, 3600L));
			      }
		      });

		// PRECISE_FEED Tasks
		var preciseFeedTaskArray = node.mapper()
		                               .readValue(gqlRequest(new String[]{this.preciseFeedCodeHash}),
		                                          GraphQLAnswer.class)
		                               .data()
		                               .accounts();
		Arrays.stream(preciseFeedTaskArray)
		      .forEach(task -> {
			      var taskId = task.get("id").toString();
			      var subscriptionDetails = subscribe(taskId, TaskType.PRECISE_FEED);
			      node().feedFisherman().pond.put(taskId, new FishInfo(subscriptionDetails, 0L, 3600L));
		      });

	}

	public String gqlRequest(String[] codeHashes) throws JsonProcessingException {
		var url = node().endpoint();
		final String accountQuery = GraphQLUtils.accountsByCodeHash(codeHashes,
		                                                            "id balance last_trans_lt boc",
		                                                            "last_trans_lt",
		                                                            false,
		                                                            50);

		final String jsonRequest;
		jsonRequest = node().mapper().writeValueAsString(new GraphQLQuery(accountQuery));
		return node().webClient().postJSON(url, jsonRequest);
	}

	private void loadDeployedSubscriptions() throws JsonProcessingException {
		// Let's find all current active subscriptions by code_hash
		final String accountQuery =
				GraphQLUtils.accountsByCodeHash(new String[]{codeHash()},
				                                "id balance last_trans_lt boc",
				                                "last_trans_lt",
				                                false,
				                                50);
		final var mapper = node().oracleValidator().sdk().context().mapper();
		final String jsonRequest;
		jsonRequest = mapper.writeValueAsString(new GraphQLQuery(accountQuery));

		final var jsonResponse = node().webClient().postJSON(node().endpoint(), jsonRequest);

		//final var jsonResponse = HttpAction.httpRequest(node.graphQLEndpoint(), jsonRequest, "POST");
		final var jsonNodeIterator = mapper.readTree(jsonResponse).at("/data/accounts").elements();

		if (!jsonNodeIterator.hasNext()) {
			log.warn("No contracts found!");
		}

		StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(jsonNodeIterator, Spliterator.IMMUTABLE), true)
				.filter(gqlResponse -> !subscriptions().contains(gqlResponse.get("id").asText()))
				.filter(gqlResponse -> !this.badSubscriptions.contains(gqlResponse.get("id").asText()))
				.map(gqlResponse -> new Account(
						gqlResponse.get("id").asText(),
						1,
						gqlResponse.get("balance").asText(),
						gqlResponse.get("boc").asText(),
						null,
						null,
						null,
						codeHash(),
						null,
						0L
				))
				.map(account -> {
					try {
						return parseSubscribedTask(node().oracleValidator().sdk(), account);
					} catch (final EverSdkException | JsonProcessingException | TemplateProcessingException e) {
						this.log.error(
								"Error on retrieving taskAddress from subscription: " + account.id() + " Error: " +
								e);
						this.badSubscriptions.add(account.id());
						return null;
					}
				})
				.filter(Objects::nonNull)
				.forEach(subscription -> {
					addSubscribedTask(subscription);
					if (subscription.taskType().equals(TaskType.MEDIAN_FEED) ||
					    subscription.taskType().equals(TaskType.PRECISE_FEED)
					) {
						if (Objs.isNotNull(subscription)) {
							node().feedFisherman().pond.put(subscription.taskId(),
							                                new FishInfo(subscription, 0L, 3600L));
						}
					}
				});
	}

	public Set<String> badSubscriptions() {
		return badSubscriptions;
	}


	public void requestDone(String taskId, long lastProcessedTx) {
		var details = subscriptionsMap.get(taskId);
		subscriptionsMap.put(taskId, details.withLastProcessedRequestTx(lastProcessedTx));
	}
}
