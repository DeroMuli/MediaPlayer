
package javafxapplication2;

import java.util.ArrayList;


public class Doer {
     
    private final ArrayList<Observers> observers;
    public Doer(){
        observers = new ArrayList();
    }
    
    public void add(Observers ob){
        observers.add(ob);
    }
    
    public void notifyobservers(String args){
        for(Observers ob : observers)
            ob.update(args);
    }
}
