import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;

public class main {

    public static void main(String[] args) {

        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();

        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase database = client.getDatabase("students");
        MongoCollection<Document> grades = database.getCollection("grades");

        ArrayList<Document> homeworks = grades
                .find(Filters.eq("type", "homework"))
                .sort(Sorts.ascending("student_id", "score"))
                .into(new ArrayList<Document>());


        Integer lastStudent = null;

        for (Document doc : homeworks) {
            Integer currentStudent = doc.getInteger("student_id");

            if(!currentStudent.equals(lastStudent))
                grades.deleteOne(doc);

            lastStudent = currentStudent;
        }

    }

}
