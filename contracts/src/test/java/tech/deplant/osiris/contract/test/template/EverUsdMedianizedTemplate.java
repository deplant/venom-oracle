package tech.deplant.osiris.contract.test.template;

public class EverUsdMedianizedTemplate implements StoredTaskTemplate {

	@Override
	public String name() {
		return "ETH/USD Medianized Price Feed";
	}

	@Override
	public String json() {
		return "{\n" +
		       "  \"version\" : 1,\n" +
		       "  \"name\" : \"ETH/USD Price (Price Feed)\",\n" +
		       "  \"consensus\" : {\n" +
		       "    \"type\" : \"MEDIAN\",\n" +
		       "    \"finalActionId\" : \"8\",\n" +
		       "    \"minValidators\" : 3,\n" +
		       "    \"maxFee\" : 1000000000\n" +
		       "  },\n" +
		       "  \"trigger\" : {\n" +
		       "    \"type\" : \"VALUE_FEED\",\n" +
		       "    \"pctThreshold\" : 0.05,\n" +
		       "    \"absoluteThreshold\" : 50000,\n" +
		       "    \"timerThreshold\" : 3600\n" +
		       "  },\n" +
		       "  \"externalId\" : \"\",\n" +
		       "  \"actions\" : [ {\n" +
		       "    \"type\" : \"HTTP\",\n" +
		       "    \"id\" : \"1\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"method\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"GET\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    },\n" +
		       "    \"uri\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"https://data.gateapi.io/api2/1/ticker/ETH_USDT\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    },\n" +
		       "    \"body\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : true\n" +
		       "    },\n" +
		       "    \"headers\" : [ {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"Content-Type: application/json\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : true\n" +
		       "    } ]\n" +
		       "  }, {\n" +
		       "    \"type\" : \"HTTP\",\n" +
		       "    \"id\" : \"2\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"method\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"GET\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    },\n" +
		       "    \"uri\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"https://api.kucoin.com/api/v1/market/orderbook/level1?symbol=ETH-USDT\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    },\n" +
		       "    \"body\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : true\n" +
		       "    },\n" +
		       "    \"headers\" : [ {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"Content-Type: application/json\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : true\n" +
		       "    } ]\n" +
		       "  }, {\n" +
		       "    \"type\" : \"HTTP\",\n" +
		       "    \"id\" : \"3\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"method\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"GET\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    },\n" +
		       "    \"uri\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    },\n" +
		       "    \"body\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : true\n" +
		       "    },\n" +
		       "    \"headers\" : [ {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"Content-Type: application/json\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : true\n" +
		       "    } ]\n" +
		       "  }, {\n" +
		       "    \"type\" : \"JSON_PARSE\",\n" +
		       "    \"id\" : \"5\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"data\" : {\n" +
		       "      \"type\" : \"REF\",\n" +
		       "      \"ref\" : \"1\"\n" +
		       "    },\n" +
		       "    \"path\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"/last\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"JSON_PARSE\",\n" +
		       "    \"id\" : \"6\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"data\" : {\n" +
		       "      \"type\" : \"REF\",\n" +
		       "      \"ref\" : \"2\"\n" +
		       "    },\n" +
		       "    \"path\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"/data/price\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"JSON_PARSE\",\n" +
		       "    \"id\" : \"7\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"data\" : {\n" +
		       "      \"type\" : \"REF\",\n" +
		       "      \"ref\" : \"3\"\n" +
		       "    },\n" +
		       "    \"path\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"/ethereum/usd\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    }\n" +
		       "  }, {\n" +
		       "    \"type\" : \"AVERAGE_MEDIAN\",\n" +
		       "    \"id\" : \"8\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"precision\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"6\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    },\n" +
		       "    \"values\" : [ {\n" +
		       "      \"type\" : \"REF\",\n" +
		       "      \"ref\" : \"5\"\n" +
		       "    }, {\n" +
		       "      \"type\" : \"REF\",\n" +
		       "      \"ref\" : \"6\"\n" +
		       "    }, {\n" +
		       "      \"type\" : \"REF\",\n" +
		       "      \"ref\" : \"7\"\n" +
		       "    } ]\n" +
		       "  }, {\n" +
		       "    \"type\" : \"MULTIPLY\",\n" +
		       "    \"id\" : \"10\",\n" +
		       "    \"responseType\" : \"STRING\",\n" +
		       "    \"first\" : {\n" +
		       "      \"type\" : \"REF\",\n" +
		       "      \"ref\" : \"8\"\n" +
		       "    },\n" +
		       "    \"second\" : {\n" +
		       "      \"type\" : \"VAL\",\n" +
		       "      \"val\" : \"1000000000\",\n" +
		       "      \"responseType\" : \"STRING\",\n" +
		       "      \"nullable\" : false\n" +
		       "    }\n" +
		       "  } ]\n" +
		       "}";

//	@Override
//	public String json() {
//		return "{\n" +
//		       "  \"version\" : 1,\n" +
//		       "  \"name\" : \"1\",\n" +
//		       "  \"trigger\" : {\n" +
//		       "    \"type\" : \"IMMEDIATE\"\n" +
//		       "  },\n" +
//		       "  \"externalId\" : \"1\",\n" +
//		       "  \"actions\" : [ {\n" +
//		       "    \"type\" : \"HTTP\",\n" +
//		       "    \"id\" : \"0\",\n" +
//		       "    \"responseType\" : \"STRING\",\n" +
//		       "    \"method\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"GET\"\n" +
//		       "    },\n" +
//		       "    \"uri\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"https://cex.io/api/ticker/EVER/USD\"\n" +
//		       "    },\n" +
//		       "    \"headers\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"application/json\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"HTTP\",\n" +
//		       "    \"id\" : \"1\",\n" +
//		       "    \"responseType\" : \"STRING\",\n" +
//		       "    \"method\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"GET\"\n" +
//		       "    },\n" +
//		       "    \"uri\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"https://data.gateapi.io/api2/1/ticker/EVER_USDT\"\n" +
//		       "    },\n" +
//		       "    \"headers\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"application/json\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"HTTP\",\n" +
//		       "    \"id\" : \"2\",\n" +
//		       "    \"responseType\" : \"STRING\",\n" +
//		       "    \"method\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"GET\"\n" +
//		       "    },\n" +
//		       "    \"uri\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"https://api.kucoin.com/api/v1/market/orderbook/level1?symbol=EVER-USDT\"\n" +
//		       "    },\n" +
//		       "    \"headers\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"application/json\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"HTTP\",\n" +
//		       "    \"id\" : \"3\",\n" +
//		       "    \"responseType\" : \"STRING\",\n" +
//		       "    \"method\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"GET\"\n" +
//		       "    },\n" +
//		       "    \"uri\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"https://api.coingecko.com/api/v3/simple/price?ids=everscale&vs_currencies=usd\"\n" +
//		       "    },\n" +
//		       "    \"headers\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"application/json\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"JSON_PARSE\",\n" +
//		       "    \"id\" : \"4\",\n" +
//		       "    \"responseType\" : \"UINT256\",\n" +
//		       "    \"data\" : {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"0\"\n" +
//		       "    },\n" +
//		       "    \"path\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"/last\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"JSON_PARSE\",\n" +
//		       "    \"id\" : \"5\",\n" +
//		       "    \"responseType\" : \"UINT256\",\n" +
//		       "    \"data\" : {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"1\"\n" +
//		       "    },\n" +
//		       "    \"path\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"/last\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"JSON_PARSE\",\n" +
//		       "    \"id\" : \"6\",\n" +
//		       "    \"responseType\" : \"UINT256\",\n" +
//		       "    \"data\" : {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"2\"\n" +
//		       "    },\n" +
//		       "    \"path\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"/data/price\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"JSON_PARSE\",\n" +
//		       "    \"id\" : \"7\",\n" +
//		       "    \"responseType\" : \"UINT256\",\n" +
//		       "    \"data\" : {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"3\"\n" +
//		       "    },\n" +
//		       "    \"path\" : {\n" +
//		       "      \"type\" : \"ConstStringAction\",\n" +
//		       "      \"input\" : \"/everscale/usd\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"AVERAGE_MEDIAN\",\n" +
//		       "    \"id\" : \"8\",\n" +
//		       "    \"responseType\" : \"UINT256\",\n" +
//		       "    \"values\" : [ {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"4\"\n" +
//		       "    }, {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"5\"\n" +
//		       "    }, {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"6\"\n" +
//		       "    }, {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"7\"\n" +
//		       "    } ]\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"PRECISE_CONSENSUS\",\n" +
//		       "    \"id\" : \"9\",\n" +
//		       "    \"responseAction\" : {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"10\"\n" +
//		       "    }\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"ABI_ENCODE\",\n" +
//		       "    \"id\" : \"10\",\n" +
//		       "    \"storeList\" : [ {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"11\"\n" +
//		       "    }]\n" +
//		       "  }, {\n" +
//		       "    \"type\" : \"MULTIPLY\",\n" +
//		       "    \"id\" : \"11\",\n" +
//		       "    \"responseType\" : \"UINT256\",\n" +
//		       "    \"first\" : {\n" +
//		       "      \"type\" : \"PlaceholderStringAction\",\n" +
//		       "      \"id\" : \"8\"\n" +
//		       "    },\n" +
//		       "    \"second\" : {\n" +
//		       "      \"type\" : \"ConstBigDecimalAction\",\n" +
//		       "      \"input\" : \"1000000000\"\n" +
//		       "    }\n" +
//		       "  } ]\n" +
//		       "}";
//	}
	}
}
