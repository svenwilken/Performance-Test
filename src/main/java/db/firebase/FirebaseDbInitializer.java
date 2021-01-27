package db.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseDbInitializer {
  private static Firestore db;
  private static void initializeDb(){
   try {
     //get current working dir
     String workingDir = System.getProperty("user.dir");
     FileInputStream serviceAccount = new FileInputStream(workingDir+System.getProperty("file.separator")+"firebase-admin-file.json");

     FirebaseOptions options = new FirebaseOptions.Builder()
         .setCredentials(GoogleCredentials.fromStream(serviceAccount))
         .setDatabaseUrl("https://integrateit-41c60.firebaseio.com")
         .build();

     FirebaseApp.initializeApp(options);
     Firestore db = FirestoreClient.getFirestore();
     FirebaseDbInitializer.db=db;
   } catch (IOException e) {
     e.printStackTrace();
     System.exit(1);
   }
  }

  public static Firestore getDb() {
    if(FirebaseDbInitializer.db==null){
      FirebaseDbInitializer.initializeDb();
    }
    return db;
  }

}
