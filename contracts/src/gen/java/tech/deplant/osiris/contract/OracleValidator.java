package tech.deplant.osiris.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>OracleValidator</strong> contract for Everscale blockchain.
 */
public record OracleValidator(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Validator {
  public OracleValidator(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public OracleValidator(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public OracleValidator(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"name_\",\"type\":\"string\"},{\"name\":\"logo_\",\"type\":\"string\"}],\"outputs\":[]},{\"name\":\"commit\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"commitHash_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"reveal\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"revealValue_\",\"type\":\"cell\"},{\"name\":\"revealSalt_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"requestRevealPhase\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"requestFinalPhase\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"refreshFeed\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"getCommitHash\",\"inputs\":[{\"name\":\"response_\",\"type\":\"cell\"},{\"name\":\"salt_\",\"type\":\"uint256\"}],\"outputs\":[{\"name\":\"valueHash\",\"type\":\"uint256\"}]},{\"name\":\"subscribe\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"lastProcessedTx_\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"subscriptionAddress\",\"type\":\"address\"}]},{\"name\":\"unsubscribe\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"saveRequestTx\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"lastProcessedTx_\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"setSubscriptionCode\",\"inputs\":[{\"name\":\"subscriptionCode_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"getSubscriptionCodeHash\",\"inputs\":[],\"outputs\":[{\"name\":\"codeHash\",\"type\":\"uint256\"}]},{\"name\":\"_subscriptionCode\",\"inputs\":[],\"outputs\":[{\"name\":\"_subscriptionCode\",\"type\":\"cell\"}]},{\"name\":\"_name\",\"inputs\":[],\"outputs\":[{\"name\":\"_name\",\"type\":\"string\"}]},{\"name\":\"_logo\",\"inputs\":[],\"outputs\":[{\"name\":\"_logo\",\"type\":\"string\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_subscriptionCode\",\"type\":\"cell\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_subscriptionCode\",\"type\":\"cell\"},{\"name\":\"_name\",\"type\":\"string\"},{\"name\":\"_logo\",\"type\":\"string\"}]}");
  }

  public FunctionHandle<Void> commit(Address taskAddress_, BigInteger commitHash_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "commitHash_", commitHash_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "commit", params, null);
  }

  public FunctionHandle<Void> reveal(Address taskAddress_, TvmCell revealValue_,
      BigInteger revealSalt_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "revealValue_", revealValue_, 
        "revealSalt_", revealSalt_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "reveal", params, null);
  }

  public FunctionHandle<Void> requestRevealPhase(Address taskAddress_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "requestRevealPhase", params, null);
  }

  public FunctionHandle<Void> requestFinalPhase(Address taskAddress_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "requestFinalPhase", params, null);
  }

  public FunctionHandle<Void> refreshFeed(Address taskAddress_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "refreshFeed", params, null);
  }

  public FunctionHandle<ResultOfGetCommitHash> getCommitHash(TvmCell response_, BigInteger salt_) {
    Map<String, Object> params = Map.of("response_", response_, 
        "salt_", salt_);
    return new FunctionHandle<ResultOfGetCommitHash>(ResultOfGetCommitHash.class, sdk(), address(), abi(), credentials(), "getCommitHash", params, null);
  }

  public FunctionHandle<ResultOfSubscribe> subscribe(Address taskAddress_, Long lastProcessedTx_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "lastProcessedTx_", lastProcessedTx_);
    return new FunctionHandle<ResultOfSubscribe>(ResultOfSubscribe.class, sdk(), address(), abi(), credentials(), "subscribe", params, null);
  }

  public FunctionHandle<Void> unsubscribe(Address taskAddress_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "unsubscribe", params, null);
  }

  public FunctionHandle<Void> saveRequestTx(Address taskAddress_, Long lastProcessedTx_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "lastProcessedTx_", lastProcessedTx_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "saveRequestTx", params, null);
  }

  public FunctionHandle<Void> setSubscriptionCode(TvmCell subscriptionCode_) {
    Map<String, Object> params = Map.of("subscriptionCode_", subscriptionCode_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setSubscriptionCode", params, null);
  }

  public FunctionHandle<ResultOfGetSubscriptionCodeHash> getSubscriptionCodeHash() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetSubscriptionCodeHash>(ResultOfGetSubscriptionCodeHash.class, sdk(), address(), abi(), credentials(), "getSubscriptionCodeHash", params, null);
  }

  public FunctionHandle<ResultOf_subscriptionCode> _subscriptionCode() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_subscriptionCode>(ResultOf_subscriptionCode.class, sdk(), address(), abi(), credentials(), "_subscriptionCode", params, null);
  }

  public FunctionHandle<ResultOf_name> _name() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_name>(ResultOf_name.class, sdk(), address(), abi(), credentials(), "_name", params, null);
  }

  public FunctionHandle<ResultOf_logo> _logo() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_logo>(ResultOf_logo.class, sdk(), address(), abi(), credentials(), "_logo", params, null);
  }

  public record ResultOfGetCommitHash(BigInteger valueHash) {
  }

  public record ResultOfSubscribe(Address subscriptionAddress) {
  }

  public record ResultOfGetSubscriptionCodeHash(BigInteger codeHash) {
  }

  public record ResultOf_subscriptionCode(TvmCell _subscriptionCode) {
  }

  public record ResultOf_name(String _name) {
  }

  public record ResultOf_logo(String _logo) {
  }
}
