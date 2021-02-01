package openApiMappings.repository;

import static global.Parameters.USER_ID;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import db.firebase.FirebaseDbInitializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;
import openApiMappings.model.OpenApiMappingEntity;

public class FirebaseOpenApiMappingRepository extends OpenApiMappingRepository {
  private Firestore db;

  protected FirebaseOpenApiMappingRepository() {
    db = FirebaseDbInitializer.getDb();
  }

  public void commitBulkOperation(){
    // not supported
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

  private Iterable<DocumentReference> getAllDocs() {
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
}
