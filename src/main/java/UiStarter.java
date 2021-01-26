import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import mongo.MongoDbInitializer;
import network.NetworkInitializer;
import openApi.OpenApiRepository;
import openApi.model.OpenApiEntity;
import openApi.mongo.MongoOpenApiRepository;
import openApiMappings.OpenApiMappingRepository;
import openApiMappings.model.OpenApiMappingEntity;
import openApiMappings.mongo.MongoOpenApiMappingRepository;
import ui.TestUIPanel;


public class UiStarter {
  public static void main (String args[]){
  //  UiStarter uiStarter = new UiStarter();
  //  NetworkInitializer initializer = new NetworkInitializer(10, 0, 0 ,false);
  //  uiStarter.startupFrame(initializer);

    OpenApiEntity random1 = new OpenApiEntity("Random API1");
    OpenApiEntity random2 = new OpenApiEntity("Random API2");

    OpenApiMappingEntity mapping1 = new OpenApiMappingEntity(random1, random2);

    OpenApiRepository apiRepo = new MongoOpenApiRepository();
    OpenApiMappingRepository mappingRepo = new MongoOpenApiMappingRepository();
    apiRepo.save(random1);
    apiRepo.save(random2);

    mappingRepo.save(mapping1);

    apiRepo.deleteAllTestApis();

  }


  public void startupFrame(NetworkInitializer initializer) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Performance Test");
      frame.add(new TestUIPanel(initializer));
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.setSize(400, 400);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          //initializer.deleteTestEntitys();
          e.getWindow().dispose();
        }
      });
    });
  }
}
