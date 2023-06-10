package tech.deplant.osiris.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.Template;
import tech.deplant.osiris.contract.TestFaucet;

/**
 * Java template class for deploy of <strong>TestFaucet</strong> contract for Everscale blockchain.
 */
public record TestFaucetTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public TestFaucetTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public TestFaucetTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"token_\",\"type\":\"address\"},{\"name\":\"amount_\",\"type\":\"uint128\"}],\"outputs\":[]},{\"name\":\"onTokenRootCallback\",\"inputs\":[{\"name\":\"tokenWallet_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"faucet\",\"inputs\":[{\"name\":\"doDeploy_\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"_token\",\"inputs\":[],\"outputs\":[{\"name\":\"_token\",\"type\":\"address\"}]},{\"name\":\"_tokenWallet\",\"inputs\":[],\"outputs\":[{\"name\":\"_tokenWallet\",\"type\":\"address\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_token\",\"type\":\"address\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_token\",\"type\":\"address\"},{\"name\":\"_tokenWallet\",\"type\":\"address\"},{\"name\":\"_amount\",\"type\":\"uint128\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("te6ccgECHQEAA44AAgE0AwEBAcACAEPQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgBCSK7VMg4wMgwP/jAiDA/uMC8gsaBQQcA5ztRNDXScMB+GaJ+Gkh2zzTAAGOF4MI1xgg+CjIzs7J+QBY+EIg+GX5EPKo3tM/AfhDIbnytCD4I4ED6KiCCBt3QKC58rT4Y9MfAds88jwVEgYDUu1E0NdJwwH4ZiLQ0wP6QDD4aak4ANwhxwDjAiHXDR/yvCHjAwHbPPI8GRkGAiggghBFI3PHu+MCIIIQVmUd27rjAgoHAygw+Eby4Ez4Qm7jANIA0ds84wDyABgJCAAo7UTQ0//TPzH4Q1jIy//LP87J7VQCqmim+2Dy0EiBYaj4KPpCbxLbPHD7AoIQBfXhAHDjBIhw+ElVAvhJ+Ez4S3/Iz4WAygDPhEDOcc8LblVQyM+Rz4iFDst/zst/VSDIzsoAzM3NyYMG+wANHARQIIIQC8H+L7rjAiCCED3ZFoO64wIgghBCHlYVuuMCIIIQRSNzx7rjAhcWEAsDNDD4RvLgTPhCbuMAIZPU0dDe+kDR2zzbPPIAGAwRATKBYaj4KPpCbxLbPHD7AvhJ+ErHBfLgZvhrDQEM2zyDD6mGDgFOIMAAIcD/sfLgQ4AUgBXjBPgyII6A3/LgRNDTB9M/0z/TB9cLP2xBDwEEiAEcAugw+EJu4wD4RvJzIZPU0dDe+kDTf9H4RSBukjBw3vhCuvLgZAH4SscF8uBl+AD4bIIQBfXhAPgo+Ep/yM+FgMoAz4RAzoKAIF9eEAAAAAAAAAAAAAAAAAABzwuOWYuDHt1MdFI3PHjIzs7Lf83JcfsA2zzyABIRADb4TPhL+Er4Q/hCyMv/yz/Pg85ZyM7Lf83J7VQCFu1E0NdJwgGOgOMNExgCSnDtRND0BXEhgED0Do6A34lw+Gz4a/hqgED0DvK91wv/+GJw+GMUFQECiRUAQ4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABABTjDR2zz4SiGOG40EcAAAAAAAAAAAAAAAAC92RaDgyM7OyXD7AN7yABgBTjDR2zz4SyGOG40EcAAAAAAAAAAAAAAAACLwf4vgyM7OyXD7AN7yABgAPO1E0NP/0z/TADH6QNTR0PpA03/R+Gz4a/hq+GP4YgAK+Eby4EwCCvSkIPShHBsAFHNvbCAwLjY0LjAAAA==");
  }

  public DeployHandle<TestFaucet> prepareDeploy(Sdk sdk, Credentials credentials, Address _token,
      Address token_, BigInteger amount_) {
    Map<String, Object> initialDataFields = Map.of("_token", _token);
    Map<String, Object> params = Map.of("token_", token_, 
        "amount_", amount_);
    return new DeployHandle<TestFaucet>(TestFaucet.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
