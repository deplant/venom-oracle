package tech.deplant.osiris.contract;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

public interface Validator extends Contract {


	FunctionHandle<Void> commit(Address taskAddress_, BigInteger commitHash_);

	FunctionHandle<Void> reveal(Address taskAddress_, TvmCell revealValue_,
	                            BigInteger revealSalt_);

	FunctionHandle<Void> requestRevealPhase(Address taskAddress_);

	FunctionHandle<Void> requestFinalPhase(Address taskAddress_);

	FunctionHandle<OracleValidator.ResultOfSubscribe> subscribe(Address taskAddress_, Long lastProcessedTx_);

	FunctionHandle<Void> unsubscribe(Address taskAddress_);

	FunctionHandle<Void> saveRequestTx(Address taskAddress_, Long lastProcessedTx_);

	FunctionHandle<OracleValidator.ResultOfGetSubscriptionCodeHash> getSubscriptionCodeHash();

	FunctionHandle<OracleValidator.ResultOf_subscriptionCode> _subscriptionCode();

	FunctionHandle<OracleValidator.ResultOf_name> _name();

	FunctionHandle<OracleValidator.ResultOf_logo> _logo();

	default BigInteger hash(TvmCell response, BigInteger salt) throws EverSdkException {
		return getCommitHash(response,salt).get().valueHash();
	}

	FunctionHandle<OracleValidator.ResultOfGetCommitHash> getCommitHash(TvmCell response_, BigInteger salt_);

	FunctionHandle<Void> setSubscriptionCode(TvmCell subscriptionCode_);

	default void updateSubscriptionCode(Tvc tvc) throws EverSdkException {
		setSubscriptionCode(tvc.codeCell(sdk())).call();
	}

}
