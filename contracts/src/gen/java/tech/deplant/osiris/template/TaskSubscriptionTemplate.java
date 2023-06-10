package tech.deplant.osiris.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Long;
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
import tech.deplant.osiris.contract.TaskSubscription;

/**
 * Java template class for deploy of <strong>TaskSubscription</strong> contract for Everscale blockchain.
 */
public record TaskSubscriptionTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public TaskSubscriptionTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public TaskSubscriptionTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"lastTx_\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"cancel\",\"inputs\":[],\"outputs\":[]},{\"name\":\"updateLastTx\",\"inputs\":[{\"name\":\"lastTx_\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"_taskAddress\",\"inputs\":[],\"outputs\":[{\"name\":\"_taskAddress\",\"type\":\"address\"}]},{\"name\":\"_oracleAddress\",\"inputs\":[],\"outputs\":[{\"name\":\"_oracleAddress\",\"type\":\"address\"}]},{\"name\":\"_lastTx\",\"inputs\":[],\"outputs\":[{\"name\":\"_lastTx\",\"type\":\"uint64\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_taskAddress\",\"type\":\"address\"},{\"key\":2,\"name\":\"_oracleAddress\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_taskAddress\",\"type\":\"address\"},{\"name\":\"_oracleAddress\",\"type\":\"address\"},{\"name\":\"_lastTx\",\"type\":\"uint64\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("te6ccgECIgEAA7oAAgE0AwEBAcACAEPQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgBCSK7VMg4wMgwP/jAiDA/uMC8gsfBQQhA6btRNDXScMB+GaJ+Gkh2zzTAAGOFIMI1xgg+CjIzs7J+QBY+EL5EPKo3tM/AfhDIbnytCD4I4ED6KiCCBt3QKC58rT4Y9MfAfgjvPK50x8B2zzyPBcUBgNS7UTQ10nDAfhmItDTA/pAMPhpqTgA3CHHAOMCIdcNH/K8IeMDAds88jweHgYCKCCCEA0P3qy74wIgghB1ju5Nu+MCCwcCKCCCEFb4QlK64wIgghB1ju5NuuMCCQgBTjDR2zz4SyGOG40EcAAAAAAAAAAAAAAAAD1ju5NgyM7OyXD7AN7yAB0DKDD4RvLgTPhCbuMA0z/R2zzbPPIAHQoOAWBopvtg8tBI+En4S8cF8uDJgWGo+Cj6Qm8S2zxw+wL4bPhJyM+FiM6Ab89AyYMG+wARBFAgghAJAQNHuuMCIIIQChxxlbrjAiCCEAzad+264wIgghAND96suuMCHBsYDARoMPhCbuMA+EbycyGT1NHQ3vpA0z/R+En4S8cF8uDJgWGo+Cj6Qm8S2zxw+wL4Sts8AfhqIBQREA0COo6A3vLgyPhsMPhJyM+FiM6Ab89AyYMG+wDbPPIADw4ANvhM+Ev4SvhD+ELIy//LP8+DzlnIzss/zcntVAEcMPhL2zwB+Gsi+ErHBbAQABQg+kJvE9cL/8MAAQzbPIMPqYYSAU4gwAAhwP+x8uBDgBSAFeME+DIgjoDf8uBE0NMH0z/TP9MH1ws/bEETAQSIASECFu1E0NdJwgGOgOMNFR0CWnDtRND0BXEhgED0Do6A33IigED0Do6A33D4bPhr+GqAQPQO8r3XC//4YnD4YxYWAQKJFwBDgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAMkMPhG8uBM+EJu4wDR2zzjAPIAHRoZACjtRNDT/9M/MfhDWMjL/8s/zsntVABIaKb7YPLQSPhJ+EvHBfLgyfhLyM+FCM6Ab89AyYMGpiK1B/sAAVAw0ds8+EwhjhyNBHAAAAAAAAAAAAAAAAAihxxlYMjOyz/JcPsA3vIAHQFOMNHbPPhKIY4bjQRwAAAAAAAAAAAAAAAAIkBA0eDIzs7JcPsA3vIAHQA87UTQ0//TP9MAMfpA1NHQ+kDTP9H4bPhr+Gr4Y/hiAAr4RvLgTAIK9KQg9KEhIAAUc29sIDAuNjQuMAAA");
  }

  public DeployHandle<TaskSubscription> prepareDeploy(Sdk sdk, Credentials credentials,
      Address _taskAddress, Address _oracleAddress, Address taskAddress_, Long lastTx_) {
    Map<String, Object> initialDataFields = Map.of("_taskAddress", _taskAddress, 
        "_oracleAddress", _oracleAddress);
    Map<String, Object> params = Map.of("taskAddress_", taskAddress_, 
        "lastTx_", lastTx_);
    return new DeployHandle<TaskSubscription>(TaskSubscription.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
