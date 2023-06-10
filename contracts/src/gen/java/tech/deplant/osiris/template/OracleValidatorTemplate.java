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
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"name_\",\"type\":\"string\"},{\"name\":\"logo_\",\"type\":\"string\"}],\"outputs\":[]},{\"name\":\"commit\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"commitHash_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"reveal\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"revealValue_\",\"type\":\"cell\"},{\"name\":\"revealSalt_\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"requestRevealPhase\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"requestFinalPhase\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"refreshFeed\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"getCommitHash\",\"inputs\":[{\"name\":\"response_\",\"type\":\"cell\"},{\"name\":\"salt_\",\"type\":\"uint256\"}],\"outputs\":[{\"name\":\"valueHash\",\"type\":\"uint256\"}]},{\"name\":\"subscribe\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"lastProcessedTx_\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"subscriptionAddress\",\"type\":\"address\"}]},{\"name\":\"unsubscribe\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"saveRequestTx\",\"inputs\":[{\"name\":\"taskAddress_\",\"type\":\"address\"},{\"name\":\"lastProcessedTx_\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"setSubscriptionCode\",\"inputs\":[{\"name\":\"subscriptionCode_\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"getSubscriptionCodeHash\",\"inputs\":[],\"outputs\":[{\"name\":\"codeHash\",\"type\":\"uint256\"}]},{\"name\":\"_subscriptionCode\",\"inputs\":[],\"outputs\":[{\"name\":\"_subscriptionCode\",\"type\":\"cell\"}]},{\"name\":\"_name\",\"inputs\":[],\"outputs\":[{\"name\":\"_name\",\"type\":\"string\"}]},{\"name\":\"_logo\",\"inputs\":[],\"outputs\":[{\"name\":\"_logo\",\"type\":\"string\"}]}],\"events\":[],\"data\":[{\"key\":1,\"name\":\"_subscriptionCode\",\"type\":\"cell\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"_subscriptionCode\",\"type\":\"cell\"},{\"name\":\"_name\",\"type\":\"string\"},{\"name\":\"_logo\",\"type\":\"string\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("te6ccgECOQEACBIAAgE0AwEBAcACAEPQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgBCSK7VMg4wMgwP/jAiDA/uMC8gs2BQQ4AvLtRNDXScMB+GaNCGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAT4aSHbPNMAAY4XgwjXGCD4KMjOzsn5AFj4QiD4ZfkQ8qje0z8B+EMhufK0IPgjgQPoqIIIG3dAoLnytPhj0x8B+CO88rnTHwHbPPI8JAYDUu1E0NdJwwH4ZiLQ0wP6QDD4aak4ANwhxwDjAiHXDR/yvCHjAwHbPPI8NTUGBFAgghAmAH0Pu+MCIIIQUR9DmLvjAiCCEHSDP5674wIgghB+Sovwu+MCJxgOBwM8IIIQeWijvrrjAiCCEHzBVCq64wIgghB+SovwuuMCDAoIAi4w+Eby4Ewhk9TR0N76QNP/0ds84wDyAAkwAZBopvtgwP/y4Ef4ACH6Qm8T1wv/8uEtIPLhLIIAw1D4KPpCbxLbPFh/yM+FgMoAz4RAzgH6AnHPC2oByM+R9LRbKsv/zclx+wAyA2gw+Eby4Ez4Qm7jANHbPCGOHCPQ0wH6QDAxyM+HIM6CEPzBVCrPC4HL/8lw+wCRMOLjAPIALAswAB5opvtgwP/y4Ef4APhK+QACKjD4RvLgTCGT1NHQ3vpA0ds84wDyAA0wAXxopvtgwP/y4Ef4ACD6Qm8T1wv/8uEtggMNQPgo+kJvEts8AX/Iz4WAygDPhEDOAfoCghBhbF4/zwuKyXH7ADIEUCCCEFEs0NW64wIgghBT/UkhuuMCIIIQYKdyaLrjAiCCEHSDP5664wIWEhEPAzgw+Eby4Ez4Qm7jACGT1NHQ3vpA0z/R2zzjAPIALBAwAnRopvtgwP/y4Ef4AAHbPDABggDDUPgo+kJvEts8WH/Iz4WAygDPhEDOAfoCghBW+EJSzwuKyz/JcfsAFDIBTjDR2zz4TCGOG40EcAAAAAAAAAAAAAAAADgp3JogyM7MyXD7AN7yACwDNDD4RvLgTPhCbuMAIZPU0dDe+kDR2zzjAPIALBMwAmxopvtgwP/y4Ef4ANs8MIIAw1D4KPpCbxLbPAF/yM+FgMoAz4RAzgH6AoIQDNp37c8Lislx+wAUMgFc+ELIy/9wbYBA9EMBcViAQPQW+ChyWIBA9BbI9ADJ+ErIz4SA9AD0AM+Byds8ARUAGCD5AMjPigBAy//J0AJkMPhG8uBM1NP/0ds8IY4cI9DTAfpAMDHIz4cgzoIQ0SzQ1c8Lgcv/yXD7AJEw4uMA8gAXMAAQAcjMy//J+QAEUCCCECooPxW64wIgghAwBDDQuuMCIIIQMNTV77rjAiCCEFEfQ5i64wIiHBsZAiow+Eby4Ewhk9TR0N76QNHbPOMA8gAaMAF8aKb7YMD/8uBH+AAg+kJvE9cL//LhLYIAw1D4KPpCbxLbPAF/yM+FgMoAz4RAzgH6AoIQEiWds88Lislx+wAyAU4w0ds8+EshjhuNBHAAAAAAAAAAAAAAAAAsNTV74MjOzMlw+wDe8gAsAyYw+Eby4Ez4Qm7jANTR2zzbPPIALB0jAT5opvtgwP/y4Ef4APgo+kJvE9cL/8jL/wHQAcnbPPhqHgIWIYs4rbNYxwWKiuIgHwEIAds8ySEBJgHU1DAS0Ns8yM+OK2zWEszPEckhAWbViy9KQNcm9ATTCTEg10qR1I6A4osvShjXJjAByM+L0pD0AIAgzwsJz4vShswSzMjPEc40AjAw+EJu4wD4RvJz1NTR+AAB+Gv4bNs88gAkIwAu+Ez4S/hK+EP4QsjL/8s/z4PMzMzJ7VQCFu1E0NdJwgGOgOMNJSwCSnDtRND0BXEhgED0D46A34gg+Gz4a/hqgED0DvK91wv/+GJw+GMmOAECiDgEUCCCEBw3fOO64wIgghAcgxxZuuMCIIIQH3MDirrjAiCCECYAfQ+64wIvLSsoA4Iw+Eby4Ez4Qm7jACGT1NHQ3vpA0z/R2zwhjh8j0NMB+kAwMcjPhyDOcc8LYQHIz5KYAfQ+zs3JcPsAkTDi4wDyACwpMALqaKb7YMD/8uBH+AAh+ELIy/9wbYBA9ENVAnFYgED0FvgocliAQPQWyPQAyfhKyM+EgPQA9ADPgckg+QDIz4oAQMv/ydBVIIIBhqD4KPpCbxLbPCTIz4WIzgH6AnPPC2oh2zzMz4NZyM+QND96ss7LP83JcfsAMioANNDSAAGT0gQx3tIAAZPSATHe9AT0BPQE0V8DAU4w0ds8+EohjhuNBHAAAAAAAAAAAAAAAAAn3MDioMjOzMlw+wDe8gAsADDtRNDT/9M/0wAx1NTU0fhs+Gv4avhj+GICMDD4RvLgTCGT1NHQ3vpA1NP/0ds84wDyAC4wAY5opvtgwP/y4Ef4ACL6Qm8T1wv/8uEtAYIAw1D4KPpCbxLbPFUCf8jPhYDKAM+EQM4B+gJxzwtqWcjPkNKwcArMy//NyXH7ADICKjD4RvLgTCGT1NHQ3vpA0ds84wDyADEwACjtRNDT/9M/MfhDWMjL/8s/zsntVAF8aKb7YMD/8uBH+AAg+kJvE9cL//LhLYIAw1D4KPpCbxLbPAF/yM+FgMoAz4RAzgH6AoIQP3bTYc8Lislx+wAyAQzbPIMPqYYzAU4gwAAhwP+x8uBDgBSAFeME+DIgjoDf8uBE0NMH0z/TP9MH1ws/bEE0AQSIATgACvhG8uBMAgr0pCD0oTg3ABRzb2wgMC42NC4wAAA=");
  }

  public DeployHandle<OracleValidator> prepareDeploy(Sdk sdk, Credentials credentials,
      TvmCell _subscriptionCode, String name_, String logo_) {
    Map<String, Object> initialDataFields = Map.of("_subscriptionCode", _subscriptionCode);
    Map<String, Object> params = Map.of("name_", name_, 
        "logo_", logo_);
    return new DeployHandle<OracleValidator>(OracleValidator.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
