import java.awt.Color;
/**
 * Contains definitions for more asthetically pleasing color choices in order to promote uniform color design throughout the game.
 * BetterColors were chosen based on Google's Material Design guidelines.
 * All default Color configurations (RED, BLUE, YELLOW, etc) should be available here EXCEPT for Black and White (these are unchanged).
 */
public class BetterColors {
    public static final Color 
        GREEN = new Color(46,204,113),
        RED = new Color(231,76,60),
        BLUE = new Color(52,152,219),
        ORANGE = new Color(230,126,34),
        YELLOW = new Color(241,196,15),
        PURPLE = new Color(155,89,182),
        GREY = new Color(127,140,141),
        DARK_GREY = new Color(52,73,94),
        NULL = new Color(0,0,0,0); //Occurs when String color is invalid in stringToColor
    
    /** 
     * Converts a string to a BetterColor.
     * @param color Name of color.
     * @return Color
     */
    public static Color stringToColor(String color) {
        return (color.equals("red")) ? RED : 
                (color.equals("green")) ? GREEN : 
                (color.equals("blue")) ? BLUE : 
                (color.equals("orange")) ? ORANGE :
                (color.equals("yellow")) ? YELLOW :
                (color.equals("purple")) ? PURPLE :
                (color.equals("grey")) ? GREY :
                (color.equals("darkgrey")) ? DARK_GREY : NULL;
    }

    /**
     * Converts a string to int (cost of color). 
     * @param color Name of color.
     * @return int
    */
    public static int stringToCost(String color) {
        return (color.equals("red")) ? 1 : 
                (color.equals("green")) ? 2 : 
                (color.equals("blue")) ? 3 : 
                (color.equals("orange")) ? 4 :
                (color.equals("yellow")) ? 5 :
                (color.equals("purple")) ? 6 :
                (color.equals("grey")) ? 7 :
                (color.equals("darkgrey")) ? 8 : -1;
    }
}