package openApi;

import static global.Parameters.USER_ID;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import firebase.DbInitializer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

public class OpenApiRepository {

  private static final String OPEN_APIS = "openApis";
  private static final String PERFORMANCE_TEST = "PerformanceTest";
  Firestore db;

  public OpenApiRepository() {
    db = DbInitializer.getDb();
  }

  public OpenApiEntity save(OpenApiEntity apiEntity) {

    Map<String, Object> docData = new HashMap<>();
    docData.put("createdBy", USER_ID);

    Map<String, Object> metadata = new HashMap<>();
    docData.put("name", "Performance Test - " + apiEntity.name);
    docData.put("openApiSpec", getApiSpec(apiEntity));
    docData.put("metadata", "{}");

    String id = PERFORMANCE_TEST +apiEntity.name;
    apiEntity.setId(id);

    ApiFuture<WriteResult> future = db.collection(OPEN_APIS).document(id).set(docData);
    try {
      WriteResult result = future.get();
      System.out.println("Save OpenApi: " + apiEntity.getId());
      System.out.println("Update time : " + result.getUpdateTime());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      System.exit(1);
      return null;
    }
    return apiEntity;
  }

  public void delete(OpenApiEntity apiEntity) {
    ApiFuture<WriteResult> writeResult = db.collection(OPEN_APIS).document(apiEntity.id).delete();
    try {
      System.out.println("Update time : " + writeResult.get().getUpdateTime());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void deleteAllTestApis() {
    StreamSupport.stream(this.getAllDocs().spliterator(), false)
        .filter(documentReference ->
            documentReference.getId().startsWith(PERFORMANCE_TEST)
        ).forEach(documentReference -> {
          System.out.println("Deleted: "+documentReference.getId());
          db.collection(OPEN_APIS).document(documentReference.getId()).delete();
        }
    );
    System.out.println("Deleted all Test Apis");
  }


  public Iterable<DocumentReference> getAllDocs() {
    Iterable<DocumentReference> openApis = db.collection(OPEN_APIS).listDocuments();
    return openApis;
  }

  public void printAll() {
    StreamSupport.stream(this.getAllDocs().spliterator(), false)
        .forEach(documentReference -> {
          DocumentReference docRef = this.db.collection(OPEN_APIS)
              .document(documentReference.getId());
          ApiFuture<DocumentSnapshot> future = docRef.get();
          try {
            DocumentSnapshot document = future.get();
            System.out.println(document.getData());
          } catch (InterruptedException e) {
            e.printStackTrace();
          } catch (ExecutionException e) {
            e.printStackTrace();
          }
        });
  }

  private String getApiSpec(OpenApiEntity apiEntity) {
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

