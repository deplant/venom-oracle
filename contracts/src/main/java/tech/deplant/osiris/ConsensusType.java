package tech.deplant.osiris;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Electors can be of various types depending on consensus details.
 * TaskFactory inner map saves code for all types with this enum as a key.
 *
 */
public enum ConsensusType {
	PRECISE(0), // Consensus is built around equal answer from all Oracles
	MEDIAN(1), // Consensus is built around medianized Schelling point
	RANDOM(2); // Consensus is built with VRF procedure
	private final int solidityInt;

	ConsensusType(int value) {
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

	public static ConsensusType ofValue(int value) {
		return ConsensusType.values()[value];
	}
}