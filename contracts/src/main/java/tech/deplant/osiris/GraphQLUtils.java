package tech.deplant.osiris;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GraphQLUtils {

	public static String accountsByCodeHash(String[] codeHashes, String returnFields, String orderBy, boolean orderAsc, int limit) {
		String filter = null;
		if (codeHashes.length > 1) {
			filter  = "in: [\"%s\"]".formatted(Arrays.stream(codeHashes).collect(Collectors.joining("\",\"")));
		} else if (codeHashes.length == 1) {
			filter = "eq: \"%s\"".formatted(codeHashes[0]);
		} else {
			throw new RuntimeException("No code hashes provided for GQL request!");
		}
		String gqlTemplate = """
				{
				  accounts(
				  filter: {
					code_hash: {
					  %s
					}
					acc_type: { eq: 1 }
				  }
				  orderBy: { direction: %s, path: "%s" }
				  limit: %d
				  ) {
				    %s
				  }
				}""".formatted(
						filter,
						orderAsc? "ASC":"DESC",
						orderBy,
						limit,
						returnFields
						);

		return gqlTemplate;
	}
}
