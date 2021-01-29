package openApi.repository;

import static global.Parameters.USER_ID;
import openApi.model.OpenApiEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import db.firebase.FirebaseDbInitializer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

public class FirebaseOpenApiRepository extends OpenApiRepository {

  private static final String OPEN_APIS = "openApis";
  Firestore db;

  protected FirebaseOpenApiRepository() {
    db = FirebaseDbInitializer.getDb();
  }

  public OpenApiEntity save(OpenApiEntity apiEntity) {

    Map<String, Object> docData = new HashMap<>();
    docData.put("createdBy", USER_ID);

    // Map<String, Object> metadata = new HashMap<>();
    docData.put("name", "Performance Test - " + apiEntity.name);
    docData.put("openApiSpec", this.getApiSpec(apiEntity));
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
}

