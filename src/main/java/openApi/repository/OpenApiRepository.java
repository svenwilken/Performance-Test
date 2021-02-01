package openApi.repository;
import global.Parameters;
import openApi.model.OpenApiEntity;

public abstract class OpenApiRepository {
  private static OpenApiRepository instance;

  public static OpenApiRepository getInstance() {
    if(OpenApiRepository.instance == null) {
      switch (Parameters.DATABASE) {
        case MONGO_DB:
          OpenApiRepository.instance = new MongoOpenApiRepository();
          break;
        case FIREBASE:
          OpenApiRepository.instance = new FirebaseOpenApiRepository();
          break;
        default:
          System.out.println("No Database defined");
          break;
      }
    }
    return OpenApiRepository.instance;
  }
  
  protected static final String PERFORMANCE_TEST = "PerformanceTest";

  public abstract OpenApiEntity save(OpenApiEntity apiEntity);
  public abstract void deleteAllTestApis();
  public abstract void delete(OpenApiEntity apiEntity);
  public abstract void printAll();

  public abstract void commitBulkOperation();

  protected String getApiSpec(OpenApiEntity apiEntity) {
    return "{\n"
        + "  \"openapi\": \"3.0.0\",\n"
        + "  \"info\": {\n"
        + "    \"title\": \"" + apiEntity.name + "\",\n"
        + "    \"version\": \"1.0.0\",\n"
        + "    \"description\": \"This is a sample server endpoint. Used for testing purposes.\",\n"
        + "    \"contact\": {\n"
        + "      \"email\": \"dsiemens@mail.uni-mannheim.de\"\n"
        + "    },\n"
        + "    \"x-logo\": {\n"
        + "      \"url\": \"\"\n"
        + "    },\n"
        + "    \"license\": {\n"
        + "      \"name\": \"\",\n"
        + "      \"url\": \"https://iot.informatik.uni-mannheim.de\"\n"
        + "    }\n"
        + "  },\n"
        + "  \"servers\": [\n"
        + "    {\n"
        + "      \"url\": \"https://iot.informatik.uni-mannheim.de:8082\"\n"
        + "    }\n"
        + "  ],\n"
        + "  \"paths\": {\n"
        + "    \"/phillips_light\": {\n"
        + "      \"post\": {\n"
        + "        \"responses\": {\n"
        + "          \"200\": {\n"
        + "            \"content\": {\n"
        + "              \"application/json\": {\n"
        + "                \"schema\": {\n"
        + "                  \"$ref\": \"#/components/schemas/PhillipsLightBulbInfo\"\n"
        + "                }\n"
        + "              }\n"
        + "            },\n"
        + "            \"description\": \"A JSON object containing current date and settings of the lightbulb.\"\n"
        + "          }\n"
        + "        },\n"
        + "        \"summary\": \"Returns a LightBulbInfo object.\",\n"
        + "        \"description\": \"Returns current date and settings of the lightbulb.\",\n"
        + "        \"operationId\": \"postLightBulbInfo\"\n"
        + "      }\n"
        + "    }\n"
        + "  },\n"
        + "  \"components\": {\n"
        + "    \"schemas\": {\n"
        + "      \"PhillipsLightBulbInfo\": {\n"
        + "        \"type\": \"object\",\n"
        + "        \"properties\": {\n"
        + "          \"switch_philips\": {\n"
        + "            \"type\": \"boolean\",\n"
        + "            \"example\": true\n"
        + "          },\n"
        + "          \"color\": {\n"
        + "            \"type\": \"string\",\n"
        + "            \"example\": \"yellow\"\n"
        + "          },\n"
        + "          \"brightness\": {\n"
        + "            \"type\": \"integer\",\n"
        + "            \"example\": 50\n"
        + "          },\n"
        + "          \"color_temperature\": {\n"
        + "            \"type\": \"integer\",\n"
        + "            \"example\": 20\n"
        + "          },\n"
        + "          \"time\": {\n"
        + "            \"type\": \"object\",\n"
        + "            \"properties\": {\n"
        + "              \"day\": {\n"
        + "                \"type\": \"integer\",\n"
        + "                \"maximum\": 31,\n"
        + "                \"minimum\": 1,\n"
        + "                \"example\": 1\n"
        + "              },\n"
        + "              \"month\": {\n"
        + "                \"type\": \"integer\",\n"
        + "                \"maximum\": 12,\n"
        + "                \"minimum\": 1,\n"
        + "                \"example\": 1\n"
        + "              },\n"
        + "              \"year\": {\n"
        + "                \"type\": \"integer\",\n"
        + "                \"maximum\": 12,\n"
        + "                \"minimum\": 1,\n"
        + "                \"example\": 2020\n"
        + "              },\n"
        + "              \"hour\": {\n"
        + "                \"type\": \"integer\",\n"
        + "                \"maximum\": 23,\n"
        + "                \"minimum\": 0,\n"
        + "                \"example\": 0\n"
        + "              },\n"
        + "              \"minute\": {\n"
        + "                \"type\": \"integer\",\n"
        + "                \"maximum\": 59,\n"
        + "                \"minimum\": 0,\n"
        + "                \"example\": 0\n"
        + "              },\n"
        + "              \"second\": {\n"
        + "                \"type\": \"integer\",\n"
        + "                \"maximum\": 59,\n"
        + "                \"minimum\": 0,\n"
        + "                \"example\": 0\n"
        + "              }\n"
        + "            }\n"
        + "          }\n"
        + "        }\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "}".trim();
  }
}

