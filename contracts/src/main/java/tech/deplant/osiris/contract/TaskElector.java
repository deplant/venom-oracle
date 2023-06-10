package tech.deplant.osiris.contract;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Map;

public sealed interface TaskElector extends Contract permits TaskMedianizedOnRequest, TaskMedianizedValueFeed, TaskPreciseOnRequest, TaskPreciseValueFeed {

}
