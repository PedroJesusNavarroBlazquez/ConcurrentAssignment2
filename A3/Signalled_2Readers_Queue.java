package concurrent_assignment2.A3;

import concurrent_assignment2.A_intro.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Use the synchronized keyword and signals.
 *
 * You cannot decide whose's turn it is based on a 2 states variables, so know
 * use a variable which allows for more values (you need 3 states, that is, 3
 * turns).
 *
 * Output should be: writer prints, both readers read, and so on...
 *
 */
class Signalled_2Readers_Queue implements Queue {

    int n = 0;

    volatile int turn = 2;

    @Override
    public synchronized void read(int ID) {
        // TODO Auto-generated method stub
        while (turn != ID){
            try {
              wait();
          } catch (InterruptedException ex) {
              Logger.getLogger(Signalled_2Readers_Queue.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
        System.out.println("Reading n for ID --> " + ID + ": " + n);
        turn = turn + 1;

        notifyAll();
    }

    @Override
    public synchronized void write(int x) {
        // TODO Auto-generated method stub
        while (turn!=2){
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Signalled_2Readers_Queue.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        n = x;
        System.out.println("Wrinting n: " + n);
        turn=turn-2;
        
        notifyAll();
    }

    @Override
    public void read() {
        // no need to implement this
    }

}
