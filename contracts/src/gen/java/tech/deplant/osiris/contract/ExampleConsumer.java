package tech.deplant.osiris.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>ExampleConsumer</strong> contract for Everscale blockchain.
 */
public record ExampleConsumer(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public ExampleConsumer(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public ExampleConsumer(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public ExampleConsumer(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"calculatePreciseOnRequest\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"payerAddress_\",\"type\":\"address\"},{\"name\":\"params_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"calculateMedianizedOnRequest\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"payerAddress_\",\"type\":\"address\"},{\"name\":\"params_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"calculatePreciseFeed\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"payerAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"calculateMedianizedFeed\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"payerAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"getPreciseResult\",\"inputs\":[],\"outputs\":[{\"name\":\"value0\",\"type\":\"cell\"}]},{\"name\":\"getMedianizedResult\",\"inputs\":[],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint128\"}]},{\"name\":\"preciseCallback\",\"inputs\":[{\"name\":\"response_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"medianizedCallback\",\"inputs\":[{\"name\":\"response_\",\"type\":\"uint128\"}],\"outputs\":[]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_preciseResult\",\"type\":\"cell\"},{\"name\":\"_medianizedResult\",\"type\":\"uint128\"},{\"name\":\"_currentTask\",\"type\":\"address\"}]}");
  }

  public FunctionHandle<Void> calculatePreciseOnRequest(Address taskAddress_, Address payerAddress_,
      TvmCell params_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "payerAddress_", payerAddress_, 
        "params_", params_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "calculatePreciseOnRequest", params, null);
  }

  public FunctionHandle<Void> calculateMedianizedOnRequest(Address taskAddress_,
      Address payerAddress_, TvmCell params_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "payerAddress_", payerAddress_, 
        "params_", params_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "calculateMedianizedOnRequest", params, null);
  }

  public FunctionHandle<Void> calculatePreciseFeed(Address taskAddress_, Address payerAddress_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "payerAddress_", payerAddress_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "calculatePreciseFeed", params, null);
  }

  public FunctionHandle<Void> calculateMedianizedFeed(Address taskAddress_, Address payerAddress_) {
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "payerAddress_", payerAddress_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "calculateMedianizedFeed", params, null);
  }

  public FunctionHandle<ResultOfGetPreciseResult> getPreciseResult() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetPreciseResult>(ResultOfGetPreciseResult.class, sdk(), address(), abi(), credentials(), "getPreciseResult", params, null);
  }

  public FunctionHandle<ResultOfGetMedianizedResult> getMedianizedResult() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetMedianizedResult>(ResultOfGetMedianizedResult.class, sdk(), address(), abi(), credentials(), "getMedianizedResult", params, null);
  }

  public FunctionHandle<Void> preciseCallback(TvmCell response_) {
    Map<String, Object> params = Map.of("response_", response_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "preciseCallback", params, null);
  }

  public FunctionHandle<Void> medianizedCallback(BigInteger response_) {
    Map<String, Object> params = Map.of("response_", response_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "medianizedCallback", params, null);
  }

  public record ResultOfGetPreciseResult(TvmCell value0) {
  }

  public record ResultOfGetMedianizedResult(BigInteger value0) {
  }
}
