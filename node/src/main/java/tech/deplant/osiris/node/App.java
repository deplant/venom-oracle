package tech.deplant.osiris.node;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.spi.ConfigSource;
import io.helidon.media.jackson.JacksonSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import tech.deplant.java4ever.utils.Strings;
import tech.deplant.osiris.node.events.RequestsListHandler;
import tech.deplant.osiris.node.events.RequestsStartHandler;
import tech.deplant.osiris.node.events.RequestsStopHandler;
import tech.deplant.osiris.node.subscription.*;
import tech.deplant.osiris.node.tasks.TasksListHandler;
import tech.deplant.osiris.node.tasks.TasksStartHandler;
import tech.deplant.osiris.node.tasks.TasksStopHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class App {

	public static WebServer ws;
	public static Routing routing;

	public static void main(final String[] args) throws IOException {
		final Config config = getConfig();
		var nodeConfig = OracleNodeConfig.of(config.get("oracle"));
		var node = new OracleNode(nodeConfig);
		ws = startServer(config, node, args);
//        Scheduling.cronBuilder()
//                .expression("0 45 9 ? * *")
//                .task(inv -> System.out.println("Executed every day at 9:45"))
//                .build();

	}

	private static Config getConfig() {
		List<Supplier<? extends ConfigSource>> configs = new ArrayList<>();
		// first priority - local file
		configs.add(() -> ConfigSources.file("application.yaml").optional().build());
		// second priority - classpath file
		configs.add(() -> ConfigSources
				.classpath("application.yaml")
				.optional()
				.build());
		return Config
				.builder()
				.sources(configs)
				.build();
	}

	private static WebServer startServer(Config config, OracleNode node, String[] args) {
		var builder = WebServer.builder(createRouting(config, node))
		                       .config(config.get("server"))
		                       .addMediaSupport(JacksonSupport.create());

		if (args.length >= 2 && Strings.isNotEmpty(args[0]) && Strings.isNotEmpty(args[1])) {
			builder.bindAddress(args[0])
			       .port(Integer.parseInt(args[1]));
		}

		WebServer server = builder.build();

		server.start()
		      .thenAccept(ws -> {
			      System.out.println(
					      "Oracle Node STARTED. Check http://" + ws.configuration().bindAddress().getHostAddress() + ":" +
					      ws.port() + "/status");
			      ws.whenShutdown().thenRun(() -> System.out.println("Oracle Node server was SHUTDOWN. Good bye!"));
		      })
		      .exceptionally(t -> {
			      System.err.println("Oracle Node startup failed: " + t.getMessage());
			      t.printStackTrace(System.err);
			      return null;
		      });
		return server;
	}

	private static Routing createRouting(Config config, OracleNode node) {
		return Routing.builder()
		              .get("/", new NodeStatusHandler(node))
		              .get("/start", new OracleNodeStartHandler(node))
		              .get("/stop", new OracleNodeStopHandler(node))
		              .get("/sub", new SubscriptionsListHandler(node))
		              .get("/sub/stop", new SubscriptionsStopHandler(node))
		              .get("/sub/start", new SubscriptionsStartHandler(node))
		              .get("/sub/update-code", new SubscriptionsCodeUpdateHandler(node))
		              .get("/sub/unsubscribe/{task}", new SubscriptionsRemoveHandler(node))
		              .get("/sub/subscribe/{task}", new SubscriptionsAddHandler(node))
		              .get("/requests", new RequestsListHandler(node))
		              .get("/requests/stop", new RequestsStopHandler(node))
		              .get("/requests/start", new RequestsStartHandler(node))
		              .get("/tasks", new TasksListHandler(node))
		              .get("/tasks/stop", new TasksStopHandler(node))
		              .get("/tasks/start", new TasksStartHandler(node))
		              .build();
	}

}
