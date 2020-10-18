package openApiMappings;

import static global.Parameters.USER_ID;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import firebase.DbInitializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;
import openApi.OpenApiEntity;

public class OpenApiMappingRepository {

  private static final String OPEN_API_MAPPING = "openApiMappings";
  private static final String PERFORMANCE_TEST = "PerformanceTest";
  Firestore db;

  public OpenApiMappingRepository() {
    db = DbInitializer.getDb();
  }

  public OpenApiMappingEntity save(OpenApiMappingEntity apiMappingEntity) {

    String targetIdMapping = getIdMapping(apiMappingEntity.target);
    String sourceIdMapping = getIdMapping(apiMappingEntity.source);
    String requestMapping = "{\"" + targetIdMapping + "\":\"" + sourceIdMapping + "\"}";
    String responseMapping = getResponseMapping(targetIdMapping, sourceIdMapping);

    Map<String, Object> docData = new HashMap<>();
    docData.put("createdBy", USER_ID);
    docData.put("requestMapping", requestMapping);
    docData.put("responseMapping", responseMapping);
    docData.put("sourceId", sourceIdMapping);
    ArrayList<Object> targetIds = new ArrayList<>();
    targetIds.add(targetIdMapping);
    docData.put("targetIds", targetIds);
    docData.put("type", 0);

    String id =
        PERFORMANCE_TEST + apiMappingEntity.source.getName() + "To" + apiMappingEntity.target
            .getName();
    apiMappingEntity.setId(id);

    ApiFuture<WriteResult> future = db.collection(OPEN_API_MAPPING).document(id).set(docData);
    try {
      WriteResult writeResult = future.get();
      System.out.println("Save Mapping: " + apiMappingEntity.id);
      System.out.println("Update time : " + writeResult.getUpdateTime());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      System.exit(1);
      return null;
    }
    return apiMappingEntity;
  }


  public void delete(OpenApiMappingEntity mappingEntity) {
    ApiFuture<WriteResult> writeResult = db.collection(OPEN_API_MAPPING).document(mappingEntity.id)
        .delete();
    try {
      System.out.println("Update time : " + writeResult.get().getUpdateTime());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void deleteAllTestMappings() {
    StreamSupport.stream(this.getAllDocs().spliterator(), false)
        .filter(documentReference ->
            documentReference.getId().startsWith(PERFORMANCE_TEST)
        ).forEach(documentReference -> {
          db.collection(OPEN_API_MAPPING).document(documentReference.getId()).delete();
          System.out.println("Deleted: " + documentReference.getId());
        }
    );
    System.out.println("Deleted all Test Mappings");
  }

  public Iterable<DocumentReference> getAllDocs() {
    Iterable<DocumentReference> openApis = db.collection(OPEN_API_MAPPING).listDocuments();
    return openApis;
  }

  public void printAllMappings() {
    StreamSupport.stream(this.getAllDocs().spliterator(), false)
        .forEach(documentReference -> {
          DocumentReference docRef = this.db.collection(OPEN_API_MAPPING)
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

  public void printMapping(String id) {
    StreamSupport.stream(this.getAllDocs().spliterator(), false)
        .filter(documentReference -> documentReference.getId().equals(id))
        .forEach(documentReference -> {
          DocumentReference docRef = this.db.collection(OPEN_API_MAPPING)
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

  private String getResponseMapping(String targetIdMapping, String sourceIdMapping) {
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

  private String getIdMapping(OpenApiEntity source) {
    return source.getId() + "_postLightBulbInfo_200";
  }

}
