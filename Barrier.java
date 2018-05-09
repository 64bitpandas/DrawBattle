import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
    * The mass of blocks on each side of the map that prevents minions from entering. 
    * @author Ben Cuan
    * @since 5 May 2017
*/
public class Barrier {

    public int playerNo; //Either 1 or 2. The player that this barrier belongs to.
    public ArrayList<BarrierBlock> blocks; //Current set of blocks


    /**
     * Instantiates the Barrier for player specified.
     * @param player The player that this barrier belongs to. Either 1 or 2.
     */
    public Barrier (int player) {
        playerNo = player;
        blocks = new ArrayList<BarrierBlock>();
        for(int x = (playerNo == 1) ? 0 : 800; x < ((playerNo == 1) ? 200 : 1000); x+= BarrierBlock.SIZE) {
            for(int y = 0; y < 500; y+= BarrierBlock.SIZE) {
                blocks.add(new BarrierBlock(x, y, playerNo));
            }
        }
    }
}

/**
 * A single block within the larger Barrier. 
 * @author Ben Cuan
 * @since 5 May 2017
 */
class BarrierBlock {

    public float healthPercent; //Between 0 and 1. The fraction of health remaining in the block
    public int x, y; //xy coordinates of top left corner
    public int playerNo; //player that this barrierBlock belongs to
    public int currHealth;
    public int maxHealth;

    public boolean dead; //Health <= 0

    public static final int SIZE = 25; //Width and Height of each BarrierBlock.
    public static final float HEALTH = 5f; //Starting health of each pixel in each block
    /**
     * Instantiates a BarrierBlock.
     * @param xValue X-value of the top left corner.
     * @param yValue Y-value of the top left corner.
     * @param player The player that this BarrierBlock belongs to.
     */
    public BarrierBlock (int xValue, int yValue, int player) {
        x = xValue;
        y = yValue;
        playerNo = player;
        currHealth = maxHealth = (int)(Math.pow(SIZE, 2) * HEALTH);
    }

    /**
     * Draws the barrier block. Recommended to run in update loop.
     * @param g the Graphics of the panel where the BarrierBlock is being drawn.
     */
    public void draw (Graphics g) {
        
        if(currHealth <= 0) dead = true;
        else {
            healthPercent = (float)currHealth/(int)(Math.pow(SIZE, 2) * HEALTH);
            g.setColor((playerNo == 1) ? new Color(229/255f, 29/255f, 29/255f, healthPercent) : new Color(26/255f, 134/255f, 206/255f, healthPercent));
            g.fillRect(x, y, SIZE, SIZE);
        }
    }
}

