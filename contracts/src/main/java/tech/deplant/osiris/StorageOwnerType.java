package tech.deplant.osiris;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StorageOwnerType {
	CONSUMER_STORAGE(0),
	VALIDATOR_STORAGE(1),
	TASK_STORAGE(2);
	private final int solidityInt;

	StorageOwnerType(int value) {
		this.solidityInt = value;
	}

	/** As other contract enums, value() method should be annotated with JsonValue,
	 * so it will correctly serialized to integer.
	 *
	 * @return
	 */
	@JsonValue
	public int value() {
		return this.solidityInt;
	}

	public static StorageOwnerType ofValue(int value) {
		return StorageOwnerType.values()[value];
	}
}
