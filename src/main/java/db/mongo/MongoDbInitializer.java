package db.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoDriverInformation;

public class MongoDbInitializer {

	private static MongoClient client;
	private static MongoDatabase apiDB;
	private static MongoDatabase mappingDB;

	private static void initializeDb() {
		ServerAddress address = null;
		MongoCredential credentials = null;
		MongoClientOptions options = MongoClientOptions.builder().build();
		MongoDriverInformation driverInformation = MongoDriverInformation.builder().build();

		if (MongoConstants.MONGO_DB_USER != null && !MongoConstants.MONGO_DB_USER.isEmpty()
				&& MongoConstants.MONGO_DB_AUTHENTICATION_DATABASE != null
				&& !MongoConstants.MONGO_DB_AUTHENTICATION_DATABASE.isEmpty()
				&& MongoConstants.MONGO_DB_PASSWORD != null && !MongoConstants.MONGO_DB_PASSWORD.isEmpty()) {
			credentials = MongoCredential.createCredential(MongoConstants.MONGO_DB_USER,
					MongoConstants.MONGO_DB_AUTHENTICATION_DATABASE, MongoConstants.MONGO_DB_PASSWORD.toCharArray());
		}
		address = new ServerAddress(MongoConstants.MONGO_DB_HOST, MongoConstants.MONGO_DB_PORT);

		MongoDbInitializer.client = new MongoClient(address, credentials, options, driverInformation);
		MongoDbInitializer.apiDB = MongoDbInitializer.client.getDatabase(MongoConstants.MONGO_DB_API_DB_NAME);
		MongoDbInitializer.mappingDB = MongoDbInitializer.client.getDatabase(MongoConstants.MONGO_DB_MAPPING_DB_NAME);
	}

	public static MongoDatabase getApiDB() {
		if (MongoDbInitializer.apiDB == null) {
			MongoDbInitializer.initializeDb();
		}
		return MongoDbInitializer.apiDB;
	}

	public static MongoDatabase getMappingDB() {
		if (MongoDbInitializer.mappingDB == null) {
			MongoDbInitializer.initializeDb();
		}
		return MongoDbInitializer.mappingDB;
	}
}
