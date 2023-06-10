package tech.deplant.osiris.contract.test.template;

public class EverUsdMedianizedTemplate implements StoredTaskTemplate {

	@Override
	public String name() {
		return "EVER/USD Medianized Price Feed";
	}

	@Override
	public String json() {
		return "{\n" +
		       "  \"version\" : 1,\n" +
		       "  \"name\" : \"1\",\n" +
		       "  \"trigger\" : {\n" +
		       "    \"type\" : \"IMMEDIATE\"\n" +
		       "  },\n" +
		       "  \"externalId\" : \"1\",\n" +
		       "  \"actions\" : [ {\n" +
		       "    \"type\" : \"HTTP\",\n" +
		       "    \"id\" : \"0\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"method\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"GET\"\n" +
		       "    },\n" +
		       "    \"uri\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"https://cex.io/api/ticker/EVER/USD\"\n" +
		       "    },\n" +
		       "    \"headers\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"application/json\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"HTTP\",\n" +
		       "    \"id\" : \"1\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"method\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"GET\"\n" +
		       "    },\n" +
		       "    \"uri\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"https://data.gateapi.io/api2/1/ticker/EVER_USDT\"\n" +
		       "    },\n" +
		       "    \"headers\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"application/json\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"HTTP\",\n" +
		       "    \"id\" : \"2\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"method\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"GET\"\n" +
		       "    },\n" +
		       "    \"uri\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"https://api.kucoin.com/api/v1/market/orderbook/level1?symbol=EVER-USDT\"\n" +
		       "    },\n" +
		       "    \"headers\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"application/json\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"HTTP\",\n" +
		       "    \"id\" : \"3\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"method\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"GET\"\n" +
		       "    },\n" +
		       "    \"uri\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"https://api.coingecko.com/api/v3/simple/price?ids=everscale&vs_currencies=usd\"\n" +
		       "    },\n" +
		       "    \"headers\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"application/json\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"JSON_PARSE\",\n" +
		       "    \"id\" : \"4\",\n" +
		       "    \"responseType\" : \"UINT256\",\n" +
		       "    \"data\" : {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"0\"\n" +
		       "    },\n" +
		       "    \"path\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"/last\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"JSON_PARSE\",\n" +
		       "    \"id\" : \"5\",\n" +
		       "    \"responseType\" : \"UINT256\",\n" +
		       "    \"data\" : {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"1\"\n" +
		       "    },\n" +
		       "    \"path\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"/last\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"JSON_PARSE\",\n" +
		       "    \"id\" : \"6\",\n" +
		       "    \"responseType\" : \"UINT256\",\n" +
		       "    \"data\" : {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"2\"\n" +
		       "    },\n" +
		       "    \"path\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"/data/price\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"JSON_PARSE\",\n" +
		       "    \"id\" : \"7\",\n" +
		       "    \"responseType\" : \"UINT256\",\n" +
		       "    \"data\" : {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"3\"\n" +
		       "    },\n" +
		       "    \"path\" : {\n" +
		       "      \"type\" : \"ConstStringAction\",\n" +
		       "      \"input\" : \"/everscale/usd\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"AVERAGE_MEDIAN\",\n" +
		       "    \"id\" : \"8\",\n" +
		       "    \"responseType\" : \"UINT256\",\n" +
		       "    \"values\" : [ {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"4\"\n" +
		       "    }, {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"5\"\n" +
		       "    }, {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"6\"\n" +
		       "    }, {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"7\"\n" +
		       "    } ]\n" +
		       "  }, {\n" +
		       "    \"type\" : \"PRECISE_CONSENSUS\",\n" +
		       "    \"id\" : \"9\",\n" +
		       "    \"responseAction\" : {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"10\"\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"ABI_ENCODE\",\n" +
		       "    \"id\" : \"10\",\n" +
		       "    \"storeList\" : [ {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"11\"\n" +
		       "    }]\n" +
		       "  }, {\n" +
		       "    \"type\" : \"MULTIPLY\",\n" +
		       "    \"id\" : \"11\",\n" +
		       "    \"responseType\" : \"UINT256\",\n" +
		       "    \"first\" : {\n" +
		       "      \"type\" : \"PlaceholderStringAction\",\n" +
		       "      \"id\" : \"8\"\n" +
		       "    },\n" +
		       "    \"second\" : {\n" +
		       "      \"type\" : \"ConstBigDecimalAction\",\n" +
		       "      \"input\" : \"1000000000\"\n" +
		       "    }\n" +
		       "  } ]\n" +
		       "}";
	}
}
