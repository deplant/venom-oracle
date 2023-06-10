package tech.deplant.osiris.node.tst.integration.json;

public class JsonTemplateParseTests {

	public static final String MULTIPLY_2_AND_ENCODE =
			"{\n" +
			"  \"version\" : 1,\n" +
			"  \"name\" : \"super_taks\",\n" +
			"  \"trigger\" : {\n" +
			"    \"type\" : \"IMMEDIATE\"\n" +
			"  },\n" +
			"  \"externalId\" : \"yyy\",\n" +
			"  \"actions\" : [ {\n" +
			"    \"type\" : \"MULTIPLY\",\n" +
			"    \"id\" : \"0b\",\n" +
			"    \"responseType\" : \"UINT256\",\n" +
			"    \"first\" : {\n" +
			"      \"type\" : \"PlaceholderStringAction\",\n" +
			"      \"id\" : \"0c\"\n" +
			"    },\n" +
			"    \"second\" : {\n" +
			"      \"type\" : \"ConstStringAction\",\n" +
			"      \"input\" : \"3\"\n" +
			"    }\n" +
			"  },{\n" +
			"    \"type\" : \"MULTIPLY\",\n" +
			"    \"id\" : \"0a\",\n" +
			"    \"responseType\" : \"UINT256\",\n" +
			"    \"first\" : {\n" +
			"      \"type\" : \"PlaceholderStringAction\",\n" +
			"      \"id\" : \"0c\"\n" +
			"    },\n" +
			"    \"second\" : {\n" +
			"      \"type\" : \"ConstStringAction\",\n" +
			"      \"input\" : \"4\"\n" +
			"    }\n" +
			"  },{\n" +
			"    \"type\" : \"MULTIPLY\",\n" +
			"    \"id\" : \"0c\",\n" +
			"    \"responseType\" : \"UINT256\",\n" +
			"    \"first\" : {\n" +
			"      \"type\" : \"ConstStringAction\",\n" +
			"      \"input\" : \"2\"\n" +
			"    },\n" +
			"    \"second\" : {\n" +
			"      \"type\" : \"ConstStringAction\",\n" +
			"      \"input\" : \"5\"\n" +
			"    }\n" +
			"  }, {\n" +
			"    \"type\" : \"PRECISE_CONSENSUS\",\n" +
			"    \"id\" : \"2\",\n" +
			"    \"responseAction\" : {\n" +
			"      \"type\" : \"PlaceholderStringAction\",\n" +
			"      \"id\" : \"1\"\n" +
			"    }\n" +
			"  }, {\n" +
			"    \"type\" : \"ABI_ENCODE\",\n" +
			"    \"id\" : \"1\",\n" +
			"    \"storeList\" : [ {\n" +
			"      \"type\" : \"PlaceholderStringAction\",\n" +
			"      \"id\" : \"0a\"\n" +
			"    },{\n" +
			"      \"type\" : \"PlaceholderStringAction\",\n" +
			"      \"id\" : \"0b\"\n" +
			"    } ]\n" +
			"  } ]\n" +
			"}";


	public static final String MULTIPLY_AND_ENCODE =
			"{\n" +
			"  \"version\" : 1,\n" +
			"  \"name\" : \"super_taks\",\n" +
			"  \"trigger\" : {\n" +
			"    \"type\" : \"IMMEDIATE\"\n" +
			"  },\n" +
			"  \"externalId\" : \"yyy\",\n" +
			"  \"actions\" : [ {\n" +
			"    \"type\" : \"MULTIPLY\",\n" +
			"    \"id\" : \"0\",\n" +
			"    \"responseType\" : \"UINT256\",\n" +
			"    \"first\" : {\n" +
			"      \"type\" : \"ConstStringAction\",\n" +
			"      \"input\" : \"10\"\n" +
			"    },\n" +
			"    \"second\" : {\n" +
			"      \"type\" : \"ConstStringAction\",\n" +
			"      \"input\" : \"3\"\n" +
			"    }\n" +
			"  }, {\n" +
			"    \"type\" : \"PRECISE_CONSENSUS\",\n" +
			"    \"id\" : \"2\",\n" +
			"    \"responseAction\" : {\n" +
			"      \"type\" : \"PlaceholderStringAction\",\n" +
			"      \"id\" : \"1\"\n" +
			"    }\n" +
			"  }, {\n" +
			"    \"type\" : \"ABI_ENCODE\",\n" +
			"    \"id\" : \"1\",\n" +
			"    \"storeList\" : [ {\n" +
			"      \"type\" : \"PlaceholderStringAction\",\n" +
			"      \"id\" : \"0\"\n" +
			"    } ]\n" +
			"  } ]\n" +
			"}";

	public static final String ORTODOX_JSON_2 = "{\n" +
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

//	@Test
//	public void equals_metadata_on_good_template() throws TemplateProcessingException, JsonProcessingException {
//		//TODO Check every null or empty value
//		TaskTemplate template = TaskTemplate.deserialize(ORTODOX_JSON_2.replaceAll("[\u0000-\u001f]", ""));
//		assertEquals(1, template.version());
//		assertEquals("Immediate trigger!", template.trigger().description());
//	}
//
//	@Test
//	public void throws_on_bad_json_template() throws TemplateProcessingException {
//		assertThrows(TemplateProcessingException.class,
//		             () -> TaskTemplate.deserialize(JsonExamples.TASK_VERSION_1_JSON_1));
//	}


}
