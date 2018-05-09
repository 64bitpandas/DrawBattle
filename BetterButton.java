import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Basic, easily customizable button that expands upon the default JButton.
 * @author Ben Cuan
 * @since 20 Apr 2017
  * @param background The background color
     * @param foreground The foreground color
     * @param text The name of the button to be displayed
     * @param listener The ActionListener to be used by the JButton
 */
public class BetterButton extends JButton {

    private Color fg;
    private String name;

    /**
     * Create a BetterButton the same way as a JButton.
     * @param background The background color
     * @param foreground The foreground color
     * @param text The name of the button to be displayed
     * @param listener The ActionListener to be used by the JButton
     */
    public BetterButton (Color background, Color foreground, String text, ActionListener listener) {
        super(text);
        name = text;
        setBackground(background);
        setForeground(foreground);
        setFocusPainted(false);
        setFont(FontLoader.load("BebasNeue Bold", 40));
        addActionListener(listener);
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor(fg);
    }
}
    