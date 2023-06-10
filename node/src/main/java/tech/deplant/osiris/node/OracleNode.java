package tech.deplant.osiris.node;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.osiris.contract.OracleValidator;
import tech.deplant.osiris.model.task.TaskContext;
import tech.deplant.osiris.node.client.JavaClient;
import tech.deplant.osiris.node.client.WebClient;
import tech.deplant.osiris.node.consensus.ConsensusParticipator;
import tech.deplant.osiris.node.events.EventListener;
import tech.deplant.osiris.node.queues.CompletedTasksQueue;
import tech.deplant.osiris.node.queues.RequestQueue;
import tech.deplant.osiris.node.queues.ScheduledTasksMap;
import tech.deplant.osiris.node.subscription.SubscriptionManager;
import tech.deplant.osiris.node.tasks.TaskProcessor;

import java.io.IOException;

public class OracleNode {

	private final String endpoint;

	private final OracleValidator oracleValidator;
	private final EventListener eventListener;
	private final RequestQueue requestQueue;
	private final CompletedTasksQueue completedTasksQueue;

	private final ScheduledTasksMap scheduledTasksMap;
	private final SubscriptionManager subscriptionManager;
	private final TaskProcessor taskProcessor;

	private final JsonMapper mapper;

	private final ConsensusParticipator consensusParticipator;

	private final WebClient webClient;

	public OracleNode(OracleNodeConfig nodeConfig) throws IOException {
		var sdk = Sdk.builder()
		             .networkEndpoints(nodeConfig.validatorConfig().endpoint())
		             .timeout(nodeConfig.validatorConfig().timeout().intValue())
		             .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));

		var jsonMapper = JsonMapper.builder()
		                           .addModules(new ParameterNamesModule(), new Jdk8Module(), new JavaTimeModule())
		                           .build();

		this.mapper = JsonMapper.builder()
		                        .addModules(new ParameterNamesModule(), new Jdk8Module(), new JavaTimeModule())
		                        .build();
		var cborMapper = CBORMapper.builder()
		                           .addModules(new ParameterNamesModule(), new Jdk8Module(), new JavaTimeModule())
		                           .build();
		this.endpoint = nodeConfig.validatorConfig().endpoint();
		this.oracleValidator = new OracleValidator(sdk,
		                                           nodeConfig.validatorConfig().address(),
		                                           new Credentials(nodeConfig.validatorConfig().publicKey(),
		                                                           nodeConfig.validatorConfig().privateKey()));
		this.subscriptionManager = new SubscriptionManager(this);
		this.eventListener = new EventListener(this);
		this.requestQueue = new RequestQueue();
		this.completedTasksQueue = new CompletedTasksQueue();
		this.scheduledTasksMap = new ScheduledTasksMap();
		this.taskProcessor = new TaskProcessor(this, new TaskContext(sdk,
		                                                             jsonMapper,
		                                                             cborMapper,
		                                                             nodeConfig.taskConfig().maxTaskThreads(),
		                                                             nodeConfig.taskConfig()
		                                                                       .maxActionThreadsPerTask()));
		this.consensusParticipator = new ConsensusParticipator(this, 120000L);
		this.webClient = JavaClient.of();
	}

	public void start() {
		subscriptionManager().start();
		eventListener().start();
		taskProcessor().start();
		consensusParticipator().start();
	}

	public void abort() {
		subscriptionManager().abort();
		eventListener().abort();
		taskProcessor().abort();
		consensusParticipator().abort();
	}

	public String endpoint() {
		return endpoint;
	}

	public OracleValidator oracleValidator() {
		return oracleValidator;
	}


	public EventListener eventListener() {
		return eventListener;
	}

	public ConsensusParticipator consensusParticipator() {
		return this.consensusParticipator;
	}

	public RequestQueue requestQueue() {
		return requestQueue;
	}

	public CompletedTasksQueue completedTasksQueue() {
		return completedTasksQueue;
	}

	public ScheduledTasksMap scheduledTasksMap() {
		return scheduledTasksMap;
	}

	public SubscriptionManager subscriptionManager() {
		return subscriptionManager;
	}

	public TaskProcessor taskProcessor() {
		return taskProcessor;
	}

	public WebClient webClient() {
		return this.webClient;
	}

	public JsonMapper mapper() {
		return this.mapper;
	}

}
