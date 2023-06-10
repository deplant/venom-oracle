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
import tech.deplant.osiris.contract.BankRoot;

/**
 * Java template class for deploy of <strong>BankRoot</strong> contract for Everscale blockchain.
 */
public record BankRootTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public BankRootTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public BankRootTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"storageOf\",\"inputs\":[{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"address\"}]},{\"name\":\"deployStorage\",\"inputs\":[{\"name\":\"deployWalletValue_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"deployStorageTo\",\"inputs\":[{\"name\":\"owner_\",\"type\":\"address\"},{\"name\":\"type_\",\"type\":\"uint8\"},{\"name\":\"deployWalletValue_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"setStorageCode\",\"inputs\":[{\"name\":\"storageCode_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"setFactory\",\"inputs\":[{\"name\":\"factory_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"_owner\",\"inputs\":[],\"outputs\":[{\"name\":\"_owner\",\"type\":\"address\"}]},{\"name\":\"_token\",\"inputs\":[],\"outputs\":[{\"name\":\"_token\",\"type\":\"address\"}]},{\"name\":\"_factory\",\"inputs\":[],\"outputs\":[{\"name\":\"_factory\",\"type\":\"address\"}]},{\"name\":\"_storageCode\",\"inputs\":[],\"outputs\":[{\"name\":\"_storageCode\",\"type\":\"cell\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_owner\",\"type\":\"address\"},{\"key\":2,\"name\":\"_token\",\"type\":\"address\"},{\"key\":3,\"name\":\"_factory\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_owner\",\"type\":\"address\"},{\"name\":\"_token\",\"type\":\"address\"},{\"name\":\"_factory\",\"type\":\"address\"},{\"name\":\"_storageCode\",\"type\":\"cell\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("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");
  }

  public DeployHandle<BankRoot> prepareDeploy(Sdk sdk, Credentials credentials, Address _owner,
      Address _token, Address _factory) {
    Map<String, Object> initialDataFields = Map.of("_owner", _owner, 
        "_token", _token, 
        "_factory", _factory);
    Map<String, Object> params = Map.of();
    return new DeployHandle<BankRoot>(BankRoot.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
