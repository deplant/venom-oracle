package tech.deplant.osiris;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Election phases for commit-reveal scheme
 *
 */
public enum ElectionPhase {
	FAILURE(0),
	READY(1),
	COMMIT(2),
	REVEAL(3),
	SUCCESS(4);
	private final int solidityInt;

	ElectionPhase(int value) {
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

	public static ElectionPhase ofValue(int value) {
		return ElectionPhase.values()[value];
	}
}
