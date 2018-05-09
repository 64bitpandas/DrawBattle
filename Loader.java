import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
    * JPanel that uses a Counter to improve transitions between different panels. 
    * There should be only ONE Loader per application instance. [Static]  
    * @author Ben Cuan
    * @since 21 Apr 2017
*/
public class Loader extends JPanel {

    private static String txt = "";
    private static JLabel textArea;

    public Loader () {

        setLayout(new BorderLayout());

        textArea = new JLabel("", SwingConstants.CENTER) {
            { 
                setPreferredSize(new Dimension(1000, 100));
                setFont(FontLoader.load("BebasNeue Bold", 80));
            }
        };
        add(textArea, BorderLayout.NORTH);
    }

    /**
     * Creates a loading screen. 
     * @param seconds Number of seconds to transition to the next state.
     * @param nextState the next GameState to transition to after loading.
     * @param text the text to be displayed above the counter.
     */
    public static void load(int seconds, Container.GameState nextState, String text) {
        
        MainWindow.loader.removeAll();

        MainWindow.frame.setState(Container.GameState.LOADING);
        MainWindow.currCounter = new Counter(seconds, 100, nextState);

        MainWindow.loader.add(MainWindow.currCounter);
        MainWindow.loader.add(textArea, BorderLayout.NORTH);
        MainWindow.loader.revalidate();
        MainWindow.loader.repaint();
        
        textArea.setText(text);
    }
}