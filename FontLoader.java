import java.awt.Font;
import java.io.File;
/**
    * Loads custom fonts from the fonts/ folder. TTF format only.
    * @author Ben Cuan
    * @since 21 Apr 2017
*/
public class FontLoader {

    /**
     * Loads custom fonts from the fonts/ folder using Font.createFont().
     * @param fontName The regular name of the font (e.g. 'Ubuntu'). Exclude filepath and extensions.
     * @param fontSize The px size of the font.
     * @return Font
     */
    public static Font load (String fontName, int fontSize) {
        
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("fonts/" + fontName + ".ttf")).deriveFont((float)fontSize);
        } catch (Exception e) {
            System.err.println("Error loading font " + fontName + ":\n" + e);
        }
        
        //This shouldn't run
        System.out.println("Something went wrong! Here is some nice Comic Sans to save the day.");
        return new Font("Comic Sans", Font.BOLD, 10);
    }
}