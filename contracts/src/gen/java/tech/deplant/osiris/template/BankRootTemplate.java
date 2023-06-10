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
    return Tvc.ofBase64String("te6ccgECKgEABakAAgE0AwEBAcACAEPQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgBCSK7VMg4wMgwP/jAiDA/uMC8gsnBQQpA5btRNDXScMB+GaJ+Gkh2zzTAAGOFIMI1xgg+CjIzs7J+QBY+EL5EPKo3tM/AfhDIbnytCD4I4ED6KiCCBt3QKC58rT4Y9MfAds88jwRDgYDUu1E0NdJwwH4ZiLQ0wP6QDD4aak4ANwhxwDjAiHXDR/yvCHjAwHbPPI8JiYGAzwgghA92RaDu+MCIIIQaLVfP7vjAiCCEHxgWZO74wIbDAcCKCCCEHeTAL264wIgghB8YFmTuuMCCggDKDD4RvLgTPhCbuMA03/R2zzbPPIAJQkgAjZopvtg8tBIgWGo+Cj6Qm8S2zxw+wL4SXBY2zwiGQMmMPhG8uBM+EJu4wDU0ds82zzyACULIAF+aKb7YPLQSPhJ+kJvE9cL/8MAIJcw+En4SscF3vLgyYFhqPgo+kJvEts8cPsC+G34ScjPhYjOgG/PQMmDBvsAIgRQIIIQQkI6ibrjAiCCEGHP88a64wIgghBkq3t0uuMCIIIQaLVfP7rjAhcWEg0CIjD4Qm7jAPhG8nPR+ADbPPIADiACFu1E0NdJwgGOgOMNDyUEcHDtRND0BXEhgED0Do6A33IigED0Do6A33MjgED0Do6A34j4bfhs+Gv4aoBA9A7yvdcL//hicPhjEBAQKQECiREAQ4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABADgjD4RvLgTPhCbuMAIZPU0dDe+kDTB9HbPCGOHyPQ0wH6QDAxyM+HIM5xzwthAcjPk5Kt7dLOzclw+wCRMOLjAPIAJRQTACjtRNDT/9M/MfhDWMjL/8s/zsntVAEE2zwVAH5wyMv/cG2AQPRD+ChxWIBA9BZYcliAQPQWAcjLB3NYgED0Q8j0AMn4TcjPhID0APQAz4HJ+QDIz4oAQMv/ydABTjDR2zz4TSGOG40EcAAAAAAAAAAAAAAAADhz/PGgyM7MyXD7AN7yACUDPDD4RvLgTPhCbuMAIZPU0dDe+kDTB9N/0ds82zzyACUYIAJsaKb7YPLQSIFhqPgo+kJvEts8cPsC+En6Qm8T1wv/wwAgnjD4SfhKxwX4SfhMxwWx3vLgyds8IhkB2vhLURIkcMjL/3BtgED0Q/gocViAQPQWVQVyWIBA9BZVBMjLB3NYgED0Q8j0AMn4TcjPhID0APQAz4HJIPkAyM+KAEDL/8jPhYjPE3PPC24h2zzMz4NVMMjPkS3ce+bOywfLfwHIzs3NyYMG+wAaADTQ0gABk9IEMd7SAAGT0gEx3vQE9AT0BNFfAwRQIIIQDo5aULrjAiCCEA/YYKy64wIgghAxOCYJuuMCIIIQPdkWg7rjAh8eHRwBTjDR2zz4SyGOG40EcAAAAAAAAAAAAAAAAC92RaDgyM7OyXD7AN7yACUBTjDR2zz4TCGOG40EcAAAAAAAAAAAAAAAACxOCYJgyM7OyXD7AN7yACUBTjDR2zz4SiGOG40EcAAAAAAAAAAAAAAAACP2GCsgyM7OyXD7AN7yACUDNDD4RvLgTPhCbuMAIZPU0dDe+kDR2zzbPPIAJSEgAEL4TfhM+Ev4SvhD+ELIy//LP8+DzlUgyM5ZyM7Mzc3J7VQBfmim+2Dy0Ej4SfpCbxPXC//DACCXMPhJ+ErHBd7y4MmBYaj4KPpCbxLbPHD7Avhs+EnIz4WIzoBvz0DJgwb7ACIBDNs8gw+phiMBTiDAACHA/7Hy4EOAFIAV4wT4MiCOgN/y4ETQ0wfTP9M/0wfXCz9sQSQBBIgBKQBI7UTQ0//TP9MAMfpA1NHQ+kDU0dD6QNTR+G34bPhr+Gr4Y/hiAAr4RvLgTAIK9KQg9KEpKAAUc29sIDAuNjQuMAAA");
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
