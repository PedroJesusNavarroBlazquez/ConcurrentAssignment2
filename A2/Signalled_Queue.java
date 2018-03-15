package concurrent_assignment2.A2;

import concurrent_assignment2.A_intro.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Use the synchronized keyword and signals so that you do not need to busy
 * wait. But of course you still need your variable to know whose's turn it is.
 *
 */
class Signalled_Queue implements Queue {

    int n = 0;
    //we declare as volatile because there are more than a thread using it
    //avoids to make independent copies in Java (but still atomic)
    boolean signalSincroRead = false;

    //In order to run the code in a concurrent way, we must use "synchronized"
    @Override
    public synchronized void read() {
      
        //passive wait 
        if(!signalSincroRead){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Signalled_Queue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //CS
        System.out.println("Reading n: "+ n);
        signalSincroRead = false;
        //END CS
        
        //This implies that all the threads waiting will wake up. 
        //Because there are only two threads (Write and Read), there is no need
        //to use notifyAll
        notify();
    }

    //In order to run the code in a concurrent way, we must use "synchronized"
    @Override
    public synchronized void write(int x) {
        
        //passive wait
        if(signalSincroRead){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Signalled_Queue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //CS
        n = x;
        System.out.println("Wrinting n: " + n);
        signalSincroRead = true;
        //END OF CS
        
        //This implies that all the threads waiting will wake up
        //Because there are only two threads (Write and Read), there is no need
        //to use notifyAll
        notify();
    }

    @Override
    public void read(int ID) {
        // no need to implement this

    }

}
