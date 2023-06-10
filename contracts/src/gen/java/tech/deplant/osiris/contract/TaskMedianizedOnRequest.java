package tech.deplant.osiris.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
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
 * Java wrapper class for usage of <strong>TaskMedianizedOnRequest</strong> contract for Everscale blockchain.
 */
public record TaskMedianizedOnRequest(Sdk sdk, String address, ContractAbi abi,
    Credentials credentials) implements Contract {
  public TaskMedianizedOnRequest(Sdk sdk, String address) throws JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public TaskMedianizedOnRequest(Sdk sdk, String address, ContractAbi abi) {
    this(sdk,address,abi,Credentials.NONE);
  }

  public TaskMedianizedOnRequest(Sdk sdk, String address, Credentials credentials) throws
      JsonProcessingException {
    this(sdk,address,DEFAULT_ABI(),credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"taskSettings_\",\"type\":\"tuple\",\"components\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"consensusType\",\"type\":\"uint8\"},{\"name\":\"minValidators\",\"type\":\"uint32\"},{\"name\":\"executionFee\",\"type\":\"uint128\"}]},{\"name\":\"factorySettings_\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}],\"outputs\":[]},{\"name\":\"medianizedTaskRequest\",\"inputs\":[{\"name\":\"payerAddress_\",\"type\":\"address\"},{\"name\":\"requestParams_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"onReceivedTransfer\",\"inputs\":[{\"name\":\"sender_\",\"type\":\"address\"},{\"name\":\"value1\",\"type\":\"address\"},{\"name\":\"valueAmount_\",\"type\":\"uint128\"},{\"name\":\"tokenAmount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"acceptLockResult\",\"inputs\":[{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"hasLocked_\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"setTaskDetails\",\"inputs\":[{\"name\":\"taskSettings_\",\"type\":\"tuple\",\"components\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"consensusType\",\"type\":\"uint8\"},{\"name\":\"minValidators\",\"type\":\"uint32\"},{\"name\":\"executionFee\",\"type\":\"uint128\"}]}],\"outputs\":[]},{\"name\":\"requestFactorySettingsUpdate\",\"inputs\":[],\"outputs\":[]},{\"name\":\"setStorage\",\"inputs\":[{\"name\":\"storage_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onUpdateFactorySettingsResponse\",\"inputs\":[{\"name\":\"factorySettings_\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}],\"outputs\":[]},{\"name\":\"onCommit\",\"inputs\":[{\"name\":\"commitHash_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"onRequestRevealPhase\",\"inputs\":[],\"outputs\":[]},{\"name\":\"onReveal\",\"inputs\":[{\"name\":\"revealValue_\",\"type\":\"cell\"},{\"name\":\"revealSalt_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"onRequestFinalPhase\",\"inputs\":[],\"outputs\":[]},{\"name\":\"_factory\",\"inputs\":[],\"outputs\":[{\"name\":\"_factory\",\"type\":\"address\"}]},{\"name\":\"_owner\",\"inputs\":[],\"outputs\":[{\"name\":\"_owner\",\"type\":\"address\"}]},{\"name\":\"_name\",\"inputs\":[],\"outputs\":[{\"name\":\"_name\",\"type\":\"string\"}]},{\"name\":\"_taskSettings\",\"inputs\":[],\"outputs\":[{\"name\":\"_taskSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"consensusType\",\"type\":\"uint8\"},{\"name\":\"minValidators\",\"type\":\"uint32\"},{\"name\":\"executionFee\",\"type\":\"uint128\"}]}]},{\"name\":\"_factorySettings\",\"inputs\":[],\"outputs\":[{\"name\":\"_factorySettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}]},{\"name\":\"_currElectionState\",\"inputs\":[],\"outputs\":[{\"name\":\"_currElectionState\",\"type\":\"tuple\",\"components\":[{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"failureReason\",\"type\":\"uint16\"},{\"name\":\"response\",\"type\":\"cell\"},{\"name\":\"commitStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"revealStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"responseTimestamp\",\"type\":\"uint64\"},{\"name\":\"participants\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"validator\",\"type\":\"address\"},{\"name\":\"enterStake\",\"type\":\"uint128\"},{\"name\":\"commitHash\",\"type\":\"uint256\"},{\"name\":\"revealResponse\",\"type\":\"cell\"},{\"name\":\"bountyValue\",\"type\":\"uint128\"},{\"name\":\"slashValue\",\"type\":\"uint128\"},{\"name\":\"committed\",\"type\":\"bool\"},{\"name\":\"revealed\",\"type\":\"bool\"}]},{\"name\":\"commitedCount\",\"type\":\"uint16\"},{\"name\":\"revealedCount\",\"type\":\"uint16\"},{\"name\":\"acceptedCount\",\"type\":\"uint16\"}]}]},{\"name\":\"_prevElectionState\",\"inputs\":[],\"outputs\":[{\"name\":\"_prevElectionState\",\"type\":\"tuple\",\"components\":[{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"failureReason\",\"type\":\"uint16\"},{\"name\":\"response\",\"type\":\"cell\"},{\"name\":\"commitStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"revealStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"responseTimestamp\",\"type\":\"uint64\"},{\"name\":\"participants\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"validator\",\"type\":\"address\"},{\"name\":\"enterStake\",\"type\":\"uint128\"},{\"name\":\"commitHash\",\"type\":\"uint256\"},{\"name\":\"revealResponse\",\"type\":\"cell\"},{\"name\":\"bountyValue\",\"type\":\"uint128\"},{\"name\":\"slashValue\",\"type\":\"uint128\"},{\"name\":\"committed\",\"type\":\"bool\"},{\"name\":\"revealed\",\"type\":\"bool\"}]},{\"name\":\"commitedCount\",\"type\":\"uint16\"},{\"name\":\"revealedCount\",\"type\":\"uint16\"},{\"name\":\"acceptedCount\",\"type\":\"uint16\"}]}]},{\"name\":\"_storage\",\"inputs\":[],\"outputs\":[{\"name\":\"_storage\",\"type\":\"address\"}]}],\"events\":[{\"name\":\"requestReceived\",\"inputs\":[{\"name\":\"requester\",\"type\":\"address\"},{\"name\":\"requestParams\",\"type\":\"cell\"}]}],\"data\":[{\"key\":1,\"name\":\"_factory\",\"type\":\"address\"},{\"key\":2,\"name\":\"_owner\",\"type\":\"address\"},{\"key\":3,\"name\":\"_name\",\"type\":\"string\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_factory\",\"type\":\"address\"},{\"name\":\"_owner\",\"type\":\"address\"},{\"name\":\"_name\",\"type\":\"string\"},{\"name\":\"_taskSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"consensusType\",\"type\":\"uint8\"},{\"name\":\"minValidators\",\"type\":\"uint32\"},{\"name\":\"executionFee\",\"type\":\"uint128\"}]},{\"name\":\"_factorySettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]},{\"name\":\"_currElectionState\",\"type\":\"tuple\",\"components\":[{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"failureReason\",\"type\":\"uint16\"},{\"name\":\"response\",\"type\":\"cell\"},{\"name\":\"commitStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"revealStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"responseTimestamp\",\"type\":\"uint64\"},{\"name\":\"participants\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"validator\",\"type\":\"address\"},{\"name\":\"enterStake\",\"type\":\"uint128\"},{\"name\":\"commitHash\",\"type\":\"uint256\"},{\"name\":\"revealResponse\",\"type\":\"cell\"},{\"name\":\"bountyValue\",\"type\":\"uint128\"},{\"name\":\"slashValue\",\"type\":\"uint128\"},{\"name\":\"committed\",\"type\":\"bool\"},{\"name\":\"revealed\",\"type\":\"bool\"}]},{\"name\":\"commitedCount\",\"type\":\"uint16\"},{\"name\":\"revealedCount\",\"type\":\"uint16\"},{\"name\":\"acceptedCount\",\"type\":\"uint16\"}]},{\"name\":\"_prevElectionState\",\"type\":\"tuple\",\"components\":[{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"failureReason\",\"type\":\"uint16\"},{\"name\":\"response\",\"type\":\"cell\"},{\"name\":\"commitStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"revealStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"responseTimestamp\",\"type\":\"uint64\"},{\"name\":\"participants\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"validator\",\"type\":\"address\"},{\"name\":\"enterStake\",\"type\":\"uint128\"},{\"name\":\"commitHash\",\"type\":\"uint256\"},{\"name\":\"revealResponse\",\"type\":\"cell\"},{\"name\":\"bountyValue\",\"type\":\"uint128\"},{\"name\":\"slashValue\",\"type\":\"uint128\"},{\"name\":\"committed\",\"type\":\"bool\"},{\"name\":\"revealed\",\"type\":\"bool\"}]},{\"name\":\"commitedCount\",\"type\":\"uint16\"},{\"name\":\"revealedCount\",\"type\":\"uint16\"},{\"name\":\"acceptedCount\",\"type\":\"uint16\"}]},{\"name\":\"_storage\",\"type\":\"address\"},{\"name\":\"_currentRequester\",\"type\":\"address\"},{\"name\":\"_requestParams\",\"type\":\"cell\"}]}");
  }

  public FunctionHandle<Void> medianizedTaskRequest(Address payerAddress_, TvmCell requestParams_) {
    Map<String, Object> params = Map.of("payerAddress_", payerAddress_, 
        "requestParams_", requestParams_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "medianizedTaskRequest", params, null);
  }

  public FunctionHandle<Void> onReceivedTransfer(Address sender_, Address value1,
      BigInteger valueAmount_, BigInteger tokenAmount_) {
    Map<String, Object> params = Map.of("sender_", sender_, 
        "value1", value1, 
        "valueAmount_", valueAmount_, 
        "tokenAmount_", tokenAmount_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onReceivedTransfer", params, null);
  }

  public FunctionHandle<Void> acceptLockResult(Address owner_, Integer type_, Boolean hasLocked_) {
    Map<String, Object> params = Map.of("owner_", owner_, 
        "type_", type_, 
        "hasLocked_", hasLocked_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "acceptLockResult", params, null);
  }

  public FunctionHandle<Void> setTaskDetails(Map<String, Object> taskSettings_) {
    Map<String, Object> params = Map.of("taskSettings_", taskSettings_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setTaskDetails", params, null);
  }

  public FunctionHandle<Void> requestFactorySettingsUpdate() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "requestFactorySettingsUpdate", params, null);
  }

  public FunctionHandle<Void> setStorage(Address storage_) {
    Map<String, Object> params = Map.of("storage_", storage_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "setStorage", params, null);
  }

  public FunctionHandle<Void> onUpdateFactorySettingsResponse(
      Map<String, Object> factorySettings_) {
    Map<String, Object> params = Map.of("factorySettings_", factorySettings_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onUpdateFactorySettingsResponse", params, null);
  }

  public FunctionHandle<Void> onCommit(BigInteger commitHash_) {
    Map<String, Object> params = Map.of("commitHash_", commitHash_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onCommit", params, null);
  }

  public FunctionHandle<Void> onRequestRevealPhase() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onRequestRevealPhase", params, null);
  }

  public FunctionHandle<Void> onReveal(TvmCell revealValue_, BigInteger revealSalt_) {
    Map<String, Object> params = Map.of("revealValue_", revealValue_, 
        "revealSalt_", revealSalt_);
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onReveal", params, null);
  }

  public FunctionHandle<Void> onRequestFinalPhase() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "onRequestFinalPhase", params, null);
  }

  public FunctionHandle<ResultOf_factory> _factory() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_factory>(ResultOf_factory.class, sdk(), address(), abi(), credentials(), "_factory", params, null);
  }

  public FunctionHandle<ResultOf_owner> _owner() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_owner>(ResultOf_owner.class, sdk(), address(), abi(), credentials(), "_owner", params, null);
  }

  public FunctionHandle<ResultOf_name> _name() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_name>(ResultOf_name.class, sdk(), address(), abi(), credentials(), "_name", params, null);
  }

  public FunctionHandle<ResultOf_taskSettings> _taskSettings() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_taskSettings>(ResultOf_taskSettings.class, sdk(), address(), abi(), credentials(), "_taskSettings", params, null);
  }

  public FunctionHandle<ResultOf_factorySettings> _factorySettings() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_factorySettings>(ResultOf_factorySettings.class, sdk(), address(), abi(), credentials(), "_factorySettings", params, null);
  }

  public FunctionHandle<ResultOf_currElectionState> _currElectionState() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_currElectionState>(ResultOf_currElectionState.class, sdk(), address(), abi(), credentials(), "_currElectionState", params, null);
  }

  public FunctionHandle<ResultOf_prevElectionState> _prevElectionState() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_prevElectionState>(ResultOf_prevElectionState.class, sdk(), address(), abi(), credentials(), "_prevElectionState", params, null);
  }

  public FunctionHandle<ResultOf_storage> _storage() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOf_storage>(ResultOf_storage.class, sdk(), address(), abi(), credentials(), "_storage", params, null);
  }

  public record ResultOf_factory(Address _factory) {
  }

  public record ResultOf_owner(Address _owner) {
  }

  public record ResultOf_name(String _name) {
  }

  public record ResultOf_taskSettings(Map<String, Object> _taskSettings) {
  }

  public record ResultOf_factorySettings(Map<String, Object> _factorySettings) {
  }

  public record ResultOf_currElectionState(Map<String, Object> _currElectionState) {
  }

  public record ResultOf_prevElectionState(Map<String, Object> _prevElectionState) {
  }

  public record ResultOf_storage(Address _storage) {
  }
}
