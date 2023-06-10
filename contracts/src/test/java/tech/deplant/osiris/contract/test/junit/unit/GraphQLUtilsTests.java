package tech.deplant.osiris.contract.test.junit.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.osiris.GraphQLUtils;

import java.io.IOException;

import static tech.deplant.osiris.contract.test.junit.TestContext.INIT;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GraphQLUtilsTests {

	@BeforeAll
	public static void getContext() throws IOException {
		INIT();
	}

	@Test
	public void check_gql_template_accountsByCodeHash() throws JsonProcessingException, EverSdkException {
		String s = GraphQLUtils.accountsByCodeHash(new String[]{"7bb4f2878d2167862692a8c4099037ba3f6dea2a1d8fe87e3a17c142a1f1b02e","aedb3263ed38b4481fa369f8a1d43be813fb7181b50aed9c37c757c7d3157a40"},
		                                           "id code_hash last_trans_lt",
		                                           "last_trans_lt",
		                                           false,
		                                           10);
		System.out.println(s);
	}
}
