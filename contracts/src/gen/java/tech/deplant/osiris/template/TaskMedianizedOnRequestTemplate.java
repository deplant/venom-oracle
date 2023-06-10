package tech.deplant.osiris.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Object;
import java.lang.String;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.Template;
import tech.deplant.osiris.contract.TaskMedianizedOnRequest;

/**
 * Java template class for deploy of <strong>TaskMedianizedOnRequest</strong> contract for Everscale blockchain.
 */
public record TaskMedianizedOnRequestTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public TaskMedianizedOnRequestTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public TaskMedianizedOnRequestTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"taskSettings_\",\"type\":\"tuple\",\"components\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"consensusType\",\"type\":\"uint8\"},{\"name\":\"minValidators\",\"type\":\"uint32\"},{\"name\":\"executionFee\",\"type\":\"uint128\"}]},{\"name\":\"factorySettings_\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}],\"outputs\":[]},{\"name\":\"medianizedTaskRequest\",\"inputs\":[{\"name\":\"payerAddress_\",\"type\":\"address\"},{\"name\":\"requestParams_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"onReceivedTransfer\",\"inputs\":[{\"name\":\"sender_\",\"type\":\"address\"},{\"name\":\"value1\",\"type\":\"address\"},{\"name\":\"valueAmount_\",\"type\":\"uint128\"},{\"name\":\"tokenAmount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"acceptLockResult\",\"inputs\":[{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"hasLocked_\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"setTaskDetails\",\"inputs\":[{\"name\":\"taskSettings_\",\"type\":\"tuple\",\"components\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"consensusType\",\"type\":\"uint8\"},{\"name\":\"minValidators\",\"type\":\"uint32\"},{\"name\":\"executionFee\",\"type\":\"uint128\"}]}],\"outputs\":[]},{\"name\":\"requestFactorySettingsUpdate\",\"inputs\":[],\"outputs\":[]},{\"name\":\"setStorage\",\"inputs\":[{\"name\":\"storage_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onUpdateFactorySettingsResponse\",\"inputs\":[{\"name\":\"factorySettings_\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}],\"outputs\":[]},{\"name\":\"onCommit\",\"inputs\":[{\"name\":\"commitHash_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"onRequestRevealPhase\",\"inputs\":[],\"outputs\":[]},{\"name\":\"onReveal\",\"inputs\":[{\"name\":\"revealValue_\",\"type\":\"cell\"},{\"name\":\"revealSalt_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"onRequestFinalPhase\",\"inputs\":[],\"outputs\":[]},{\"name\":\"_factory\",\"inputs\":[],\"outputs\":[{\"name\":\"_factory\",\"type\":\"address\"}]},{\"name\":\"_owner\",\"inputs\":[],\"outputs\":[{\"name\":\"_owner\",\"type\":\"address\"}]},{\"name\":\"_name\",\"inputs\":[],\"outputs\":[{\"name\":\"_name\",\"type\":\"string\"}]},{\"name\":\"_taskSettings\",\"inputs\":[],\"outputs\":[{\"name\":\"_taskSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"consensusType\",\"type\":\"uint8\"},{\"name\":\"minValidators\",\"type\":\"uint32\"},{\"name\":\"executionFee\",\"type\":\"uint128\"}]}]},{\"name\":\"_factorySettings\",\"inputs\":[],\"outputs\":[{\"name\":\"_factorySettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}]},{\"name\":\"_currElectionState\",\"inputs\":[],\"outputs\":[{\"name\":\"_currElectionState\",\"type\":\"tuple\",\"components\":[{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"failureReason\",\"type\":\"uint16\"},{\"name\":\"response\",\"type\":\"cell\"},{\"name\":\"commitStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"revealStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"responseTimestamp\",\"type\":\"uint64\"},{\"name\":\"participants\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"validator\",\"type\":\"address\"},{\"name\":\"enterStake\",\"type\":\"uint128\"},{\"name\":\"commitHash\",\"type\":\"uint256\"},{\"name\":\"revealResponse\",\"type\":\"cell\"},{\"name\":\"bountyValue\",\"type\":\"uint128\"},{\"name\":\"slashValue\",\"type\":\"uint128\"},{\"name\":\"committed\",\"type\":\"bool\"},{\"name\":\"revealed\",\"type\":\"bool\"}]},{\"name\":\"commitedCount\",\"type\":\"uint16\"},{\"name\":\"revealedCount\",\"type\":\"uint16\"},{\"name\":\"acceptedCount\",\"type\":\"uint16\"}]}]},{\"name\":\"_prevElectionState\",\"inputs\":[],\"outputs\":[{\"name\":\"_prevElectionState\",\"type\":\"tuple\",\"components\":[{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"failureReason\",\"type\":\"uint16\"},{\"name\":\"response\",\"type\":\"cell\"},{\"name\":\"commitStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"revealStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"responseTimestamp\",\"type\":\"uint64\"},{\"name\":\"participants\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"validator\",\"type\":\"address\"},{\"name\":\"enterStake\",\"type\":\"uint128\"},{\"name\":\"commitHash\",\"type\":\"uint256\"},{\"name\":\"revealResponse\",\"type\":\"cell\"},{\"name\":\"bountyValue\",\"type\":\"uint128\"},{\"name\":\"slashValue\",\"type\":\"uint128\"},{\"name\":\"committed\",\"type\":\"bool\"},{\"name\":\"revealed\",\"type\":\"bool\"}]},{\"name\":\"commitedCount\",\"type\":\"uint16\"},{\"name\":\"revealedCount\",\"type\":\"uint16\"},{\"name\":\"acceptedCount\",\"type\":\"uint16\"}]}]},{\"name\":\"_storage\",\"inputs\":[],\"outputs\":[{\"name\":\"_storage\",\"type\":\"address\"}]}],\"events\":[{\"name\":\"requestReceived\",\"inputs\":[{\"name\":\"requester\",\"type\":\"address\"},{\"name\":\"requestParams\",\"type\":\"cell\"}]}],\"data\":[{\"key\":1,\"name\":\"_factory\",\"type\":\"address\"},{\"key\":2,\"name\":\"_owner\",\"type\":\"address\"},{\"key\":3,\"name\":\"_name\",\"type\":\"string\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_factory\",\"type\":\"address\"},{\"name\":\"_owner\",\"type\":\"address\"},{\"name\":\"_name\",\"type\":\"string\"},{\"name\":\"_taskSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"consensusType\",\"type\":\"uint8\"},{\"name\":\"minValidators\",\"type\":\"uint32\"},{\"name\":\"executionFee\",\"type\":\"uint128\"}]},{\"name\":\"_factorySettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]},{\"name\":\"_currElectionState\",\"type\":\"tuple\",\"components\":[{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"failureReason\",\"type\":\"uint16\"},{\"name\":\"response\",\"type\":\"cell\"},{\"name\":\"commitStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"revealStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"responseTimestamp\",\"type\":\"uint64\"},{\"name\":\"participants\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"validator\",\"type\":\"address\"},{\"name\":\"enterStake\",\"type\":\"uint128\"},{\"name\":\"commitHash\",\"type\":\"uint256\"},{\"name\":\"revealResponse\",\"type\":\"cell\"},{\"name\":\"bountyValue\",\"type\":\"uint128\"},{\"name\":\"slashValue\",\"type\":\"uint128\"},{\"name\":\"committed\",\"type\":\"bool\"},{\"name\":\"revealed\",\"type\":\"bool\"}]},{\"name\":\"commitedCount\",\"type\":\"uint16\"},{\"name\":\"revealedCount\",\"type\":\"uint16\"},{\"name\":\"acceptedCount\",\"type\":\"uint16\"}]},{\"name\":\"_prevElectionState\",\"type\":\"tuple\",\"components\":[{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"failureReason\",\"type\":\"uint16\"},{\"name\":\"response\",\"type\":\"cell\"},{\"name\":\"commitStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"revealStartTimestamp\",\"type\":\"uint64\"},{\"name\":\"responseTimestamp\",\"type\":\"uint64\"},{\"name\":\"participants\",\"type\":\"map(address,tuple)\",\"components\":[{\"name\":\"validator\",\"type\":\"address\"},{\"name\":\"enterStake\",\"type\":\"uint128\"},{\"name\":\"commitHash\",\"type\":\"uint256\"},{\"name\":\"revealResponse\",\"type\":\"cell\"},{\"name\":\"bountyValue\",\"type\":\"uint128\"},{\"name\":\"slashValue\",\"type\":\"uint128\"},{\"name\":\"committed\",\"type\":\"bool\"},{\"name\":\"revealed\",\"type\":\"bool\"}]},{\"name\":\"commitedCount\",\"type\":\"uint16\"},{\"name\":\"revealedCount\",\"type\":\"uint16\"},{\"name\":\"acceptedCount\",\"type\":\"uint16\"}]},{\"name\":\"_storage\",\"type\":\"address\"},{\"name\":\"_currentRequester\",\"type\":\"address\"},{\"name\":\"_requestParams\",\"type\":\"cell\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("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");
  }

  public DeployHandle<TaskMedianizedOnRequest> prepareDeploy(Sdk sdk, Credentials credentials,
      Address _factory, Address _owner, String _name, Map<String, Object> taskSettings_,
      Map<String, Object> factorySettings_) {
    Map<String, Object> initialDataFields = Map.of("_factory", _factory, 
        "_owner", _owner, 
        "_name", _name);
    Map<String, Object> params = Map.of("taskSettings_", taskSettings_, 
        "factorySettings_", factorySettings_);
    return new DeployHandle<TaskMedianizedOnRequest>(TaskMedianizedOnRequest.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
