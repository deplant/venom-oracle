package tech.deplant.osiris;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StorageAssetType {
	GAS(0),
	TOKEN(1);
	private final int solidityInt;

	StorageAssetType(int value) {
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

	public static StorageAssetType ofValue(int value) {
		return StorageAssetType.values()[value];
	}
}
