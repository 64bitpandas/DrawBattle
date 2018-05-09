import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
    * Creates an explosion graphic when a minion dies.
    * @author Ben Cuan
    * @since 22 Apr 2017
*/
public class Explosion extends JPanel implements ActionListener {

    private final int TIME = 1; //seconds the image will show for
    private int rand; //Random from 0 to 2- determines the image to be drawn
    private Timer count;

    private static Image crash = new ImageIcon("images/crash.png").getImage(), 
                          boom = new ImageIcon("images/boom.png").getImage(), 
                           pow = new ImageIcon("images/pow.png").getImage(); //Three images that will be selected at random

    /** *All of the Explosions that are to be drawn. */
    public static ArrayList<Explosion> explosions = new ArrayList<Explosion> ();

    /** *Coordinates to draw the Explosion's image */
    public int x, y;

    /** *Image to be drawn */
    public Image currImage;

    public Explosion (int xValue, int yValue) {
        count = new Timer(1000, this);
        count.setInitialDelay(1000);
        count.start();

        x = xValue;
        y = yValue;
        rand = (int)(Math.random() * 3);

        currImage = (rand == 0) ? crash : (rand == 1) ? boom : pow;

        explosions.add(this);
    }

    public void actionPerformed (ActionEvent e) {
        count.stop();
        explosions.remove(this);
    }  
}