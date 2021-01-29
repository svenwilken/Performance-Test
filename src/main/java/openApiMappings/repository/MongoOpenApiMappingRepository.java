package openApiMappings.repository;

import static global.Parameters.USER_ID;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;

import org.bson.BsonDateTime;
import org.bson.Document;
import org.bson.types.ObjectId;

import global.Utils;
import db.mongo.MongoConstants;
import db.mongo.MongoDbInitializer;
import java.util.ArrayList;
import openApiMappings.model.OpenApiMappingEntity;

public class MongoOpenApiMappingRepository extends OpenApiMappingRepository {
  private MongoCollection<Document> mappingCollection;

  protected MongoOpenApiMappingRepository() {
    MongoDatabase mappingDb = MongoDbInitializer.getMappingDB();
    this.mappingCollection = mappingDb.getCollection(MongoConstants.MONGO_DB_API_MAPPING_COLLECTION_NAME);
  }

  public OpenApiMappingEntity save(OpenApiMappingEntity apiMappingEntity) {

    String targetIdMapping = OpenApiMappingRepository.getIdMapping(apiMappingEntity.target);
    String sourceIdMapping = OpenApiMappingRepository.getIdMapping(apiMappingEntity.source);
    String requestMapping = "{\"" + targetIdMapping + "\":\"" + sourceIdMapping + "\"}";
    String responseMapping = OpenApiMappingRepository.getResponseMapping(targetIdMapping, sourceIdMapping);

    Document docData = new Document();
    docData.put("createdBy", USER_ID);
    docData.put("requestMapping", requestMapping);
    docData.put("responseMapping", responseMapping);
    docData.put("sourceId", sourceIdMapping);
    docData.put("__t", "OpenApiMapping");

    ArrayList<Object> targetIds = new ArrayList<>();
    targetIds.add(targetIdMapping);

    docData.put("targetIds", targetIds);
    docData.put("type", 0);
    docData.put("apiType", 0);
    docData.put("checksum", Utils.getRandomHexString(40));
    // docData.put("performance_test", true);

    // Mongoose stuff
    docData.put("createdAt", new BsonDateTime(System.currentTimeMillis()));
    docData.put("updatedAt", new BsonDateTime(System.currentTimeMillis()));
    docData.put("__v", 0);

    if (apiMappingEntity.id == null) {
      apiMappingEntity.setId(new ObjectId().toHexString());
    }
    this.mappingCollection.replaceOne(new Document("_id", new ObjectId(apiMappingEntity.id)), docData,
        new UpdateOptions().upsert(true));
    System.out.println("Save Mapping: " + apiMappingEntity.id);
    System.out.println("Update time : " + Utils.getCurrentISOTimeString());
    return apiMappingEntity;
  }

  public void delete(OpenApiMappingEntity mappingEntity) {
    this.mappingCollection.deleteOne(new Document("_id", new ObjectId(mappingEntity.id)));
    System.out.println("Delete Mapping: " + mappingEntity.id);
    System.out.println("Update time : " + Utils.getCurrentISOTimeString());
  }

  public void deleteAllTestMappings() {
    DeleteResult res = this.mappingCollection.deleteMany(new Document("performance_test", true));
    System.out.println("Deleted " + res.getDeletedCount() + " Test Mappings");
  }

  public void printAllMappings() {
    ArrayList<Document> docs = new ArrayList<Document>();
    this.mappingCollection.find().into(docs);
    for (Document d : docs) {
      System.out.println(d.toString());
    }
  }

  public void printMapping(String id) {
    ArrayList<Document> res = new ArrayList<Document>();
    this.mappingCollection.find(new Document("_id", new ObjectId(id))).into(res);
    if (res.size() == 1) {
      System.out.println(res.get(0));
    } else {
      System.out.println("Could not find mapping: " + id);
    }
  }
}
