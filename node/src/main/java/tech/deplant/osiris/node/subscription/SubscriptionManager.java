package tech.deplant.osiris.node.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.osiris.GraphQLUtils;
import tech.deplant.osiris.contract.CustomTaskElector;
import tech.deplant.osiris.contract.TaskSubscription;
import tech.deplant.osiris.model.exception.TemplateProcessingException;
import tech.deplant.osiris.model.template.TaskTemplate;
import tech.deplant.osiris.node.ListenerControls;
import tech.deplant.osiris.node.OracleNode;
import tech.deplant.osiris.template.TaskSubscriptionTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SubscriptionManager extends ListenerControls {

	private final Logger log = LogManager.getLogger(SubscriptionManager.class);
	private final Map<String, SubscriptionDetails> subscriptionsMap = new ConcurrentHashMap<>();

	private final Set<String> badSubscriptions = new HashSet<>();

	private final OracleNode node;
	private String codeHash = "";

	// RequestsProcessor
	public SubscriptionManager(OracleNode node) {
		this.node = node;
	}

	public SubscriptionDetails parseSubscribedTask(final Sdk sdk,
	                                               final Account accountOfSubscription) throws JsonProcessingException, TemplateProcessingException, EverSdkException {
		var subscription = new TaskSubscription(sdk, accountOfSubscription.id());

		final String taskAddress = subscription.getLocalTaskAddress(accountOfSubscription.boc());
		final Long lastRequestTx = subscription.getLocalLastRequestTx(accountOfSubscription.boc());

		var elector = new CustomTaskElector(sdk, taskAddress);

		final TaskTemplate template = TaskTemplate.deserialize(node().mapper(), elector.jsonBody());

		return new SubscriptionDetails(taskAddress,
		                               accountOfSubscription.id(),
		                               lastRequestTx,
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

	public Set<String> badSubscriptions() {
		return this.badSubscriptions;
	}

	public String subscribe(String taskAddress) throws EverSdkException, TemplateProcessingException, JsonProcessingException {
		var subAddress = node().oracleValidator()
		                       //TODO Last processed tx!!!
		                       .subscribe(new Address(taskAddress), Long.valueOf("0"))
		                       .call()
		                       .subscriptionAddress();
		var subscriptionDetails = parseSubscribedTask(node().oracleValidator().sdk(),
		                                              Account.ofAddress(node().oracleValidator()
		                                                                      .sdk(),
		                                                                subAddress.makeAddrStd()));
		node().subscriptionManager().addSubscribedTask(subscriptionDetails);
		return subAddress.makeAddrStd();
	}

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
			checkDeployedSubscriptions();
			super.start();
		} catch (EverSdkException e) {
			throw new RuntimeException(e);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void mainLoop() {
		mainLoopIterationWrapper();
		log.debug("Checking for deployed task subscriptions!");
	}

	@Override
	protected void mainLoopIteration() throws ExecutionException, InterruptedException, JsonProcessingException {

	}

	private void checkDeployedSubscriptions() throws JsonProcessingException {
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
				.filter(gqlResponse -> !badSubscriptions().contains(gqlResponse.get("id").asText()))
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
				.forEach(this::addSubscribedTask);
	}

}
