package tech.deplant.osiris.node.tst;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DevelopmentTests {
//
//	public final static ObjectMapper JSON_MAPPER = JsonMapper.builder() // or different mapper for other format
//	                                                         .addModule(new ParameterNamesModule())
//	                                                         .addModule(new Jdk8Module())
//	                                                         .addModule(new JavaTimeModule())
//	                                                         // and possibly other configuration, modules, then:
//	                                                         .build();
//	private static final Logger log = LogManager.getLogger(DevelopmentTests.class);
//
//	static public String shuffleString(final String data, final long shuffleSeed) {
//		if (shuffleSeed != 0) {
//			final Random rnd = new Random(shuffleSeed);
//			final StringBuilder sb = new StringBuilder(data);
//			int n = data.length();
//			while (n > 1) {
//				final int k = rnd.nextInt(n--);
//				final char t = sb.charAt(n);
//				sb.setCharAt(n, sb.charAt(k));
//				sb.setCharAt(k, t);
//			}
//			return sb.toString();
//		} else {
//			return data;
//		}
//	}
//
//	public static String httpRequest(final String url, final String query, final String method) throws IOException, InterruptedException {
//		//example url = "https://devnet.evercloud.dev/graphql?query="
//
//		final String uriString = url + URLEncoder.encode(query, StandardCharsets.UTF_8);
//		//System.out.println(uriString);
//		HttpClient client = HttpClient.newBuilder()
//		                              .version(HttpClient.Version.HTTP_1_1)
//		                              .connectTimeout(Duration.ofSeconds(15))
//		                              .followRedirects(HttpClient.Redirect.NEVER) // I think redirects should be forbidden as its hard to estimate their cost and to prevent using oracle as a botnet
//		                              .build();
//		HttpResponse<String> response = null;
//		Map<String, String> headersChecked;
//
//		if (method.equals("GET")) {
//
//			final var builder = HttpRequest.newBuilder()
//			                               .GET()
//			                               .uri(URI.create(uriString));
//
//			if (true) {//if (null == this.headers) {
//				headersChecked = Map.of("Accept", "application/json");
//			} else {
//				//headersChecked = this.headers;
//			}
//			headersChecked.forEach(builder::header);
//			final HttpRequest request = builder.build();
//
//			response = client.send(
//					request,
//					HttpResponse.BodyHandlers.ofString() // String
//					//InputStream
//					//File
//			);
//		} else if (method.equals("POST")) {
//			client = HttpClient.newHttpClient();
//
//			final HttpRequest request = HttpRequest.newBuilder()
//			                                       .uri(URI.create(url))
//			                                       .POST(HttpRequest.BodyPublishers.ofString(query))
//			                                       .version(HttpClient.Version.HTTP_1_1)
//			                                       .header("Content-Type", "application/json")
//			                                       //.header("Accept", "application/json")
//			                                       //.header("Accept-Encoding","gzip, deflate")
//			                                       .build();
//			//sync
//			response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		}
//		if (response.statusCode() != 200) {
//			throw new RuntimeException("Failed : HTTP Error code : "
//			                           + response.statusCode());
//		}
//		return response.body();
//	}
//
//	@Test
//	public void testCron() throws InterruptedException {
//		//TODO check scheduling
//		new Crontab();
//		Thread.sleep(Duration.ofSeconds(90));
//	}
//
//	@Test
//	public void testTDD() {
//		System.out.println();
//
//	}
//
//	@Test
//	@DisplayName("10_000 Tasks Main Loop Test")
//	public void testShit() {
//
//		final int rounds = 1_000_000; // define tasks count
//
//		try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
//
//			// Main Cycle of the Node
//			IntStream.range(0, rounds).forEach(i ->
//					                                   executor.submit(() ->
//
//					                                                   {
//						                                                   try {
//							                                                   Thread.sleep(1000);
//						                                                   } catch (final InterruptedException e) {
//							                                                   throw new RuntimeException(e);
//						                                                   }
//					                                                   }));
//		}
//	}
//
////	@Test
////	@DisplayName("2 Tasks Get Http Loop Test")
////	public void testHttp() {
////
////		log.info("Timestamp (Node started): " + Instant.now().toString());
////
////		var className = "node.HttpRequest";
////
////		Runnable task = () -> {
////			try {
////				Class.forName(className).getDeclaredConstructor().newInstance();
////			} catch (ClassNotFoundException ex) {
////				System.out.println("Class " + className + " not found!");
////
////			} catch (ArrayIndexOutOfBoundsException rr) {
////				System.out.println("Missing argument!");
////
////			} catch (Throwable any) {
////				System.out.println("Unexpected error! " + any);
////			}
////		};
////
////		int rounds = 2; // define tasks count
////
////		Tasks tasks = new Tasks();
////		IntStream.range(0, rounds).forEach(i -> tasks.addTask(task));
////
////		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
////
////			log.info("Timestamp (Task Cycle Started): " + Instant.now().toString());
////
////			// Main Cycle of the Node
////			while (true) {
////				if (tasks.hasNewTask()) {
////					executor.submit(tasks.getNextTask());
////				} else {
////					//Thread.sleep(1000);
////					//logger.debug("waiting tasks");
////					break;
////				}
////			}
////		} finally {
////			log.info("Timestamp (Task Cycle Ended): " + Instant.now().toString());
////		}
////	}
//
//	@Test
//	public void testDeterministic() {
//		System.out.println(shuffleString("vasay mul ramu", 126L));
//	}
//
//	@Test
//	public void testParseContract() throws IOException, InterruptedException, EverSdkException {
//		final String url = "https://devnet.evercloud.dev/graphql?query=";
//
//		final String accountsContract = "{\n" +
//		                                "  accounts(\n" +
//		                                "    filter: {\n" +
//		                                "      code_hash: {\n" +
//		                                "        eq: \"e8801282233f38ad17fead83aeac1820e40598de98675106b0fd91d9524e9141\"\n" +
//		                                "      }\n" +
//		                                "      acc_type: { eq: 1 }\n" +
//		                                "    }\n" +
//		                                "    orderBy: { direction: ASC, path: \"last_paid\" }\n" +
//		                                "    limit: 5\n" +
//		                                "  ) {\n" +
//		                                "    id\n" +
//		                                "    balance\n" +
//		                                "    last_trans_lt\n" +
//		                                "    boc\n" +
//		                                "  }\n" +
//		                                "}";
//		final var dataString = httpRequest(url, accountsContract, "GET");
//		final var str = JSON_MAPPER.readTree(dataString);
//		final var str2 = str.at("/data/accounts").elements();
//		final List<String> lst = new ArrayList<String>();
//		str2.forEachRemaining(elem -> lst.add(elem.get("id").asText()));
//		//System.out.println(str);
//		final Sdk sdk = Sdk.builder()
//		                   .networkEndpoints("https://devnet.evercloud.dev/032a23e8f6254ca0b4ae4046819e7ac1/graphql")
//		                   .timeout(50L)
//		                   .build(JavaLibraryPathLoader.TON_CLIENT);
//
//		final ContractAbi abi = Tip31RootTemplate.DEFAULT_ABI();
//
//		for (int i = 0; i < lst.size(); i++) {
//			//System.out.println(lst.get(i));
//			final OwnedContract contract = new OwnedContract(
//					sdk,
//					lst.get(i),
//					abi,
//					Credentials.RANDOM(sdk)
//			);
//			final var inputs = new HashMap<String, Object>();
//			inputs.put("answerId", "0");
//			log.warn("TIP3 Token Name: " + contract.runGetter("name", inputs, null).get("value0").toString());
//			log.warn(
//					"TIP3 Token Root Owner: " +
//					contract.runGetter("rootOwner", inputs, null).get("value0").toString());
//		}
//	}
//
//	@Test
//	public void testParseContractGet() throws IOException, InterruptedException, EverSdkException {
//
//		// 0 Run getter getSubscriptionCodeHash on validator to get code hash
//
//		// 1 Find all subscriptions by code_hash
//
//		final String url = "http://80.78.241.3/graphql?query=";
//
//		final String accountsContract = "{\n" +
//		                                "  accounts(\n" +
//		                                "    filter: {\n" +
//		                                "      code_hash: {\n" +
//		                                "        eq: \"95f666f87b4de56ec7118b1a100222dfdc0367927f0a74bca965b16ef8591396\"\n" +
//		                                "      }\n" +
//		                                "      acc_type: { eq: 1 }\n" +
//		                                "    }\n" +
//		                                "    orderBy: { direction: ASC, path: \"last_paid\" }\n" +
//		                                "    limit: 5\n" +
//		                                "  ) {\n" +
//		                                "    id\n" +
//		                                "    balance\n" +
//		                                "    last_trans_lt\n" +
//		                                "    boc\n" +
//		                                "  }\n" +
//		                                "}";
//
//		final var dataString = httpRequest(url, accountsContract, "GET");
//		System.out.println(dataString);
//		final var str = JSON_MAPPER.readTree(dataString);
//		final var str2 = str.at("/data/accounts").elements();
//		final List<String> lst = new ArrayList<String>();
//		str2.forEachRemaining(elem -> lst.add(elem.get("id").asText()));
//
//		final Sdk sdk = Sdk.builder()
//		                   .networkEndpoints("http://80.78.241.3/graphql") //? /graphql
//		                   .timeout(50L)
//		                   .build(JavaLibraryPathLoader.TON_CLIENT);
//
//		final var abi = TaskSubscriptionTemplate.DEFAULT_ABI();
//
//		// 2 Iterate all ids from select and make contracts
//
//		for (int i = 0; i < lst.size(); i++) {
//			//System.out.println(lst.get(i));
//			final OwnedContract contract = new OwnedContract(
//					sdk,
//					lst.get(i),
//					abi,
//					new Credentials("b23e0be4ec7ed5424e12dcd5a449d34cb2af6d9f546efe3974cee22e96425ee5",
//					                "c7d3ea9330b7d7ee1fa28f06f87d2784c3693acbf6b219435c41a02ba7d5254b")
//			);
//
//			// 3 Run _taskAddress getters on each contract
//
//			final var inputs = new HashMap<String, Object>();
//			//inputs.put("taskAddress_", new Address("0:42eeebfc21e2a162691684a3e16c309908c67100ee279d540c697662e7c1ea76"));
//			log.warn(
//					"_taskAddress: " +
//					contract.runGetter("_taskAddress", inputs, null).get("_taskAddress").toString());
//			// 4 Put all taskAddresses to subscribedTasks map (replace old)
//		}
//	}
//
//	@Test
//	public void testParseMessagesWithInAddressPost() throws IOException, InterruptedException {
//		final String url = "http://80.78.241.3/graphql";
//		final String method = "POST";
//		final List<String> paramsIn = new ArrayList<>();
//
//		paramsIn.add("src:{ in: [");
//		for (int i = 0; i < 1000; i++) {
//			paramsIn.add("\"0:96700c8f27bd922e72f8a3e03a6b08d6e1fe44ea44af416fe1535d92b907a038\",");
//			//System.out.println(paramsIn.get(i));
//		}
//		paramsIn.add("]}");
//
//		final StringBuilder requestStr = new StringBuilder();
//		for (int j = 0; j <= paramsIn.size() - 1; j++) {
//			requestStr.append(paramsIn.get(j));
//		}
//		final String accountsContract = "{messages(filter: {msg_type: {eq:2}," + requestStr + "}) { id body boc }}";
//
//		final String jsonRequest = "{\"query\":" + JSON_MAPPER.writeValueAsString(accountsContract) + "}";
////		System.out.println(jsonRequest);
//
////		String json = "{\"query\":\"{messages(filter: {msg_type: {eq:2},src:{ in: [\\\"0:96700c8f27bd922e72f8a3e03a6b08d6e1fe44ea44af416fe1535d92b907a038\\\",\\\"0:96700c8f27bd922e72f8a3e03a6b08d6e1fe44ea44af416fe1535d92b907a038\\\",]}}) { id body boc }}\"}";
////		System.out.println("2 - " + json);
//
//		final var dataString = httpRequest(url, jsonRequest, method);
//		System.out.println(dataString);
//
//	}
//
//	@Test
//	public void testHttpPost() throws InterruptedException, IOException, ExecutionException {
//
//		final String str = "{\"query\":\"{  messages(   filter: {msg_type: {eq: 2}, src: {in: [\\\"0:96700c8f27bd922e72f8a3e03a6b08d6e1fe44ea44af416fe1535d92b907a038\\\", \\\"0:96700c8f27bd922e72f8a3e03a6b08d6e1fe44ea44af416fe1535d92b907a038\\\", \\\"0:96700c8f27bd922e72f8a3e03a6b08d6e1fe44ea44af416fe1535d92b907a038\\\"]}}  ) {id    body    boc  }}\"}";
//
//		final HttpClient client = HttpClient.newHttpClient();
//		final HttpRequest request = HttpRequest.newBuilder()
//		                                       .uri(URI.create("http://80.78.241.3/graphql"))
//		                                       .POST(HttpRequest.BodyPublishers.ofString(str))
//		                                       .version(HttpClient.Version.HTTP_1_1)
//		                                       .header("Content-Type", "application/json")
//		                                       //.header("Accept","application/json")
//		                                       //.header("Accept-Encoding","gzip, deflate")
//		                                       .build();
//		//sync
//		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println(response.statusCode());
//		System.out.println(response.body());
//
//		//async
////		CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
////		HttpResponse<String> response = futureResponse.get();
////
////		System.out.println(response.statusCode());
////		System.out.println(response.body());
//
//	}
//
	@Test
	public void test_post_cexio() throws IOException, InterruptedException {
		final HttpClient client = HttpClient.newHttpClient();
		final HttpRequest request = HttpRequest.newBuilder()
		                                       .uri(URI.create("https://cex.io/api/convert/EVER/USD"))
		                                       .POST(HttpRequest.BodyPublishers.ofString("{\"amnt\":\"1.0\"}"))
		                                       .version(HttpClient.Version.HTTP_1_1)
		                                       .header("Content-Type", "application/json")
		                                       //.header("Accept","application/json")
		                                       //.header("Accept-Encoding","gzip, deflate")
		                                       .build();
		//sync
		final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(response.statusCode());
		System.out.println(response.body());

	}
//
////	@Test
////	public void find_hash_code_post() throws IOException, InterruptedException {
////		var url = "http://80.78.241.3/graphql";
////		var method = "POST";
////
////		String accountsRequest = "{\n" +
////		                         "  accounts(\n" +
////		                         "    filter: {\n" +
////		                         "      code_hash: {\n" +
////		                         "        eq: \"" +
////		                         "95f666f87b4de56ec7118b1a100222dfdc0367927f0a74bca965b16ef8591396" +
////		                         "\"\n" +
////		                         "      }\n" +
////		                         "      acc_type: { eq: 1 }\n" +
////		                         "    }\n" +
////		                         "    orderBy: { direction: ASC, path: \"last_paid\" }\n" +
////		                         "    limit: 5\n" +
////		                         "  ) {\n" +
////		                         "    id\n" +
////		                         "    balance\n" +
////		                         "    last_trans_lt\n" +
////		                         "    boc\n" +
////		                         "  }\n" +
////		                         "}";
////
////		String jsonRequest = "{\"query\":" + JSON_MAPPER.writeValueAsString(accountsRequest) + "}";
////
////		var dataString = HttpAction.httpRequest(url, jsonRequest, method);
////
////		var str = JSON_MAPPER.readTree(dataString);
////		var str2 = str.at("/data/accounts").elements();
////		List<String> lst = new ArrayList<String>();
////		str2.forEachRemaining(elem -> lst.add(elem.get("id").asText()));
////		for (int i = 0; i < lst.size() - 1; i++) {
////			System.out.println(lst.get(i));
////			System.out.println(lst.get(i).substring(2));
////		}
////	}
//
//	@Test
//	public void nodeTests() throws TemplateProcessingException, IOException, InterruptedException {
//		final ConfigSource configSource = ConfigSources.classpath("application.properties").build();
//		//log.debug(configSource.toString());
//		final Config config = Config.builder().sources(configSource).build();
//		final var node = new ValidatorNode(config.get("oseeris"));
//		//log.debug(node.validatorKeys().publicKey() + " " + node.validatorKeys().secretKey());
//		//node.updateSubscribeTask();
//		node.pingEvents();
//	}
//
//	@Test
//	public void bigIntegerTest() {
//		final var n = new BigInteger("9999999999999999999");
//		final var rand = new Random();
//		BigInteger result = new BigInteger(n.bitLength(), rand);
//		while (result.compareTo(n) >= 0) {
//			result = new BigInteger(n.bitLength(), rand);
//		}
//		System.out.println(result);
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//		System.out.println(new BigInteger(256, new Random()));
//
//	}

}

