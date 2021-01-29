package openApiMappings.repository;

import global.Parameters;
import openApi.model.OpenApiEntity;
import openApiMappings.model.OpenApiMappingEntity;

public abstract class OpenApiMappingRepository {
  private static OpenApiMappingRepository instance;

  public static OpenApiMappingRepository getInstance() {
    if (OpenApiMappingRepository.instance == null) {
      switch (Parameters.DATABASE) {
        case MONGO_DB:
          OpenApiMappingRepository.instance = new MongoOpenApiMappingRepository();
          break;
        case FIREBASE:
          OpenApiMappingRepository.instance = new FirebaseOpenApiMappingRepository();
          break;
        default:
          System.out.println("No Database defined");
      }
    }
    return OpenApiMappingRepository.instance;
  }

  protected static final String OPEN_API_MAPPING = "openApiMappings";
  protected static final String PERFORMANCE_TEST = "PerformanceTest";

  public abstract OpenApiMappingEntity save(OpenApiMappingEntity apiMappingEntity);

  public abstract void delete(OpenApiMappingEntity mappingEntity);

  public abstract void deleteAllTestMappings();

  public abstract void printAllMappings();

  public abstract void printMapping(String id);

  protected static String getResponseMapping(String targetIdMapping, String sourceIdMapping) {
    return "{\"" + sourceIdMapping + "\":{\"switch_philips\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"switch_philips\\\"\",\"color\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"color\\\"\",\"brightness\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"brightness\\\"\",\"color_temperature\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"color_temperature\\\"\",\"time\":{\"day\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"time\\\".\\\"day\\\"\",\"month\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"time\\\".\\\"month\\\"\",\"year\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"time\\\".\\\"year\\\"\",\"hour\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"time\\\".\\\"hour\\\"\",\"minute\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"time\\\".\\\"minute\\\"\",\"second\":\"$.\\\"" + targetIdMapping
        + "\\\".\\\"time\\\".\\\"second\\\"\"}}}";
  }

  protected static String getIdMapping(OpenApiEntity source) {
    return source.getId() + "_postLightBulbInfo_200";
  }

}
