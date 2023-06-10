package tech.deplant.osiris;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.utils.Objs;
import tech.deplant.osiris.contract.*;

import java.math.BigInteger;
import java.util.Map;

public class Elector implements Contract {

	private final Sdk sdk;
	private final String address;
	private final ContractAbi abi;
	private final Credentials credentials;

	private TaskPreciseOnRequest taskPreciseOnRequest;
	private TaskMedianizedOnRequest taskMedianizedOnRequest;
	private TaskPreciseValueFeed taskPreciseValueFeed;
	private TaskMedianizedValueFeed taskMedianizedValueFeed;
	private final TaskType type;

	public Elector(TaskElector task) {
		switch(task) {
			case TaskPreciseOnRequest po -> {
				this.taskPreciseOnRequest = po;
				this.type = TaskType.PRECISE_IMMEDIATE;
			}
			case TaskMedianizedOnRequest mo -> {
				this.taskMedianizedOnRequest = mo;
				this.type = TaskType.MEDIAN_IMMEDIATE;
			}
			case TaskPreciseValueFeed pf -> {
				this.taskPreciseValueFeed = pf;
				this.type = TaskType.PRECISE_FEED;
			}
			case TaskMedianizedValueFeed mf -> {
				this.taskMedianizedValueFeed = mf;
				this.type = TaskType.MEDIAN_FEED;
			}
		}
		this.sdk = task.sdk();
		this.abi = task.abi();
		this.address = task.address();
		this.credentials = task.credentials();
	}

	public static Elector ofType(TaskType type, Sdk sdk, String address) throws JsonProcessingException {
		return switch (type) {
			case PRECISE_IMMEDIATE -> new Elector(new TaskPreciseOnRequest(sdk, address));
			case MEDIAN_IMMEDIATE -> new Elector(new TaskMedianizedOnRequest(sdk, address));
			case PRECISE_FEED -> new Elector(new TaskPreciseValueFeed(sdk, address));
			case MEDIAN_FEED -> new Elector(new TaskMedianizedValueFeed(sdk, address));
		};
	}


	public Map<String, Object> factorySettings() throws EverSdkException {
		Map<String, Object> result;
		if (Objs.isNotNull(taskPreciseOnRequest)) {
			result = this.taskPreciseOnRequest._factorySettings().get()._factorySettings();
		} else if (Objs.isNotNull(taskMedianizedOnRequest)) {
			result = this.taskMedianizedOnRequest._factorySettings().get()._factorySettings();
		} else if (Objs.isNotNull(taskPreciseValueFeed)) {
			result = this.taskPreciseValueFeed._factorySettings().get()._factorySettings();
		} else if (Objs.isNotNull(taskMedianizedValueFeed)) {
			result = this.taskMedianizedValueFeed._factorySettings().get()._factorySettings();
		} else  {
			result = Map.of();
		}
		return result;
	}

	public Map<String, Object> taskSettings() throws EverSdkException {
		Map<String, Object> result;
		if (Objs.isNotNull(taskPreciseOnRequest)) {
			result = this.taskPreciseOnRequest._taskSettings().get()._taskSettings();
		} else if (Objs.isNotNull(taskMedianizedOnRequest)) {
			result = this.taskMedianizedOnRequest._taskSettings().get()._taskSettings();
		} else if (Objs.isNotNull(taskPreciseValueFeed)) {
			result = this.taskPreciseValueFeed._taskSettings().get()._taskSettings();
		} else if (Objs.isNotNull(taskMedianizedValueFeed)) {
			result = this.taskMedianizedValueFeed._taskSettings().get()._taskSettings();
		} else  {
			result = Map.of();
		}
		return result;
	}

	public Map<String, Object> currentState() throws EverSdkException {
		Map<String, Object> result;
		if (Objs.isNotNull(taskPreciseOnRequest)) {
			result = this.taskPreciseOnRequest._currElectionState().get()._currElectionState();
		} else if (Objs.isNotNull(taskMedianizedOnRequest)) {
			result = this.taskMedianizedOnRequest._currElectionState().get()._currElectionState();
		} else if (Objs.isNotNull(taskPreciseValueFeed)) {
			result = this.taskPreciseValueFeed._currElectionState().get()._currElectionState();
		} else if (Objs.isNotNull(taskMedianizedValueFeed)) {
			result = this.taskMedianizedValueFeed._currElectionState().get()._currElectionState();
		} else  {
			result = Map.of();
		}
		return result;
	}

	public Map<String, Object> previousState() throws EverSdkException {
		Map<String, Object> result;
		if (Objs.isNotNull(taskPreciseOnRequest)) {
			result = this.taskPreciseOnRequest._prevElectionState().get()._prevElectionState();
		} else if (Objs.isNotNull(taskMedianizedOnRequest)) {
			result = this.taskMedianizedOnRequest._prevElectionState().get()._prevElectionState();
		} else if (Objs.isNotNull(taskPreciseValueFeed)) {
			result = this.taskPreciseValueFeed._prevElectionState().get()._prevElectionState();
		} else if (Objs.isNotNull(taskMedianizedValueFeed)) {
			result = this.taskMedianizedValueFeed._prevElectionState().get()._prevElectionState();
		} else  {
			result = Map.of();
		}
		return result;
	}

	public TaskType type() {
		return type;
	}


	public Object factorySettingsProperty(String key) throws EverSdkException {
		return factorySettings().get(key);
	}

	public Object taskSettingsProperty(String key) throws EverSdkException {
		return taskSettings().get(key);
	}

	public String jsonBody() throws EverSdkException {
		return taskSettings().get("body").toString();
	}

	public Object currentStateProperty(String key) throws EverSdkException {
		return currentState().get(key);
	}

	public ElectionPhase currentPhase() throws EverSdkException {
		return ElectionPhase.ofValue(Integer.parseInt(currentStateProperty("status").toString()));
	}

	public Object previousPhase() throws EverSdkException {
		return ElectionPhase.ofValue(Integer.parseInt(previousStateProperty("status").toString()));
	}

	public Object previousStateProperty(String key) throws EverSdkException {
		return previousState().get(key);
	}

	public Abi.DecodedMessageBody decodeEventBoc(String eventName, String messageBoc) throws EverSdkException {
		return Abi.decodeMessage(sdk().context(),
		                         abi().ABI(),
		                         messageBoc,
		                         false, eventName,null);
	}

	@Override
	public Sdk sdk() {
		return this.sdk;
	}

	@Override
	public String address() {
		return this.address;
	}

	@Override
	public ContractAbi abi() {
		return this.abi;
	}

	@Override
	public Credentials credentials() {
		return this.credentials;
	}
}
