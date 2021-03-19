
package javafxapplication2;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

public class FXMLDocumentController implements Initializable , Observers{
    
    @FXML
    MediaView View;
    
    @FXML
    MenuItem FullScreenMenuItem;
    
    @FXML
    MenuItem OpenFileMenuItem;
    
    @FXML
    MenuItem MultipleFileMenuItem;
    
    @FXML
    MenuItem OpenFolderMenuItem;
    
    @FXML
    MenuItem OpenDiscMenuItem; 
    
    @FXML
    MenuItem OpenNetworkStreamMenuItem;
    
    @FXML
    MenuItem OpenCaptureDeviceMenuItem;
    
    @FXML
    MenuItem QuitMenuItem;
    
    @FXML
    MenuItem JumpForwardMenuItem;
    
    @FXML
    MenuItem JumpBackwardsMenuItem;
    
    @FXML
    MenuItem JumpToSpecificTimeMenuItem;
    
    @FXML
    MenuItem TakeSnapshotMenuItem;
    
    @FXML
    MenuItem StopMenuItem;
    
    @FXML
    MenuItem PreviousMenuItem;
    
    @FXML
    MenuItem NextMenuItem;
    
    @FXML
    MenuItem RecordMenuItem;
        
    @FXML
    Button  PlayButton;
    
    @FXML
    VBox Box;
    
    @FXML
    Button  MuteButton;
    
    @FXML
    MenuItem MuteMenuItme;
    
    @FXML
    MenuItem PauseMenuItem;
    
    @FXML 
    MenuItem FastMenuItem;
    
    @FXML 
    MenuItem NormalMenuItem;
    
    @FXML 
    MenuItem SlowMenuItem;
    
    @FXML
    MenuItem ReduceVolumeMenuItem;
    
    @FXML
    MenuItem IncreaseVolumeMenuItem;
    
    @FXML
    Slider VolumeSlider;
    
    @FXML
    ProgressBar VideoSlider;
    
    @FXML
    Menu RecentFilesMenu;
    
    @FXML
     MenuItem ClearMenuItem;
    
    @FXML
    SeparatorMenuItem Separator;
    
    @FXML
    AnchorPane mediapane;
    
    volatile Double newvalue;
    
    Double FACTOR;
    
    MediaHandler handler;
    
    Stage stage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       handler = MediaHandler.gethandler();
       handler.add(this);
       refreshrecentfiles();
       VolumeSlider.setValue(ConfigFiles.getvolume()*100);
       View.fitHeightProperty().bind(mediapane.heightProperty());
       View.fitWidthProperty().bind(mediapane.widthProperty());
       View.setPreserveRatio(true);
       
