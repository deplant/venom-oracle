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
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getTaskAddress\",\"inputs\":[{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"name_\",\"type\":\"string\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"setMinStake\",\"inputs\":[{\"name\":\"minStake_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"setBank\",\"inputs\":[{\"name\":\"bank_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"setTokenRoot\",\"inputs\":[{\"name\":\"tokenRoot_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"setTaskCode\",\"inputs\":[{\"name\":\"respType_\",\"type\":\"uint8\"},{\"name\":\"taskCode_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"acceptFactorySettingsUpdate\",\"inputs\":[],\"outputs\":[]},{\"name\":\"publishCustomTask\",\"inputs\":[{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"name_\",\"type\":\"string\"},{\"name\":\"body_\",\"type\":\"string\"},{\"name\":\"minValidators_\",\"type\":\"uint32\"},{\"name\":\"executionFee_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"_defaultSettings\",\"inputs\":[],\"outputs\":[{\"name\":\"_defaultSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]}]},{\"name\":\"_taskCodes\",\"inputs\":[],\"outputs\":[{\"name\":\"_taskCodes\",\"type\":\"map(uint8,cell)\"}]},{\"name\":\"_bank\",\"inputs\":[],\"outputs\":[{\"name\":\"_bank\",\"type\":\"address\"}]},{\"name\":\"_token\",\"inputs\":[],\"outputs\":[{\"name\":\"_token\",\"type\":\"address\"}]},{\"name\":\"_bankStorageCode\",\"inputs\":[],\"outputs\":[{\"name\":\"_bankStorageCode\",\"type\":\"cell\"}]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_defaultSettings\",\"type\":\"tuple\",\"components\":[{\"name\":\"minStake\",\"type\":\"uint128\"},{\"name\":\"electionTimeout\",\"type\":\"uint64\"},{\"name\":\"bountyStageSwitch\",\"type\":\"uint128\"},{\"name\":\"slashMissedStage\",\"type\":\"uint128\"},{\"name\":\"slashBadResponse\",\"type\":\"uint128\"}]},{\"name\":\"_taskCodes\",\"type\":\"map(uint8,cell)\"},{\"name\":\"_bank\",\"type\":\"address\"},{\"name\":\"_token\",\"type\":\"address\"},{\"name\":\"_bankStorageCode\",\"type\":\"cell\"}]}");
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
