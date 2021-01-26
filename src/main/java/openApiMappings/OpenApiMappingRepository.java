package openApiMappings;

import openApi.model.OpenApiEntity;
import openApiMappings.model.OpenApiMappingEntity;

public abstract class OpenApiMappingRepository {

  protected static final String OPEN_API_MAPPING = "openApiMappings";
  protected static final String PERFORMANCE_TEST = "PerformanceTest";

  public abstract OpenApiMappingEntity save(OpenApiMappingEntity apiMappingEntity);
  public abstract void delete(OpenApiMappingEntity mappingEntity);
  public abstract void deleteAllTestMappings();
  public abstract void printAllMappings();
  public abstract void printMapping(String id);

  protected static String getResponseMapping(String targetIdMapping, String sourceIdMapping) {
    return "{\n"
        + "  \"" + sourceIdMapping + "\":{\n"
        + "   \"switch_philips\":\"" + targetIdMapping + ".switch_philips\",\n"
        + "   \"color\":\"" + targetIdMapping + ".color\",\n"
        + "   \"brightness\":\"" + targetIdMapping + ".brightness\",\n"
        + "   \"color_temperature\":\"" + targetIdMapping + ".color_temperature\",\n"
        + "   \"time\":{\n"
        + "     \"day\":\"" + targetIdMapping + ".time.day\",\n"
        + "     \"month\":\"" + targetIdMapping + ".time.month\",\n"
        + "     \"year\":\"" + targetIdMapping + ".time.year\",\n"
        + "     \"hour\":\"" + targetIdMapping + ".time.hour\",\n"
        + "     \"minute\":\"" + targetIdMapping + ".time.minute\",\n"
        + "     \"second\":\"" + targetIdMapping + ".time.second\"\n"
        + "   }\n"
        + "  }\n"
        + "}".trim();
  }

  protected static String getIdMapping(OpenApiEntity source) {
    return source.getId() + "_postLightBulbInfo_200";
  }

}
