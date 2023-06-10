package tech.deplant.osiris.node.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public record JavaClient(HttpClient client) implements WebClient {


	public static JavaClient of() {
		return new JavaClient(HttpClient.newBuilder().build());
	}

	@Override
	public String postJSON(String uri, String json) {
		var request = HttpRequest.newBuilder().uri(URI.create(uri))
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.version(HttpClient.Version.HTTP_1_1)
				.header("Content-Type", "application/json")
				.header("Accept","application/json")
				//.header("Accept-Encoding","gzip, deflate")
				.build();
		try {
			final HttpResponse<String> response = client().send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
