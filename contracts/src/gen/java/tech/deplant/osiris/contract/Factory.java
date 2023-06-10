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
 * Java wrapper class for usage of <strong>Factory</strong> contract for Everscale blockchain.
 */
public record Factory(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public Factory(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public Factory(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public Factory(Sdk sdk, String address, Credentials credentials) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getTaskAddress\",\"inputs\":[{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"name_\",\"type\":\"string\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"setMinStake\",\"inputs\":[{\"name\":\"minStake_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"setBank\",\"inputs\":[{\"name\":\"bank_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"setTokenRoot\",\"inputs\":[{\"name\":\"tokenRoot_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"setTaskCode\",\"inputs\":[{\"name\":\"respType_\",\"type\":\"uint8\"},{\"name\":\"taskCode_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"acceptFactorySettingsUpdate\",\"inputs\":[],\"outputs\":[]},{\"name\":\"publishCustomTask\",\"inputs\":[{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"name_\",\"type\":\"string\"},{\"name\":\"body_\",\"type\":\"string\"},{\"name\":\"minValidators_\",\"type\":\"uint32\"},{\"name\":\"executionFee_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"_defaultSettings\",\"inputs\":[],\"outputs\":[{\"name\":\"_defaultSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}]},{\"name\":\"_taskCodes\",\"inputs\":[],\"outputs\":[{\"name\":\"_taskCodes\",\"type\":\"map(uint8,cell)\"}]},{\"name\":\"_bank\",\"inputs\":[],\"outputs\":[{\"name\":\"_bank\",\"type\":\"address\"}]},{\"name\":\"_token\",\"inputs\":[],\"outputs\":[{\"name\":\"_token\",\"type\":\"address\"}]},{\"name\":\"_bankStorageCode\",\"inputs\":[],\"outputs\":[{\"name\":\"_bankStorageCode\",\"type\":\"cell\"}]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_defaultSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]},{\"name\":\"_taskCodes\",\"type\":\"map(uint8,cell)\"},{\"name\":\"_bank\",\"type\":\"address\"},{\"name\":\"_token\",\"type\":\"address\"},{\"name\":\"_bankStorageCode\",\"type\":\"cell\"}]}");
  }

  public FunctionHandle<ResultOfGetTaskAddress> getTaskAddress(Integer type_, Address owner_,
      String name_) {
    Map<String, Object> params = Map.of("type_", type_, 
        "owner_", owner_, 
        "name_", name_);
    return new FunctionHandle<ResultOfGetTaskAddress>(ResultOfGetTaskAddress.class, sdk(), address(), abi(), credentials(), "getTaskAddress", params, null);
  }

  public FunctionHandle<Void> setMinStake(BigInteger minStake_) {
    Map<String, Object> params = Map.of("minStake_", minStake_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setMinStake", params, null);
  }

  public FunctionHandle<Void> setBank(Address bank_) {
    Map<String, Object> params = Map.of("bank_", bank_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setBank", params, null);
  }

  public FunctionHandle<Void> setTokenRoot(Address tokenRoot_) {
    Map<String, Object> params = Map.of("tokenRoot_", tokenRoot_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setTokenRoot", params, null);
  }

  public FunctionHandle<Void> setTaskCode(Integer respType_, TvmCell taskCode_) {
    Map<String, Object> params = Map.of("respType_", respType_, 
        "taskCode_", taskCode_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setTaskCode", params, null);
  }

  public FunctionHandle<Void> acceptFactorySettingsUpdate() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "acceptFactorySettingsUpdate", params, null);
  }

  public FunctionHandle<Void> publishCustomTask(Integer type_, String name_, String body_,
      Integer minValidators_, BigInteger executionFee_) {
    Map<String, Object> params = Map.of("type_", type_, 
        "name_", name_, 
        "body_", body_, 
        "minValidators_", minValidators_, 
        "executionFee_", executionFee_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "publishCustomTask", params, null);
  }

  public FunctionHandle<ResultOf_defaultSettings> _defaultSettings() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_defaultSettings>(ResultOf_defaultSettings.class, sdk(), address(), abi(), credentials(), "_defaultSettings", params, null);
  }

  public FunctionHandle<ResultOf_taskCodes> _taskCodes() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_taskCodes>(ResultOf_taskCodes.class, sdk(), address(), abi(), credentials(), "_taskCodes", params, null);
  }

  public FunctionHandle<ResultOf_bank> _bank() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_bank>(ResultOf_bank.class, sdk(), address(), abi(), credentials(), "_bank", params, null);
  }

  public FunctionHandle<ResultOf_token> _token() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_token>(ResultOf_token.class, sdk(), address(), abi(), credentials(), "_token", params, null);
  }

  public FunctionHandle<ResultOf_bankStorageCode> _bankStorageCode() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_bankStorageCode>(ResultOf_bankStorageCode.class, sdk(), address(), abi(), credentials(), "_bankStorageCode", params, null);
  }

  public record ResultOfGetTaskAddress(Address value0) {
  }

  public record ResultOf_defaultSettings(Map<String, Object> _defaultSettings) {
  }

  public record ResultOf_taskCodes(Map<Integer, TvmCell> _taskCodes) {
  }

  public record ResultOf_bank(Address _bank) {
  }

  public record ResultOf_token(Address _token) {
  }

  public record ResultOf_bankStorageCode(TvmCell _bankStorageCode) {
  }
}
