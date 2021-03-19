
package javafxapplication2;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PlayFromMultipleFilesController implements Initializable  {
    
        
    @FXML
    Button AddButton;
    
    @FXML
    Button RemoveButton;
    
    @FXML
    Button PlayButton;
    
    @FXML
    TextArea textarea;
    
    private int start;
    private int end;
    private String entiretext;
    private ArrayList<Path> selectedmediafiles;
    
    MediaHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = MediaHandler.gethandler();
        selectedmediafiles = new ArrayList();
    }
    
      @FXML
    private void Add(ActionEvent event) {
        
       FileChooser chooser = new FileChooser();
       File newfile = chooser.showOpenDialog(null);
       StringTokenizer tokens = new StringTokenizer(newfile.toString(), "\\");
       while(tokens.countTokens() > 1)
                      tokens.nextToken();
       
       textarea.appendText(tokens.nextToken() + "\n");
       URI uri = newfile.toURI();
       Path path = Paths.get(uri);
       selectedmediafiles.add(path);
       PlayButton.setDisable(false);
    }
    
    @FXML
    private void playfromlist(ActionEvent event) throws IOException{
       handler.playvideofromlist(selectedmediafiles);
       Stage stage = (Stage)AddButton.getScene().getWindow();
       stage.close();
    }
    
    @FXML
    private void Remove(ActionEvent event) {
        
      String highlighted = textarea.getSelectedText();
      String toreplace = textarea.getText(end, entiretext.length());
      StringTokenizer token = new StringTokenizer(toreplace, "\n");
      if(token.hasMoreTokens()){
      String afteremptyspace = token.nextToken();
      int whereactualtextbegins = toreplace.indexOf(afteremptyspace);
      toreplace = toreplace.substring(whereactualtextbegins);
      }
      textarea.deleteText(start, entiretext.length());
      textarea.insertText(start, toreplace);
      RemoveButton.setDisable(true);
      Path pathtoberemoved = null;
      for(Path path : selectedmediafiles)
          if(path.endsWith(highlighted)){
              pathtoberemoved = path;
              break;
          }
      
      selectedmediafiles.remove(pathtoberemoved);
      
      token = new StringTokenizer(textarea.getText(), "\n");
      if(token.countTokens() == 0)
          PlayButton.setDisable(true);
    }
    
    @FXML
    private void selecttext(MouseEvent t){
       
        int pos = textarea.getCaretPosition();
        String textuptoposition = textarea.getText(0, pos);
        StringTokenizer token = new StringTokenizer(textuptoposition, "\n");
        while(token.countTokens() > 1)
            token.nextToken();
        
        entiretext = textarea.getText();
        
        if(token.countTokens() == 1){
        String chosen = token.nextToken();
        
        token = new StringTokenizer(entiretext, "\n");
        
        String texttoselect = null;
        while(token.hasMoreTokens()){
            String nn = token.nextToken();
            if(nn.startsWith(chosen))
                texttoselect = nn;
        }
        
        if(texttoselect != null ){
        start = entiretext.indexOf(texttoselect);
        end = start + texttoselect.length();
        textarea.selectRange(start, end);
        }
        
        RemoveButton.setDisable(false);
    }
    }
    
}
