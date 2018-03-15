package concurrent_assignment2.A1;

import concurrent_assignment2.A_intro.Queue;

/**
 * Use condition synchronization by means of busy wait.
 *
 * What kind of variable do you need to implement busy wait synchronization?
 *
 * Set a meaningful name for such variable.
 *
 */
class CS_Queue implements Queue {

    int n = 0;
    //we declare as volatile because there are more than a thread using it
    //avoids to make independent copies in Java (but still atomic)
    volatile boolean busySincroRead= false;
    

    @Override
    public void read() {
        
        //We will make it wait till the condition is true
        while(!busySincroRead);
        
        //CS    
        System.out.println("Reading 1: " + n);   
        // END OF CS
        
        //We "open" the barrier
        busySincroRead = false;
    }

    @Override
    public void write(int x) {   
        
        //We will make it wait till the condition is false (oposite from read)
        while(busySincroRead);
        
        //CS
        n = x;
        System.out.println("Wrinting 1: " + x);
       //END OF CS
        
        //We "open" the barrier
        busySincroRead = true;
    }

    @Override
    public void read(int ID) {
        // no need to implement this

    }

}
