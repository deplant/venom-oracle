package tech.deplant.osiris.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;

/**
 * Java wrapper class for usage of <strong>TestFaucet</strong> contract for Everscale blockchain.
 */
public record TestFaucet(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TestFaucet(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public TestFaucet(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public TestFaucet(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"token_\",\"type\":\"address\"},{\"name\":\"amount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"onTokenRootCallback\",\"inputs\":[{\"name\":\"tokenWallet_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"faucet\",\"inputs\":[{\"name\":\"doDeploy_\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"_token\",\"inputs\":[],\"outputs\":[{\"name\":\"_token\",\"type\":\"address\"}]},{\"name\":\"_tokenWallet\",\"inputs\":[],\"outputs\":[{\"name\":\"_tokenWallet\",\"type\":\"address\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_token\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_token\",\"type\":\"address\"},{\"name\":\"_tokenWallet\",\"type\":\"address\"},{\"name\":\"_amount\",\"type\":\"uint128\"}]}");
  }

  public FunctionHandle<Void> onTokenRootCallback(Address tokenWallet_) {
    Map<String, Object> params = Map.of("tokenWallet_", tokenWallet_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onTokenRootCallback", params, null);
  }

  public FunctionHandle<Void> faucet(Boolean doDeploy_) {
    Map<String, Object> params = Map.of("doDeploy_", doDeploy_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "faucet", params, null);
  }

  public FunctionHandle<ResultOf_token> _token() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_token>(ResultOf_token.class, sdk(), address(), abi(), credentials(), "_token", params, null);
  }

  public FunctionHandle<ResultOf_tokenWallet> _tokenWallet() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_tokenWallet>(ResultOf_tokenWallet.class, sdk(), address(), abi(), credentials(), "_tokenWallet", params, null);
  }

  public record ResultOf_token(Address _token) {
  }

  public record ResultOf_tokenWallet(Address _tokenWallet) {
  }
}
