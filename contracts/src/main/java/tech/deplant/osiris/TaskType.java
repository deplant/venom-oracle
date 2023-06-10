package tech.deplant.osiris;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskType {
	PRECISE_IMMEDIATE(0), // Consensus is built around equal answer from all Oracles
	MEDIAN_IMMEDIATE(1), // Consensus is built around medianized Schelling point
	PRECISE_FEED(2), // Consensus is built with VRF procedure

	MEDIAN_FEED(3);
	private final int solidityInt;

	TaskType(int value) {
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

	public static TaskType ofValue(int value) {
		return TaskType.values()[value];
	}
}