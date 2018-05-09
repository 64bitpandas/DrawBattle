import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
    * Creates a colored circle with a number that counts down to zero.
    * Automatically resizes to fit center of JPanel.
    * Default operation at zero is Loader.load().
    * @author Ben Cuan
    * @since 22 Apr 2017
*/
public class Counter extends JPanel implements ActionListener {

    public Timer stepTimer; 
    private float beginSecs = 0;
    private Container.GameState next;
    private int radi;
    private int counterType = 0; //0 = no end state, 1 = switches to Loader, 2 = direct setState call
    private String loadText = "";

    /** *Number of seconds until Counter reaches zero. */
    public float secsLeft = 0;

    public Counter () {} //null instantiation constructor

    /**
    * Initializes a Counter without specifying an end state.
    * @param seconds Number of seconds before reaching zero.
    * @param radius The radius of the circle to draw. If it is greater than panel size then this will be ignored.
    */
    public Counter (int seconds, int radius) {
        stepTimer = new Timer(1000, this);
        stepTimer.setInitialDelay(0);
        stepTimer.addActionListener(this);
        beginSecs = secsLeft = seconds;
        startTimer();
        radi = radius;
        repaint();
    }

    /**
    * Initializes the Counter specifically for the Loader class.
    * @param seconds Number of seconds before reaching zero.
    * @param radius The radius of the circle to draw. If it is greater than panel size then this will be ignored.
    * @param nextState State to transition to after timer reaches zero.
    */
    public Counter (int seconds, int radius, Container.GameState nextState) {
       
       this(seconds, radius);
        next = nextState;
        counterType = 2;
    }

    /**
    * Initializes the Counter like any other JPanel.
    * @param seconds Number of seconds before reaching zero.
    * @param radius The radius of the circle to draw. If it is greater than panel size then this will be ignored.
    * @param nextState State to transition to after timer reaches zero.
    * @param showInLoader The string to display on the loading screen.
    */
    public Counter (int seconds, int radius, Container.GameState nextState, String showInLoader) {
        this(seconds, radius, nextState);
        counterType = 1;
        loadText = showInLoader;
    }

    public void startTimer() {
        if(stepTimer.isRunning()) {
            System.out.println("[INFO] StepTimer already running!");
            stepTimer.stop();
        } 

        stepTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Counter
        g.setColor(new Color(1-secsLeft/beginSecs,152/255.0f,219/255.0f));

        //if given radius is invalid
        if(radi*2 > getWidth() || radi * 2 > getHeight()) {
            g.fillOval(0,0,getWidth(),getHeight());
            g.setColor(Color.WHITE);
            g.setFont(FontLoader.load("BebasNeue Regular", getHeight()));
            g.drawString("" + (int)secsLeft, getWidth()/2 - getHeight()/2, getHeight());
        }
        else {
            g.fillOval(getWidth()/2 - radi, getHeight()/2 - radi, radi*2, radi*2 );
            g.setColor(Color.WHITE);
            g.setFont(FontLoader.load("BebasNeue Regular", radi));
            g.drawString("" + (int)secsLeft, getWidth()/2 - radi/4, getHeight()/2 + radi/4);
        }
    }

    //Manages the timer state. Decrements secsLeft every time the timer resets.
    public void actionPerformed(ActionEvent e) {

        if(secsLeft <= .5) {
            stepTimer.stop();
            
            if(counterType == 2) 
                MainWindow.frame.setState(next);
            else if(counterType == 1)
                Loader.load(3,next, loadText);
        } else {
            secsLeft -= .5;
            repaint();
        }
    }
}