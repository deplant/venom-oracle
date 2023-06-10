package tech.deplant.osiris.node.validator;

import tech.deplant.osiris.contract.Validator;

public record ValidatorStatus(String endpoint, String address, String publicKey) {
	public static ValidatorStatus of(Validator validator) {
		return new ValidatorStatus(validator.sdk().endpoints()[0], validator.address(), validator.credentials().publicKey());
	}
}
