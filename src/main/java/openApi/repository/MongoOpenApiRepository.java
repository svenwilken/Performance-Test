package openApi.repository;

import java.util.ArrayList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import static global.Parameters.USER_ID;
import org.bson.Document;
import global.Utils;
import db.mongo.MongoConstants;
import db.mongo.MongoDbInitializer;
import openApi.model.OpenApiEntity;

public class MongoOpenApiRepository extends OpenApiRepository{

    private MongoCollection<Document> apiCollection;

    protected MongoOpenApiRepository() {
        MongoDatabase apiDB = MongoDbInitializer.getApiDB();
        this.apiCollection = apiDB.getCollection(MongoConstants.MONGO_DB_API_COLLECTION_NAME);
    }

    public OpenApiEntity save(OpenApiEntity apiEntity) {
        
        Document docData = new Document();
        docData.put("createdBy", USER_ID);
        docData.put("name", "Performance Test - " + apiEntity.name);
        docData.put("apiSpec", this.getApiSpec(apiEntity));
        docData.put("metadata", new Document());

        String id = PERFORMANCE_TEST + apiEntity.name;
        apiEntity.setId(id);
        docData.put("_id", id);

        this.apiCollection.insertOne(docData);
        System.out.println("Save OpenApi: " + apiEntity.getId());
        System.out.println("Update time : " + Utils.getCurrentISOTimeString());
        return apiEntity;
    }

    public void delete(OpenApiEntity apiEntity) {
        this.apiCollection.deleteOne(new Document("_id", apiEntity.id));
        System.out.println("Delete OpenApi: " + apiEntity.getId());
        System.out.println("Update time : " + Utils.getCurrentISOTimeString());
    }

    public void deleteAllTestApis() {
        Document filter = new Document();
        Document regexIdFilter = new Document("$regex", String.format("^%s.*", PERFORMANCE_TEST));
        filter.put("_id", regexIdFilter);
        System.out.println(filter.toJson());
        DeleteResult res = this.apiCollection.deleteMany(filter);
        System.out.println("Deleted "+ res.getDeletedCount() + " Test Apis");
    }

    public void printAll() {
        ArrayList<Document> docs = new ArrayList<Document>();
        this.apiCollection.find().into(docs);
        for(Document d : docs) {
            System.out.println(d.toString());
        }
    }
}
