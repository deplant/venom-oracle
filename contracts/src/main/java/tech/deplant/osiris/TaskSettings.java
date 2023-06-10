package tech.deplant.osiris;

import java.math.BigInteger;

public record TaskSettings(String body, String consensusType, Integer minValidators, BigInteger executionFee) {
}
