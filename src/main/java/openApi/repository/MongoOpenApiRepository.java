package openApi.repository;

import java.util.ArrayList;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;

import static global.Parameters.USER_ID;

import org.bson.BsonDateTime;
import org.bson.Document;
import org.bson.types.ObjectId;

import global.Utils;
import db.mongo.MongoConstants;
import db.mongo.MongoDbInitializer;
import openApi.model.OpenApiEntity;

public class MongoOpenApiRepository extends OpenApiRepository {

    private MongoCollection<Document> apiCollection;
    private ArrayList<WriteModel<Document>> bulkWrite;

    protected MongoOpenApiRepository() {
        MongoDatabase apiDB = MongoDbInitializer.getApiDB();
        this.apiCollection = apiDB.getCollection(MongoConstants.MONGO_DB_API_COLLECTION_NAME);
        this.bulkWrite = new ArrayList<WriteModel<Document>>();
    }

    public void commitBulkOperation() {
        System.out.println("Commit api bulk update");
        this.apiCollection.bulkWrite(bulkWrite);
        System.out.println("Writing apis finished");
        this.bulkWrite.clear();
    }

    public OpenApiEntity save(OpenApiEntity apiEntity) {
        Document docData = new Document();
        docData.put("createdBy", USER_ID);
        docData.put("name", "Performance Test - " + apiEntity.name);
        docData.put("type", 0);
        docData.put("apiSpec", this.getApiSpec(apiEntity));
        docData.put("metadata", new Document());
        docData.put("performance_test", true);

        // Mongoose stuff
        docData.put("createdAt", new BsonDateTime(System.currentTimeMillis()));
        docData.put("updatedAt", new BsonDateTime(System.currentTimeMillis()));
        docData.put("__v", 0);

        if (apiEntity.id == null) {
            apiEntity.setId(new ObjectId().toHexString());
        }
        this.bulkWrite.add(new ReplaceOneModel<Document>(new Document("_id", new ObjectId(apiEntity.id)), docData,
                new UpdateOptions().upsert(true)));

        return apiEntity;
    }

    public void delete(OpenApiEntity apiEntity) {
        this.apiCollection.deleteOne(new Document("_id", new ObjectId(apiEntity.id)));
        System.out.println("Delete OpenApi: " + apiEntity.getId());
        System.out.println("Update time : " + Utils.getCurrentISOTimeString());
    }

    public void deleteAllTestApis() {
        DeleteResult res = this.apiCollection.deleteMany(new Document("performance_test", true));
        System.out.println("Deleted " + res.getDeletedCount() + " Test Apis");
    }

    public void printAll() {
        ArrayList<Document> docs = new ArrayList<Document>();
        this.apiCollection.find().into(docs);
        for (Document d : docs) {
            System.out.println(d.toString());
        }
    }
}
