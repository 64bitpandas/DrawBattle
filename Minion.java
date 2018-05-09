import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
/**
 * Creates minions (comprising of image and text file) by capturing the statistics of drawn minions in DrawPanel.
 * @author Ben Cuan
 * @since 1 May 2017
 */
public class Minion {

    /** *Image that corresponds to this minion */
    public Image img = null;
    public boolean dead = false; //Health == 0 || Game over
    public int x, y; //Coordinates in the frame
    public int adjX, adjY; //Coordinates adjusted to be in the center of the minion
    public int playerNo; //Player that the minion belongs to
    public int currHealth; //Current health
    public int maxHealth; //Maximum health a minion can have

    private int speed, slotNo; //Statistics that do not need to be accessed outside of this class
    private float healthPercent;
    /**
     * Creates a new Minion.
     * @param player Player number that the minion belongs to. (1 or 2)
     * @param slot Minion slot (1 to 4)
     * @param origX The x value that the Minion begins at
     * @param origY The y value that the Minion begins at
     */
    public Minion (int player, int slot, int origX, int origY) {
        img = new ImageIcon("minions/p" + player + "slot" + slot + ".png").getImage();

        currHealth = maxHealth = MinionProcessor.minionHealth[player-1][slot-1];
        speed = MinionProcessor.minionSpeed[player-1][slot-1];

        playerNo = player;
        slotNo = slot;

        x = origX - 50;
        y = origY - 50;

        adjX = (playerNo == 1) ? origX : origX + 50;
        adjY = origY;
        
        MinionProcessor.money[player - 1] -= MinionProcessor.minionCost[player-1][slot-1];
        MinionProcessor.revenue[player - 1] += (int) ( MinionProcessor.minionCost[player - 1][slot - 1] / 1000 );

        Statistics.gameMoney[player - 1] += MinionProcessor.minionCost[player-1][slot-1];
        Statistics.gameMinions[player - 1] ++;

        if(Statistics.mostExpensiveMinion[player - 1] <  MinionProcessor.minionCost[player-1][slot-1])
            Statistics.mostExpensiveMinion[player - 1] =  MinionProcessor.minionCost[player-1][slot-1]; 
    }

    /** *Run in paintComponent(). Updates variables such as position, health, etc. */
    public void update (Graphics g) {

        if(currHealth <= 0) dead = true;
        if(!dead) {
            g.drawImage(img, x, y, 100, 100, null); //Actually creates the image on panel
            healthPercent = (float)currHealth/maxHealth;
            g.setColor((healthPercent > .75) ? BetterColors.GREEN : (healthPercent > .5) ? BetterColors.YELLOW : (healthPercent > .25) ? BetterColors.ORANGE : BetterColors.RED);
            g.fillRect(x, y - 30, (int)(50*healthPercent), 10); //Health bar
            x = (playerNo == 1) ? x + speed : x - speed; //If player == 1, go right. Else go left
            adjX = x + 50;
        }
    }
}