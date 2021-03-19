
package javafxapplication2;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class PlayFromURLController implements Initializable {

    
    @FXML
    TextField textfield;
    
    @FXML
    Button PlayButton;
   
    MediaHandler handler;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       handler = MediaHandler.gethandler();
    }
    
    @FXML
    private void textentered(KeyEvent event){
       PlayButton.setDisable(false);
    }
    
    @FXML
    private void Play(ActionEvent event) throws MalformedURLException, URISyntaxException{
        String urlentered = textfield.getText();
        URL url = new URL(urlentered);
        handler.playvideo(url.toURI().toString());
        Stage stage = (Stage)PlayButton.getScene().getWindow();
        stage.close();
        }
    
}