       JavaFXApplication2.mainstage.widthProperty().addListener(new ChangeListener() {

           @Override
           public void changed(ObservableValue ov, Object t, Object t1) {
               Double screenwidth = (Double)ov.getValue();
               Double panewidth = mediapane.getWidth();
               Double center = (screenwidth - panewidth) / 6 ;
               mediapane.setTranslateX(center);
           }
       });
    }
    
    @FXML
    private void ClearButton(ActionEvent event){
        ObservableList<MenuItem> items = RecentFilesMenu.getItems();
        items.clear();
        ConfigFiles.clearfiles();
        items.add(Separator);
        items.add(ClearMenuItem);
        ClearMenuItem.setDisable(true);
    }
    
    public void refreshrecentfiles(){
        
        ObservableList<MenuItem> items = RecentFilesMenu.getItems();
        items.clear();
        
        ArrayList<String> order = ConfigFiles.recentfiles();
        for(int i = order.size()- 1 ; i >= 0 ; i-- ){
            enterrecentfile(order.get(i));
        }
        
        items.add(Separator);
        items.add(ClearMenuItem);
    }
    
    public void enterrecentfile(String path){
        
    ClearMenuItem.setDisable(false);
    ObservableList<MenuItem> items = RecentFilesMenu.getItems();
    MenuItem newmenuitem = new MenuItem();
    newmenuitem.setText(path);
    newmenuitem.setOnAction(new EventHandler() {

    @Override
    public void handle(Event t) {
        MenuItem item = (MenuItem)t.getSource();
        String path = item.getText();
        path = ConfigFiles.geturl(path);
        handler.playvideo(path);
                }
            });
            
    items.add(0, newmenuitem);
    if(items.size() > 5)
        items.remove(5);
    
    }
    
    @FXML
    private void MediaOperations(ActionEvent event) {
      
        if(event.getSource().equals(QuitMenuItem))
            javafx.application.Platform.exit();
        else if (event.getSource().equals(OpenFileMenuItem))
            OpenFile();
        else if(event.getSource().equals(MultipleFileMenuItem))
            OpenMultipleFile();
        else if(event.getSource().equals(OpenFolderMenuItem))
            OpenFolder();
        else if(event.getSource().equals(OpenNetworkStreamMenuItem))
            OpenNetworkStream();
            
    }
    
    @FXML
    private void PlayBackOperations(ActionEvent event) {
        if(event.getSource().equals(PauseMenuItem)){
            switch (PauseMenuItem.getText()) {
                case "Pause":
                    Play(event);
                    PauseMenuItem.setText("Play");
                    break;
                case "Play":
                    Play(event);
                    PauseMenuItem.setText("Pause");
                    break;
            }
        }
        else if(event.getSource().equals(JumpBackwardsMenuItem)){
            handler.jumptospecifictime(-10);
        }
        else if(event.getSource().equals(JumpForwardMenuItem)){
            handler.jumptospecifictime(10);
        }
        else if(event.getSource().equals(FastMenuItem)){
            handler.multiplyspeed(1.5);
            NormalMenuItem.setDisable(false);
        }
        else if(event.getSource().equals(SlowMenuItem)){
            handler.multiplyspeed(0.5);
        }
        else if(event.getSource().equals(NormalMenuItem)){
            handler.multiplyspeed(1.0);
            NormalMenuItem.setDisable(true);
        }
        else if(event.getSource().equals(StopMenuItem)){
            handler.stop();
            PlayButton.setText("Play");
        }
        else if(event.getSource().equals(NextMenuItem)){
            handler.next();
        }
        
        else if(event.getSource().equals(PreviousMenuItem)){
            handler.previous();
        }
        
    }
    
    @FXML
    private void AudioHandler(ActionEvent event) {
        if(event.getSource().equals(MuteMenuItme)) {
            switch (MuteMenuItme.getText()) {
                case "Mute":
                    Mute(event);
                    MuteMenuItme.setText("Unmute");
                    break;
                case "Unmute":
                    Mute(event);
                    MuteMenuItme.setText("Mute");
                    break;
            }
                
        }
        else if(event.getSource().equals(ReduceVolumeMenuItem)){
            handler.insertvolume(-10.0);
        }
        
        else if(event.getSource().equals(IncreaseVolumeMenuItem)){
            handler.insertvolume(10.0);
        }
    }
    
    @FXML
    private void VideoHandler(ActionEvent event) throws AWTException, IOException {
         if(event.getSource().equals(FullScreenMenuItem))
             JavaFXApplication2.mainstage.setFullScreen(true);
         else if(event.getSource().equals(TakeSnapshotMenuItem)){
             
             int x = (int)JavaFXApplication2.mainstage.getX();
             int y = (int)JavaFXApplication2.mainstage.getY();
             int height = (int) JavaFXApplication2.mainstage.getHeight();
             int width = (int) JavaFXApplication2.mainstage.getWidth();
             Robot robot = new Robot();
             Rectangle rec = new Rectangle(x,y,width,height);
             BufferedImage bufferedimage = robot.createScreenCapture(rec);
             ImageIO.write(bufferedimage, "jpg", new File("C:\\Users\\admin\\Documents\\screenshot.jpg"));
             
         }
    }

    
    @FXML
    private void SubtitleHandler(ActionEvent event) {
        
    } 
    
    @FXML
    private void ChangeTheme(ActionEvent event) {
        
    }
    
    @FXML
    private void AboutUs(ActionEvent event) {
        aboutus();
    }
    
    @FXML
    private void Play(ActionEvent event) {
        switch (PlayButton.getText()) {
            case "Play":
                handler.play();
                PlayButton.setText("Pause");
                break;
            case "Pause":
                handler.pause();
                PlayButton.setText("Play");
                break;
        }
    }
    
    @FXML
    private void Mute(ActionEvent event) {
        
        switch (MuteButton.getText()) {
            case "Mute":
                handler.mute();
                MuteButton.setText("Unmute");
                break;
            case "Unmute":
                handler.unmute();
                MuteButton.setText("Mute");
                break;
        }
     
    }
    
    public void setnplay(MediaPlayer player){
    View.setMediaPlayer(player);
    View.setVisible(true);
    PlayButton.disableProperty().set(false);
    PlayButton.setText("Pause");
    MuteButton.disableProperty().set(false);
    MuteMenuItme.setDisable(false);
    PauseMenuItem.setDisable(false);
    JumpBackwardsMenuItem.setDisable(false);
    JumpForwardMenuItem.setDisable(false);
    JumpToSpecificTimeMenuItem.setDisable(false);
    RecordMenuItem.setDisable(false);
    StopMenuItem.setDisable(false);
    FastMenuItem.setDisable(false);
    SlowMenuItem.setDisable(false);
    VolumeSlider.setValue(50.0);
    setupvideoslider();
    setupvolumeslider();
    }
    

    
    public void setupvideoslider(){
        
    VideoSlider.setOnMouseDragged( new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent t) {
            Double XTOVALUE = VideoSlider.getWidth()/100;
            Double newvalue = t.getX()/XTOVALUE;
            seekfromslider(newvalue);  
        }
    } );
    
    VideoSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            
            Double XTOVALUE = VideoSlider.getWidth()/100;
            Double newvalue = t.getX()/XTOVALUE;
            seekfromslider(newvalue);
            
        }
    });
            
    }

    public void setvideoslidervalue(Double currentvalue){
        VideoSlider.setProgress(currentvalue);
    }
    
    public void updatenextnprevious(){
        NextMenuItem.setDisable(false);
        PreviousMenuItem.setDisable(false);
    }
      
    public void setupvolumeslider(){
    VolumeSlider.setOnMouseDragged( new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent t) {
          Double volume = VolumeSlider.getValue()/100;
          ConfigFiles.setvolume(newvalue);
          handler.newvolume(volume);
        }
    } );
    
    VolumeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
         Double volume = VolumeSlider.getValue()/100;
         ConfigFiles.setvolume(newvalue);
         handler.newvolume(volume);
        }
    });
    }
    
    public void OpenFile() {
    FileChooser chooser = new FileChooser();
    File mediafile = chooser.showOpenDialog(null);
    handler.playvideo(mediafile.toURI().toString());
    }
    
    private void setnplay(String url){
    setuptitle(url);
    refreshrecentfiles();
    setnplay(handler.player);
    Thread progressbar = new Thread(new updateslider());
    progressbar.start();
    }
    
    private void setuptitle(String urlstring){
        
        URI uri = URI.create(urlstring);
        Path path = Paths.get(uri);
        String name = path.toString();
        StringTokenizer token = new StringTokenizer(name, "\\");
        while(token.countTokens() > 1)
            token.nextToken();
        
        name = token.nextToken();
        
        JavaFXApplication2.mainstage.setTitle("Muli Video Player" + "  " + " - " + name);
        
    }

    public void OpenMultipleFile() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("PlayMultipleFiles.fxml"));
            stage = new Stage();
            stage.initOwner(JavaFXApplication2.mainstage);
            stage.setTitle("Open Multiple Files");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
          ex.printStackTrace(System.out);
        }
    }

    public void OpenFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        File folder = chooser.showDialog(null);
        try {
            DirectoryStream stream = Files.newDirectoryStream(Paths.get(folder.toURI()), "*.{aif,aiff,fxm,flv,m3u8,mp3,mp4,wav,m4a,m4v}");
            handler.playvideofromlist(stream);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void OpenDisk() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void OpenNetworkStream() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("PlayFromURL.fxml"));
            stage = new Stage();
            stage.initOwner(JavaFXApplication2.mainstage);
            stage.setTitle("Open From URL");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (MalformedURLException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void OpenCaptureDevice() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void record() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(String args) {
        switch (args) {
            case "Video changed":
                setnplay(handler.geturl());
                break;
            case "Multiple":
                updatenextnprevious();
                break;
        }
    }
    

    public class updateslider extends Task<Void>{

        @Override
        protected Void call() throws Exception {
            

            handler.getplayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {

                @Override
                public void changed(ObservableValue<? extends Duration> ov, Duration t, Duration t1) {
                
                Duration total = handler.player.getTotalDuration();
                FACTOR = total.toSeconds()/100 ;
                 
                Double currenttimee = ov.getValue().toSeconds();
                newvalue = (currenttimee/FACTOR) / 100;
                setvideoslidervalue(newvalue);
                
                }
            });
 
            return null;
        }
      
    }
    
    public void seekfromslider(Double seek){
        
        Double timeinseconds = seek * FACTOR;
        Duration newduration = Duration.seconds(timeinseconds);
        handler.getplayer().seek(newduration);
       
    }
    
    public void aboutus(){
    Parent root;
        try {
           root = FXMLLoader.load(getClass().getResource("AboutUs.fxml"));
           stage = new Stage();
           stage.initOwner(JavaFXApplication2.mainstage);
           stage.setTitle("About Us");
           Scene scene = new Scene(root);
           stage.setScene(scene);
           stage.setResizable(false);
           stage.show();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }

    }
    
}
