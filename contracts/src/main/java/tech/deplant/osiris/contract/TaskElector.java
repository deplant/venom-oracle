package tech.deplant.osiris.contract;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Map;

public interface TaskElector extends Contract {

	record TaskDetails(String body, String consensusType, Integer minValidators, BigInteger executionFee) {}

	public FunctionHandle<Void> onCommit(BigInteger commitHash_);

	public FunctionHandle<Void> onRequestRevealPhase();

	public FunctionHandle<Void> onReveal(TvmCell revealValue_, BigInteger revealSalt_);

	public FunctionHandle<Void> onRequestFinalPhase();

	public FunctionHandle<TaskMedianizedOnRequest.ResultOf_factory> _factory();

	public FunctionHandle<TaskMedianizedOnRequest.ResultOf_owner> _owner();
	public FunctionHandle<TaskMedianizedOnRequest.ResultOf_name> _name();

	public FunctionHandle<TaskMedianizedOnRequest.ResultOf_taskSettings> _taskSettings();

	public FunctionHandle<TaskMedianizedOnRequest.ResultOf_factorySettings> _factorySettings();

	public FunctionHandle<TaskMedianizedOnRequest.ResultOf_currElectionState> _currElectionState();

	public FunctionHandle<TaskMedianizedOnRequest.ResultOf_prevElectionState> _prevElectionState();

	public FunctionHandle<TaskMedianizedOnRequest.ResultOf_storage> _storage();

	default String jsonBody() throws EverSdkException {
		return _taskSettings().get()._taskSettings().get("body").toString();
	}
}
