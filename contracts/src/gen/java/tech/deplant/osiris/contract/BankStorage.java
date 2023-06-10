package tech.deplant.osiris.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
import java.lang.Integer;
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
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

/**
 * Java wrapper class for usage of <strong>BankStorage</strong> contract for Everscale blockchain.
 */
public record BankStorage(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public BankStorage(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public BankStorage(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public BankStorage(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"initialDeposit_\",\"type\":\"uint128\"},{\"name\":\"token_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onTokenRootCallback\",\"inputs\":[{\"name\":\"tokenWallet_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"addAllowance\",\"inputs\":[{\"name\":\"consumer_\",\"type\":\"address\"},{\"name\":\"task_\",\"type\":\"address\"},{\"name\":\"valueAmount_\",\"type\":\"uint128\"},{\"name\":\"tokenAmount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"removeAllowance\",\"inputs\":[{\"name\":\"consumer_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"transfer\",\"inputs\":[{\"name\":\"recipient_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"valueAmount_\",\"type\":\"uint128\"},{\"name\":\"tokenAmount_\",\"type\":\"uint128\"},{\"name\":\"notifyOwner_\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"requestTransfer\",\"inputs\":[{\"name\":\"payer_\",\"type\":\"address\"},{\"name\":\"task_\",\"type\":\"address\"},{\"name\":\"consumer_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"valueAmount_\",\"type\":\"uint128\"},{\"name\":\"tokenAmount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"redeployTokenWallet\",\"inputs\":[],\"outputs\":[]},{\"name\":\"depositValue\",\"inputs\":[{\"name\":\"amount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"withdrawValue\",\"inputs\":[{\"name\":\"amount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"withdrawToken\",\"inputs\":[{\"name\":\"amount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"acceptRequestTransfer\",\"inputs\":[{\"name\":\"task_\",\"type\":\"address\"},{\"name\":\"consumer_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"valueAmount_\",\"type\":\"uint128\"},{\"name\":\"tokenAmount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"onAcceptTokensTransfer\",\"inputs\":[{\"name\":\"value0\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint128\"},{\"name\":\"sender\",\"type\":\"address\"},{\"name\":\"value3\",\"type\":\"address\"},{\"name\":\"value4\",\"type\":\"address\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"_bank\",\"inputs\":[],\"outputs\":[{\"name\":\"_bank\",\"type\":\"address\"}]},{\"name\":\"_owner\",\"inputs\":[],\"outputs\":[{\"name\":\"_owner\",\"type\":\"address\"}]},{\"name\":\"_type\",\"inputs\":[],\"outputs\":[{\"name\":\"_type\",\"type\":\"uint8\"}]},{\"name\":\"_valueBalance\",\"inputs\":[],\"outputs\":[{\"name\":\"_valueBalance\",\"type\":\"uint128\"}]},{\"name\":\"_tokenBalance\",\"inputs\":[],\"outputs\":[{\"name\":\"_tokenBalance\",\"type\":\"uint128\"}]},{\"name\":\"_tokenWallet\",\"inputs\":[],\"outputs\":[{\"name\":\"_tokenWallet\",\"type\":\"address\"}]},{\"name\":\"_token\",\"inputs\":[],\"outputs\":[{\"name\":\"_token\",\"type\":\"address\"}]},{\"name\":\"_lockedBalances\",\"inputs\":[],\"outputs\":[{\"name\":\"_lockedBalances\",\"type\":\"map(uint64,uint128)\"}]},{\"name\":\"_lockedTokens\",\"inputs\":[],\"outputs\":[{\"name\":\"_lockedTokens\",\"type\":\"map(uint64,uint128)\"}]},{\"name\":\"_allowanceMap\",\"inputs\":[],\"outputs\":[{\"name\":\"_allowanceMap\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"task\",\"type\":\"address\"},{\"name\":\"maxValue\",\"type\":\"uint128\"},{\"name\":\"maxToken\",\"type\":\"uint256\"}]}]}],\"events\":[{\"name\":\"depositIncrease\",\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"valueAmount\",\"type\":\"uint128\"},{\"name\":\"tokenAmount\",\"type\":\"uint128\"},{\"name\":\"assetType\",\"type\":\"uint8\"}]},{\"name\":\"depositDecrease\",\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"},{\"name\":\"valueAmount\",\"type\":\"uint128\"},{\"name\":\"tokenAmount\",\"type\":\"uint128\"},{\"name\":\"assetType\",\"type\":\"uint8\"}]}],\"data\":[{\"key\":1,\"name\":\"_bank\",\"type\":\"address\"},{\"key\":2,\"name\":\"_owner\",\"type\":\"address\"},{\"key\":3,\"name\":\"_type\",\"type\":\"uint8\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_bank\",\"type\":\"address\"},{\"name\":\"_owner\",\"type\":\"address\"},{\"name\":\"_type\",\"type\":\"uint8\"},{\"name\":\"_valueBalance\",\"type\":\"uint128\"},{\"name\":\"_tokenBalance\",\"type\":\"uint128\"},{\"name\":\"_tokenWallet\",\"type\":\"address\"},{\"name\":\"_token\",\"type\":\"address\"},{\"name\":\"_lockedBalances\",\"type\":\"map(uint64,uint128)\"},{\"name\":\"_lockedTokens\",\"type\":\"map(uint64,uint128)\"},{\"name\":\"_allowanceMap\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"task\",\"type\":\"address\"},{\"name\":\"maxValue\",\"type\":\"uint128\"},{\"name\":\"maxToken\",\"type\":\"uint256\"}]}]}");
  }

  public FunctionHandle<Void> onTokenRootCallback(Address tokenWallet_) {
    Map<String, Object> params = Map.of("tokenWallet_", tokenWallet_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onTokenRootCallback", params, null);
  }

  public FunctionHandle<Void> addAllowance(Address consumer_, Address task_,
      BigInteger valueAmount_, BigInteger tokenAmount_) {
    Map<String, Object> params = Map.of("consumer_", consumer_, 
        "task_", task_, 
        "valueAmount_", valueAmount_, 
        "tokenAmount_", tokenAmount_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "addAllowance", params, null);
  }

  public FunctionHandle<Void> removeAllowance(Address consumer_) {
    Map<String, Object> params = Map.of("consumer_", consumer_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "removeAllowance", params, null);
  }

  public FunctionHandle<Void> transfer(Address recipient_, Integer type_, BigInteger valueAmount_,
      BigInteger tokenAmount_, Boolean notifyOwner_) {
    Map<String, Object> params = Map.of("recipient_", recipient_, 
        "type_", type_, 
        "valueAmount_", valueAmount_, 
        "tokenAmount_", tokenAmount_, 
        "notifyOwner_", notifyOwner_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "transfer", params, null);
  }

  public FunctionHandle<Void> requestTransfer(Address payer_, Address task_, Address consumer_,
      Integer type_, BigInteger valueAmount_, BigInteger tokenAmount_) {
    Map<String, Object> params = Map.of("payer_", payer_, 
        "task_", task_, 
        "consumer_", consumer_, 
        "type_", type_, 
        "valueAmount_", valueAmount_, 
        "tokenAmount_", tokenAmount_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "requestTransfer", params, null);
  }

  public FunctionHandle<Void> redeployTokenWallet() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "redeployTokenWallet", params, null);
  }

  public FunctionHandle<Void> depositValue(BigInteger amount_) {
    Map<String, Object> params = Map.of("amount_", amount_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "depositValue", params, null);
  }

  public FunctionHandle<Void> withdrawValue(BigInteger amount_) {
    Map<String, Object> params = Map.of("amount_", amount_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "withdrawValue", params, null);
  }

  public FunctionHandle<Void> withdrawToken(BigInteger amount_) {
    Map<String, Object> params = Map.of("amount_", amount_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "withdrawToken", params, null);
  }

  public FunctionHandle<Void> acceptRequestTransfer(Address task_, Address consumer_, Integer type_,
      BigInteger valueAmount_, BigInteger tokenAmount_) {
    Map<String, Object> params = Map.of("task_", task_, 
        "consumer_", consumer_, 
        "type_", type_, 
        "valueAmount_", valueAmount_, 
        "tokenAmount_", tokenAmount_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "acceptRequestTransfer", params, null);
  }

  public FunctionHandle<Void> onAcceptTokensTransfer(Address value0, BigInteger amount,
      Address sender, Address value3, Address value4, TvmCell payload) {
    Map<String, Object> params = Map.of("value0", value0, 
        "amount", amount, 
        "sender", sender, 
        "value3", value3, 
        "value4", value4, 
        "payload", payload);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onAcceptTokensTransfer", params, null);
  }

  public FunctionHandle<ResultOf_bank> _bank() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_bank>(ResultOf_bank.class, sdk(), address(), abi(), credentials(), "_bank", params, null);
  }

  public FunctionHandle<ResultOf_owner> _owner() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_owner>(ResultOf_owner.class, sdk(), address(), abi(), credentials(), "_owner", params, null);
  }

  public FunctionHandle<ResultOf_type> _type() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_type>(ResultOf_type.class, sdk(), address(), abi(), credentials(), "_type", params, null);
  }

  public FunctionHandle<ResultOf_valueBalance> _valueBalance() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_valueBalance>(ResultOf_valueBalance.class, sdk(), address(), abi(), credentials(), "_valueBalance", params, null);
  }

  public FunctionHandle<ResultOf_tokenBalance> _tokenBalance() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_tokenBalance>(ResultOf_tokenBalance.class, sdk(), address(), abi(), credentials(), "_tokenBalance", params, null);
  }

  public FunctionHandle<ResultOf_tokenWallet> _tokenWallet() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_tokenWallet>(ResultOf_tokenWallet.class, sdk(), address(), abi(), credentials(), "_tokenWallet", params, null);
  }

  public FunctionHandle<ResultOf_token> _token() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_token>(ResultOf_token.class, sdk(), address(), abi(), credentials(), "_token", params, null);
  }

  public FunctionHandle<ResultOf_lockedBalances> _lockedBalances() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_lockedBalances>(ResultOf_lockedBalances.class, sdk(), address(), abi(), credentials(), "_lockedBalances", params, null);
  }

  public FunctionHandle<ResultOf_lockedTokens> _lockedTokens() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_lockedTokens>(ResultOf_lockedTokens.class, sdk(), address(), abi(), credentials(), "_lockedTokens", params, null);
  }

  public FunctionHandle<ResultOf_allowanceMap> _allowanceMap() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_allowanceMap>(ResultOf_allowanceMap.class, sdk(), address(), abi(), credentials(), "_allowanceMap", params, null);
  }

  public record ResultOf_bank(Address _bank) {
  }

  public record ResultOf_owner(Address _owner) {
  }

  public record ResultOf_type(Integer _type) {
  }

  public record ResultOf_valueBalance(BigInteger _valueBalance) {
  }

  public record ResultOf_tokenBalance(BigInteger _tokenBalance) {
  }

  public record ResultOf_tokenWallet(Address _tokenWallet) {
  }

  public record ResultOf_token(Address _token) {
  }

  public record ResultOf_lockedBalances(Map<Long, BigInteger> _lockedBalances) {
  }

  public record ResultOf_lockedTokens(Map<Long, BigInteger> _lockedTokens) {
  }

  public record ResultOf_allowanceMap(Map<Address, Map<String, Object>> _allowanceMap) {
  }
}
