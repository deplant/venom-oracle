package tech.deplant.osiris.node.validator;

import io.helidon.config.Config;
import tech.deplant.java4ever.framework.datatype.Address;

public record ValidatorConfig(String endpoint,
                              String address,
                              String token,
                              String publicKey,
                              String privateKey,
                              Long timeout) {

	public static ValidatorConfig of(final Config config) {
		return new ValidatorConfig(config.get("endpoint").asString().orElse(""),
		                           config.get("address").asString().orElse(Address.ZERO.makeAddrStd()),
		                           config.get("token").asString().orElse(Address.ZERO.makeAddrStd()),
		                           config.get("public-key").asString().orElse(""),
		                           config.get("secret-key").asString().orElse(""),
		                           config.get("timeout").asLong().orElse(50L));
	}

}
