
package javafxapplication2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApplication2 extends Application {
    
    public static Stage mainstage;

    @Override
    public void start(Stage stage) throws Exception {
        
        mainstage = stage;
        ConfigFiles.readfilesonstart();
        ConfigFiles.savefilesonexit();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Muli Video Player");
        stage.setScene(scene);
        stage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
      
}
