
package javafxapplication2;

import java.nio.file.Path;
import java.util.*;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MediaHandler extends Doer{
    
    private static  MediaHandler mediahandler;
    MediaPlayer player;
    Double normalrate;
    Double FACTOR;
    ArrayList<String> mediastoplay = new ArrayList();
    int COUNTER = 0;
    volatile Double newvalue;
    
    private MediaHandler(){}
    
    public static MediaHandler gethandler(){
        if(mediahandler == null)
            mediahandler = new MediaHandler(); 
        return mediahandler;
    }
    
    public void playvideo(String url){
        
    Media media = new Media(url);
    if(player != null)
            player.dispose();
    
    ConfigFiles.addfile(media.getSource());
    player = new MediaPlayer(media);
    notifyobservers("Video changed");
    player.setAutoPlay(true);
    newvolume(ConfigFiles.getvolume());
    normalrate = player.getRate();
        
    }
    
     private void playvideosfromlist(){
         
        playvideo(mediastoplay.get(COUNTER++));
        player.setOnEndOfMedia(new playnext()); 
        
    }
     
     public void playvideofromlist(Iterable<Path> stream){
         
         mediastoplay.clear();
        for(Path mediapath: stream){
                String media = mediapath.toUri().toString();
                mediastoplay.add(media);
           }  
        
       COUNTER = 0;
       playvideosfromlist();
       notifyobservers("Multiple");
     }
     
    public void multiplyspeed(Double multiplier){
     player.setRate(multiplier * this.normalrate);
   }

    public void previous() {
        
        if(COUNTER == 0)
            COUNTER = mediastoplay.size() - 1;
        
        playvideo(mediastoplay.get(COUNTER--));
    }

    public void next() {
        
        if(COUNTER == mediastoplay.size())
            COUNTER = 0;
        
        playvideo(mediastoplay.get(COUNTER++));
        
    }
    
    public void pause() {
        player.pause();
    }

    public void play() {
        player.play();
    }

    public void stop() {
        player.stop();
    }
    
    public void mute(){
        player.setMute(true);
    }
    
    public void unmute(){
        player.setMute(false);
    }
    
    private class playnext extends Task<Void>{

        @Override
        protected Void call() throws Exception {
            
            if(COUNTER < mediastoplay.size()){
                  notifyobservers("Video changed");
                  playvideosfromlist();
            }
            return null;
        }
      
    }
    
    public void insertvolume(Double newvolume){
        player.setVolume(player.getVolume() + newvolume/100);
    }
    public void newvolume(Double volume){
        player.setVolume(volume);
    }  
    public MediaPlayer getplayer(){
        return player;
    }
    
    public String geturl(){
        return player.getMedia().getSource();
    }
    public void jumptospecifictime(int seconds){
    Duration current = player.getCurrentTime();
    Duration jump = Duration.seconds(seconds);
    player.seek(current.add(jump));
   }
}
