package tech.deplant.osiris.contract;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.contract.Contract;

public interface Subscription extends Contract {

	default String getLocalTaskAddress(String boc) throws EverSdkException {
		return _taskAddress()
				.getLocal(boc)
				._taskAddress()
				.makeAddrStd();
	}

	default Long getLocalLastRequestTx(String boc) throws EverSdkException {
		return _lastTx().getLocal(boc)._lastTx();
	}

	default Long getLastRequestTx() throws EverSdkException {
		return _lastTx().get()._lastTx();
	}

	default String getTaskAddress() throws EverSdkException {
		return _taskAddress()
				.get()
				._taskAddress()
				.makeAddrStd();
	}

	FunctionHandle<Void> cancel();

	FunctionHandle<Void> updateLastTx(Long lastTx_);

	FunctionHandle<TaskSubscription.ResultOf_taskAddress> _taskAddress();

	FunctionHandle<TaskSubscription.ResultOf_oracleAddress> _oracleAddress();

	FunctionHandle<TaskSubscription.ResultOf_lastTx> _lastTx();
}
