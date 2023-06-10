package tech.deplant.osiris.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Integer;
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
 * Java wrapper class for usage of <strong>BankRoot</strong> contract for Everscale blockchain.
 */
public record BankRoot(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public BankRoot(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public BankRoot(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public BankRoot(Sdk sdk, String address, Credentials credentials) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"storageOf\",\"inputs\":[{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"deployStorage\",\"inputs\":[{\"name\":\"deployWalletValue_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"deployStorageTo\",\"inputs\":[{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"deployWalletValue_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"setStorageCode\",\"inputs\":[{\"name\":\"storageCode_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"setFactory\",\"inputs\":[{\"name\":\"factory_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"_owner\",\"inputs\":[],\"outputs\":[{\"name\":\"_owner\",\"type\":\"address\"}]},{\"name\":\"_token\",\"inputs\":[],\"outputs\":[{\"name\":\"_token\",\"type\":\"address\"}]},{\"name\":\"_factory\",\"inputs\":[],\"outputs\":[{\"name\":\"_factory\",\"type\":\"address\"}]},{\"name\":\"_storageCode\",\"inputs\":[],\"outputs\":[{\"name\":\"_storageCode\",\"type\":\"cell\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_owner\",\"type\":\"address\"},{\"key\":2,\"name\":\"_token\",\"type\":\"address\"},{\"key\":3,\"name\":\"_factory\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_owner\",\"type\":\"address\"},{\"name\":\"_token\",\"type\":\"address\"},{\"name\":\"_factory\",\"type\":\"address\"},{\"name\":\"_storageCode\",\"type\":\"cell\"}]}");
  }

  public FunctionHandle<ResultOfStorageOf> storageOf(Address owner_, Integer type_) {
    Map<String, Object> params = Map.of("owner_", owner_, 
        "type_", type_);
    return new FunctionHandle<ResultOfStorageOf>(ResultOfStorageOf.class, sdk(), address(), abi(), credentials(), "storageOf", params, null);
  }

  public FunctionHandle<Void> deployStorage(BigInteger deployWalletValue_) {
    Map<String, Object> params = Map.of("deployWalletValue_", deployWalletValue_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "deployStorage", params, null);
  }

  public FunctionHandle<Void> deployStorageTo(Address owner_, Integer type_,
      BigInteger deployWalletValue_) {
    Map<String, Object> params = Map.of("owner_", owner_, 
        "type_", type_, 
        "deployWalletValue_", deployWalletValue_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "deployStorageTo", params, null);
  }

  public FunctionHandle<Void> setStorageCode(TvmCell storageCode_) {
    Map<String, Object> params = Map.of("storageCode_", storageCode_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setStorageCode", params, null);
  }

  public FunctionHandle<Void> setFactory(Address factory_) {
    Map<String, Object> params = Map.of("factory_", factory_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setFactory", params, null);
  }

  public FunctionHandle<ResultOf_owner> _owner() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_owner>(ResultOf_owner.class, sdk(), address(), abi(), credentials(), "_owner", params, null);
  }

  public FunctionHandle<ResultOf_token> _token() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_token>(ResultOf_token.class, sdk(), address(), abi(), credentials(), "_token", params, null);
  }

  public FunctionHandle<ResultOf_factory> _factory() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_factory>(ResultOf_factory.class, sdk(), address(), abi(), credentials(), "_factory", params, null);
  }

  public FunctionHandle<ResultOf_storageCode> _storageCode() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_storageCode>(ResultOf_storageCode.class, sdk(), address(), abi(), credentials(), "_storageCode", params, null);
  }

  public record ResultOfStorageOf(Address value0) {
  }

  public record ResultOf_owner(Address _owner) {
  }

  public record ResultOf_token(Address _token) {
  }

  public record ResultOf_factory(Address _factory) {
  }

  public record ResultOf_storageCode(TvmCell _storageCode) {
  }
}
