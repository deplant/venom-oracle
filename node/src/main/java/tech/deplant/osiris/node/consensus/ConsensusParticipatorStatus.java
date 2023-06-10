package tech.deplant.osiris.node.consensus;

public record ConsensusParticipatorStatus(boolean isEnabled) {
	public static ConsensusParticipatorStatus of(ConsensusParticipator consensusParticipator) {
		return new ConsensusParticipatorStatus(consensusParticipator.isEnabled());
	}
}
