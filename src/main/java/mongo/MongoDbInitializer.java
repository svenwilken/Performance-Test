package mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;


public class MongoDbInitializer {

	private static MongoClient client;
	private static MongoDatabase apiDB;
	private static MongoDatabase mappingDB;

	private static void initializeDb() {
		String mongoConnectionString = String.format("mongodb://%s:%d/", MongoConstants.MONGO_DB_HOST, MongoConstants.MONGO_DB_PORT);
		
		MongoDbInitializer.client = new MongoClient(new MongoClientURI(mongoConnectionString));
		MongoDbInitializer.apiDB = MongoDbInitializer.client.getDatabase(MongoConstants.MONGO_DB_API_DB_NAME);
		MongoDbInitializer.mappingDB = MongoDbInitializer.client.getDatabase(MongoConstants.MONGO_DB_MAPPING_DB_NAME);
	}
	
	  public static MongoDatabase getApiDB() {
		    if(MongoDbInitializer.apiDB == null){
		    	MongoDbInitializer.initializeDb();
		    }
		    return MongoDbInitializer.apiDB;
	  }
	  
	  public static MongoDatabase getMappingDB() {
		    if(MongoDbInitializer.mappingDB == null){
		    	MongoDbInitializer.initializeDb();
		    }
		    return MongoDbInitializer.mappingDB;
	  }
}
