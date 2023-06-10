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
import tech.deplant.osiris.contract.ExampleConsumer;

/**
 * Java template class for deploy of <strong>ExampleConsumer</strong> contract for Everscale blockchain.
 */
public record ExampleConsumerTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public ExampleConsumerTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public ExampleConsumerTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"calculatePreciseOnRequest\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"payerAddress_\",\"type\":\"address\"},{\"name\":\"params_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"calculateMedianizedOnRequest\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"payerAddress_\",\"type\":\"address\"},{\"name\":\"params_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"calculatePreciseFeed\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"payerAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"calculateMedianizedFeed\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"payerAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"getPreciseResult\",\"inputs\":[],\"outputs\":[{\"name\":\"value0\",\"type\":\"cell\"}]},{\"name\":\"getMedianizedResult\",\"inputs\":[],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint128\"}]},{\"name\":\"preciseCallback\",\"inputs\":[{\"name\":\"response_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"medianizedCallback\",\"inputs\":[{\"name\":\"response_\",\"type\":\"uint128\"}],\"outputs\":[]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_preciseResult\",\"type\":\"cell\"},{\"name\":\"_medianizedResult\",\"type\":\"uint128\"},{\"name\":\"_currentTask\",\"type\":\"address\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("te6ccgECJAEABPMAAgE0AwEBAcACAEPQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgBCSK7VMg4wMgwP/jAiDA/uMC8gshBQQjA5ztRNDXScMB+GaJ+Gkh2zzTAAGOF4MI1xgg+CjIzs7J+QBY+EIg+GX5EPKo3tM/AfhDIbnytCD4I4ED6KiCCBt3QKC58rT4Y9MfAds88jwPDQYDUu1E0NdJwwH4ZiLQ0wP6QDD4aak4ANwhxwDjAiHXDR/yvCHjAwHbPPI8ICAGAzwgghA6UPx4u+MCIIIQbswsKrvjAiCCEHoPwo+64wIUCQcDPjD4RvLgTPhCbuMAIZPU0dDe+kDU0dD6QNHbPNs88gAfCB0AniH6Qm8T1wv/8uG8+EUgbpIwcN74Qrry4Mf4ACH4bAF/yM+FgMoAz4RAzoKAIO5rKAAAAAAAAAAAAAAAAAABzwuOAcjPkDEgn8bOzclx+wAEUCCCEEEPUfK64wIgghBfTeu8uuMCIIIQaLVfP7rjAiCCEG7MLCq64wISEAwKAygw+Eby4Ez4Qm7jANN/0ds82zzyAB8LHQBCaKb7YPLQSPhJ+EzHBSCcMPhJ+kJvE9cL/8MA3vLgxvhrAj4w+EJu4wD4RvJz0fhFIG6SMHDe+EK68uDH+ADbPPIADR0CFu1E0NdJwgGOgOMNDh8COnDtRND0BYhwifhs+Gv4aoBA9A7yvdcL//hicPhjIw8AQ4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABADQDD4RvLgTPhCbuMAIZPU0dDe+kDU0dD6QNTR2zzbPPIAHxEdAKAi+kJvE9cL//LhvPhFIG6SMHDe+EK68uDH+AAi+GwCf8jPhYDKAM+EQM6CgCDuaygAAAAAAAAAAAAAAAAAAc8LjlnIz5CWCjp6zszNyXH7AANoMPhG8uBM+EJu4wDR2zwhjhwj0NMB+kAwMcjPhyDOghDBD1HyzwuBy3/JcPsAkTDi4wDyAB8TGgAE+EsEUCCCEA8iy9m64wIgghA2m184uuMCIIIQN7ocI7rjAiCCEDpQ/Hi64wIcGRcVAz4w+Eby4Ez4Qm7jACGT1NHQ3vpA1NHQ+kDR2zzbPPIAHxYdAJ4h+kJvE9cL//LhvPhFIG6SMHDe+EK68uDH+AAh+GwBf8jPhYDKAM+EQM6CgCDuaygAAAAAAAAAAAAAAAAAAc8LjgHIz5D7vupGzs3JcfsAAyYw+Eby4Ez4Qm7jANTR2zzbPPIAHxgdAEJopvtg8tBI+En4TMcFIJww+En6Qm8T1wv/wwDe8uDG+GoDZjD4RvLgTPhCbuMA0ds8IY4bI9DTAfpAMDHIz4cgzoIQtptfOM8LgczJcPsAkTDi4wDyAB8bGgAo7UTQ0//TPzH4Q1jIy//LP87J7VQABPhKA0Aw+Eby4Ez4Qm7jACGT1NHQ3vpA1NHQ+kDU0ds82zzyAB8eHQA0+Ev4SvhD+ELIy//LP8+DzMt/+EzIzs3J7VQAoCL6Qm8T1wv/8uG8+EUgbpIwcN74Qrry4Mf4ACL4bAJ/yM+FgMoAz4RAzoKAIO5rKAAAAAAAAAAAAAAAAAABzwuOWcjPkVIoYt7OzM3JcfsAADrtRNDT/9M/0wAx1NN/1NHQ+kDR+Gz4a/hq+GP4YgAK+Eby4EwCCvSkIPShIyIAFHNvbCAwLjY0LjAAAA==");
  }

  public DeployHandle<ExampleConsumer> prepareDeploy(Sdk sdk, Credentials credentials) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of();
    return new DeployHandle<ExampleConsumer>(ExampleConsumer.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
