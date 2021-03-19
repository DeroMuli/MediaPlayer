
package javafxapplication2;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public final class ConfigFiles {
    
    private static final File configfile  = new File("configure.properties");
    private static final File recentfiles = new File("recentfiles.properties");
    private static final Properties configproperty = new Properties();
    private static final Properties recentfilesproperty = new Properties();
    private static final HashMap<String, String> storage = new HashMap();
    private static final ArrayDeque<String> order = new ArrayDeque();
    
    private ConfigFiles(){}
    
    static public void readfilesonstart() throws FileNotFoundException, IOException{
        
        if(!recentfiles.exists()){
            PrintWriter config = new PrintWriter(new BufferedWriter(new FileWriter(recentfiles)));
            config.flush();
            config.close();
        }
            
        FileReader reader = new FileReader(recentfiles);
        recentfilesproperty.load(reader);
        if(!recentfilesproperty.isEmpty())
            for(int i = recentfilesproperty.size()-1 ; i >= 0  ; i--)
                    addfile(recentfilesproperty.getProperty(Integer.toString(i)));
        
        if(!configfile.exists()){
            PrintWriter config = new PrintWriter(new BufferedWriter(new FileWriter(configfile)));
            config.flush();
            config.close();
        }
        
        FileReader read = new FileReader(configfile);
        configproperty.load(read);
        
    }
    
    static public void savefilesonexit(){
        
            Stage mainstage = JavaFXApplication2.mainstage;
            mainstage.setOnCloseRequest(new EventHandler() {

            @Override
            public void handle(Event t) {
               
                for(int i = 0 ; i < 5 ; i++)
                     if(!order.isEmpty())
                         recentfilesproperty.setProperty(Integer.toString(i), storage.get(order.pop()));
                
                try {
                    PrintWriter config = new PrintWriter(new BufferedWriter(new FileWriter(recentfiles)));
                    recentfilesproperty.store(config, "RecentFiles");
                    config.flush();
                    config.close();
                } catch (IOException ex) {
                    Logger.getLogger(JavaFXApplication2.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    PrintWriter config = new PrintWriter(new BufferedWriter(new FileWriter(configfile)));
                    configproperty.store(config, "Configaration Parameters");
                    config.flush();
                    config.close();
                } catch (IOException ex) {
                    Logger.getLogger(JavaFXApplication2.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }
    
    
    public static void clearfiles(){
        recentfilesproperty.clear();
        order.clear();
        storage.clear();
        recentfiles.delete();
    }
    
    public static void addfile(String url){
        
        URI uri = URI.create(url);
        Path path = Paths.get(uri);
        String name = path.toString();
        StringTokenizer token = new StringTokenizer(name, "\\");
        while(token.countTokens() > 1)
            token.nextToken();
        
        name = token.nextToken();
        
        storage.put(name, url);
        order.addFirst(name);
    }
    
    public static ArrayList<String> recentfiles(){
        ArrayList<String> tobereturned = new ArrayList(order);
        return tobereturned;
    }
    
    public static String geturl(String pathname){
        return storage.get(pathname);
    }
    
    public static Double getvolume(){
        return Double.parseDouble(configproperty.getProperty("VOLUME", "0.5"));
    }
    
    public static void setvolume(Double newvolume){
        configproperty.setProperty("VOLUME", String.valueOf(newvolume));
    }
    
}
