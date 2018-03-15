package concurrent_assignment2.B1;

/**
 * Search for the exact critical section, and protect it with Dekker's
 * algorithm.
 *
 */
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.applet.*;

public class Garden extends Applet {

    Button goButton;
    Turnstile turnstile1;
    Turnstile turnstile2;
    Counter counter;
    NumberCanvas counterD;
    NumberCanvas turn1D;
    NumberCanvas turn2D;
//    Checkbox fixit;
    public final static int MAX = 20;

    public void init() {
        super.init();
        setBackground(Color.lightGray);
        // Set up Button 
        Panel p0 = new Panel();
        p0.add(goButton = new Button(" Simulate "));
        goButton.setFont(new Font("Helvetica", Font.BOLD, 24));

        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (turnstile1 == null && turnstile2 == null) {
                    go();
                } else if (!turnstile1.isAlive() && !turnstile2.isAlive()) {
                    go();
                }
            }
        });
        Panel p = new Panel();
        Label lb = new Label("Ornamental Garden");
        lb.setFont(new Font("Arial", Font.BOLD, 24));
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p.add("South", lb);

        Panel p1 = new Panel();
        p1.setLayout(new BorderLayout());

        p1.add("Center", p0);

        // Set up Display
        Panel p2 = new Panel();
        counterD = new NumberCanvas("Counter");
        turn1D = new NumberCanvas("Turnstile 1", Color.ORANGE);
        turn2D = new NumberCanvas("Turnstile 2", Color.ORANGE);
        counterD.setSize(150, 100);
        turn1D.setSize(100, 100);
        turn2D.setSize(100, 100);
        p2.add(turn1D);
        p2.add(counterD);
        p2.add(turn2D);
        // Arrange Applet display
        setLayout(new BorderLayout());
        add("North", p);
        add("Center", p2);
        add("South", p1);

    }

    private void go() {

        counter = new Counter(counterD);
        //We modify and add the ID for both turnstile
        turnstile1 = new Turnstile(turn1D, counter, 0);
        turnstile2 = new Turnstile(turn2D, counter, 1);
        turnstile1.start();
        turnstile2.start();
    }

}

class Counter {

    boolean[] dekkerCS={false,false};
    volatile int turn = 0;
    int value = 0;
    NumberCanvas display;

    Counter(NumberCanvas n) {
        
       
        display = n;
        display.setvalue(value);
        
    }

    void increment(int ID) {
        //pre-procotol
        dekkerCS[ID] = true;
        
        //Even if is not our turn we check it
        while (dekkerCS[1-ID]) {
            if (turn == (1-ID)) {
                dekkerCS[ID] = false;//not my turn so I wait
                while (turn == (1-ID));
                dekkerCS[ID] = true;
            }
        }
        
        //CS
        int temp = value;   //read[v]
        CC.ForceCC();
        value = temp + 1;       //write[v+1]
        display.setvalue(value);
        //END OF CS
        
        //post-protocol
        turn = 1-ID;
        dekkerCS[ID] = false;
    }
}

class Turnstile extends Thread {

    NumberCanvas display;
    Counter people;
    int ID;
    //We need to modify the constructor in order to implement an ID to the turnstiles
    Turnstile(NumberCanvas n, Counter c, int ID1) {
        display = n;
        people = c;
        ID = ID1;
    }

    public void run() {
        try {
            display.setvalue(0);
            for (int i = 1; i <= Garden.MAX; i++) {
                Thread.sleep(500); //0.5 second
                display.setvalue(i);
                //Now we can call the method increment with the ID parameter
                people.increment(ID);
            }
        } catch (InterruptedException e) {
        }
    }
}

class CC {

    public static void ForceCC() {
        if (Math.random() < 0.5) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        };

    }
}
