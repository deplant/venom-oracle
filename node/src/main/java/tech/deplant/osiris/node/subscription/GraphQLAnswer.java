package tech.deplant.osiris.node.subscription;

import java.util.Map;

public record GraphQLAnswer(GraphQLData data) {

	public record GraphQLData(Map<String, Object>[] accounts) {}
}
