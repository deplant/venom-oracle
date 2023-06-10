package tech.deplant.osiris.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;

/**
 * Java wrapper class for usage of <strong>TaskSubscription</strong> contract for Everscale blockchain.
 */
public record TaskSubscription(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Subscription {
  public TaskSubscription(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public TaskSubscription(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public TaskSubscription(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"lastTx_\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"cancel\",\"inputs\":[],\"outputs\":[]},{\"name\":\"updateLastTx\",\"inputs\":[{\"name\":\"lastTx_\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"_taskAddress\",\"inputs\":[],\"outputs\":[{\"name\":\"_taskAddress\",\"type\":\"address\"}]},{\"name\":\"_oracleAddress\",\"inputs\":[],\"outputs\":[{\"name\":\"_oracleAddress\",\"type\":\"address\"}]},{\"name\":\"_lastTx\",\"inputs\":[],\"outputs\":[{\"name\":\"_lastTx\",\"type\":\"uint64\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_taskAddress\",\"type\":\"address\"},{\"key\":2,\"name\":\"_oracleAddress\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_taskAddress\",\"type\":\"address\"},{\"name\":\"_oracleAddress\",\"type\":\"address\"},{\"name\":\"_lastTx\",\"type\":\"uint64\"}]}");
  }

  public FunctionHandle<Void> cancel() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "cancel", params, null);
  }

  public FunctionHandle<Void> updateLastTx(Long lastTx_) {
    Map<String, Object> params = Map.of("lastTx_", lastTx_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "updateLastTx", params, null);
  }

  public FunctionHandle<ResultOf_taskAddress> _taskAddress() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_taskAddress>(ResultOf_taskAddress.class, sdk(), address(), abi(), credentials(), "_taskAddress", params, null);
  }

  public FunctionHandle<ResultOf_oracleAddress> _oracleAddress() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_oracleAddress>(ResultOf_oracleAddress.class, sdk(), address(), abi(), credentials(), "_oracleAddress", params, null);
  }

  public FunctionHandle<ResultOf_lastTx> _lastTx() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_lastTx>(ResultOf_lastTx.class, sdk(), address(), abi(), credentials(), "_lastTx", params, null);
  }

  public record ResultOf_taskAddress(Address _taskAddress) {
  }

  public record ResultOf_oracleAddress(Address _oracleAddress) {
  }

  public record ResultOf_lastTx(Long _lastTx) {
  }
}
