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
import tech.deplant.java4ever.framework.template.Template;
import tech.deplant.osiris.contract.Factory;

/**
 * Java template class for deploy of <strong>Factory</strong> contract for Everscale blockchain.
 */
public record FactoryTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public FactoryTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public FactoryTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getTaskAddress\",\"inputs\":[{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"name_\",\"type\":\"string\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"setMinStake\",\"inputs\":[{\"name\":\"minStake_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"setBank\",\"inputs\":[{\"name\":\"bank_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"setTokenRoot\",\"inputs\":[{\"name\":\"tokenRoot_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"setTaskCode\",\"inputs\":[{\"name\":\"respType_\",\"type\":\"uint8\"},{\"name\":\"taskCode_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"acceptTaskStorageAddress\",\"inputs\":[{\"name\":\"task_\",\"type\":\"address\"},{\"name\":\"taskStorage_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"acceptFactorySettingsUpdate\",\"inputs\":[],\"outputs\":[]},{\"name\":\"publishCustomTask\",\"inputs\":[{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"name_\",\"type\":\"string\"},{\"name\":\"body_\",\"type\":\"string\"},{\"name\":\"minValidators_\",\"type\":\"uint32\"},{\"name\":\"executionFee_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"_defaultSettings\",\"inputs\":[],\"outputs\":[{\"name\":\"_defaultSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}]},{\"name\":\"_taskCodes\",\"inputs\":[],\"outputs\":[{\"name\":\"_taskCodes\",\"type\":\"map(uint8,cell)\"}]},{\"name\":\"_bank\",\"inputs\":[],\"outputs\":[{\"name\":\"_bank\",\"type\":\"address\"}]},{\"name\":\"_token\",\"inputs\":[],\"outputs\":[{\"name\":\"_token\",\"type\":\"address\"}]},{\"name\":\"_bankStorageCode\",\"inputs\":[],\"outputs\":[{\"name\":\"_bankStorageCode\",\"type\":\"cell\"}]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_defaultSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]},{\"name\":\"_taskCodes\",\"type\":\"map(uint8,cell)\"},{\"name\":\"_bank\",\"type\":\"address\"},{\"name\":\"_token\",\"type\":\"address\"},{\"name\":\"_bankStorageCode\",\"type\":\"cell\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("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");
  }

  public DeployHandle<Factory> prepareDeploy(Sdk sdk, Credentials credentials) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of();
    return new DeployHandle<Factory>(Factory.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
