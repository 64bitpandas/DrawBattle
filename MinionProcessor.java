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
public class MinionProcessor {

    //Public Static Variables for player and minion statistics
    public static int[] money = new int[2], //Total money a player has at any given point
                        revenue = new int[2]; //Money a player receives per frame of combat
    
    public static int[][] minionHealth = new int[2][4], //Health of a single minion
                          minionCost = new int[2][4], //Cost of a single minion
                          minionSpeed = new int[2][4]; //Speed of a single minion 
 
    /** 
     * *Creates a minion with given stats:
     * @param player Either 1 or 2. The player that created this minion.
     * @param health Amount of health the minion has per pixel.
     * @param cost The price of the minion.
     * @param slot Minion slot. Must be an int from 1-4.
     * @param speed Speed of the minion in pixels/10ms. Recommended to be between 1 and 8.
     */
    public static void createMinion(int player, int health, int cost, int slot, int speed) {

        //ImageIO
        BufferedImage screenshot = new BufferedImage(500, 450, BufferedImage.TYPE_INT_ARGB); //Creates blank image (With transparency! Very important)
        MainWindow.drawing.lPanel.setBackground(new Color(1,1,1,0)); //Temporarily sets drawing panel's transparency to 0 for image capture process
        MainWindow.drawing.lPanel.repaint(); 
        MainWindow.drawing.lPanel.paint(screenshot.createGraphics()); //Saves image into screenshot
        File minionImage = new File("minions/p" + player + "slot" + slot + ".png");

        try {
            minionImage.createNewFile();
            ImageIO.write(screenshot, "png", minionImage);
        } catch (Exception e) {System.err.println(e);}

       minionHealth[player - 1][slot - 1] = health;
       minionCost[player - 1][slot - 1] = cost;
       minionSpeed[player - 1][slot - 1] = speed;
    }
}