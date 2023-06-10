package tech.deplant.osiris.contract.test.junit.compile;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;

import java.io.IOException;

import static tech.deplant.osiris.contract.test.junit.TestContext.INIT;
import static tech.deplant.osiris.contract.test.junit.TestContext.LOCAL_CONFIG;

public class CompileTests {

	public static final System.Logger logger = System.getLogger(tech.deplant.osiris.contract.test.junit.compile.CompileTests.class.getName());

	@BeforeAll
	public static void getContext() throws IOException {
		INIT();
	}

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	public void compile_consumer() throws JsonProcessingException, EverSdkException {
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/consumer",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "ExampleConsumer.tsol",
		                             "ExampleConsumer");
	}


	@Test
	@Execution(ExecutionMode.CONCURRENT)
	public void compile_for_validators() throws JsonProcessingException, EverSdkException {
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/subscription",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "TaskSubscription.tsol",
		                             "TaskSubscription");
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/validator",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "OracleValidator.tsol",
		                             "OracleValidator");
	}

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	public void compile_for_tasks() throws JsonProcessingException, EverSdkException {
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/task",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "TaskMedianizedOnRequest.tsol",
		                             "TaskMedianizedOnRequest");
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/task",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "TaskMedianizedValueFeed.tsol",
		                             "TaskMedianizedValueFeed");
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/task",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "TaskPreciseOnRequest.tsol",
		                             "TaskPreciseOnRequest");
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/task",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "TaskPreciseValueFeed.tsol",
		                             "TaskPreciseValueFeed");
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/task",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "Factory.tsol",
		                             "Factory");
	}

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	public void compile_test_faucet() throws JsonProcessingException, EverSdkException {
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/tip3/tst",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "TestFaucet.tsol",
		                             "TestFaucet");
	}

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	public void compile_for_bank() throws IOException, EverSdkException {
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/bank",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "BankStorage.tsol",
		                             "BankStorage");
		LOCAL_CONFIG.compileTemplate("C:/idea_git/iris-contracts-core/src/main/solidity/bank",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts",
		                             "BankRoot.tsol",
		                             "BankRoot");
	}


}
