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
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.Template;
import tech.deplant.osiris.contract.OracleValidator;

/**
 * Java template class for deploy of <strong>OracleValidator</strong> contract for Everscale blockchain.
 */
public record OracleValidatorTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public OracleValidatorTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public OracleValidatorTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"name_\",\"type\":\"string\"},{\"name\":\"logo_\",\"type\":\"string\"}],\"outputs\":[]},{\"name\":\"commit\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"commitHash_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"reveal\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"revealValue_\",\"type\":\"cell\"},{\"name\":\"revealSalt_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"requestRevealPhase\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"requestFinalPhase\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"getCommitHash\",\"inputs\":[{\"name\":\"response_\",\"type\":\"cell\"},{\"name\":\"salt_\",\"type\":\"uint256\"}],\"outputs\":[{\"name\":\"valueHash\",\"type\":\"uint256\"}]},{\"name\":\"subscribe\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"lastProcessedTx_\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"subscriptionAddress\",\"type\":\"address\"}]},{\"name\":\"unsubscribe\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"saveRequestTx\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"lastProcessedTx_\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"setSubscriptionCode\",\"inputs\":[{\"name\":\"subscriptionCode_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"getSubscriptionCodeHash\",\"inputs\":[],\"outputs\":[{\"name\":\"codeHash\",\"type\":\"uint256\"}]},{\"name\":\"_subscriptionCode\",\"inputs\":[],\"outputs\":[{\"name\":\"_subscriptionCode\",\"type\":\"cell\"}]},{\"name\":\"_name\",\"inputs\":[],\"outputs\":[{\"name\":\"_name\",\"type\":\"string\"}]},{\"name\":\"_logo\",\"inputs\":[],\"outputs\":[{\"name\":\"_logo\",\"type\":\"string\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_subscriptionCode\",\"type\":\"cell\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_subscriptionCode\",\"type\":\"cell\"},{\"name\":\"_name\",\"type\":\"string\"},{\"name\":\"_logo\",\"type\":\"string\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("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");
  }

  public DeployHandle<OracleValidator> prepareDeploy(Sdk sdk, Credentials credentials,
      TvmCell _subscriptionCode, String name_, String logo_) {
    Map<String, Object> initialDataFields = Map.of("_subscriptionCode", _subscriptionCode);
    Map<String, Object> params = Map.of("name_", name_, 
        "logo_", logo_);
    return new DeployHandle<OracleValidator>(OracleValidator.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
