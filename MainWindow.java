import javax.swing.*;
import java.awt.*;
import java.io.File;
/**
    * Contains the main method and main frame layout for the game. Accepts no parameters, is static. All non-static fields and methods are declared as private.
    * There should be only ONE MainWindow per application instance.  
    * @author Ben Cuan
    * @since 18 Apr 2017
*/
public class MainWindow extends JFrame {

    /** *The Container for the game, which contains currState and enum GameState  */
    public static Container frame;
    /** *The loading screen */
    public static Loader loader;
    /** *Drawing Panel */
    public static DrawPanel drawing;
    /** *Combat Panel */
    public static BattlePanel combat;
    /** *Endgame Panel */
    public static EndGame end;
    /** *Currently displayed Counter */
    public static Counter currCounter;
    /** *Global Statistics Panel */
    public static StatPanel stats;
    /** *How to Play */
    public static Tutorial tutorial;

    public MainWindow()
	{
        //Basic JFrame initialization
		super("DrawBattle Alpha 1.0.0");
		setSize(1000,600);				
		setDefaultCloseOperation(EXIT_ON_CLOSE); 		
		setLocation(100,100);
        setResizable(false);

        //Create the JPanels: frame is always last
        loader = new Loader();
        drawing = new DrawPanel();
        combat = new BattlePanel();
        end = new EndGame();
        stats = new StatPanel();
        tutorial = new Tutorial();
        
        frame = new Container();
    
        setContentPane(frame);

        //Set icon on top left
        setIconImage(new ImageIcon("images/favicon.png").getImage());

        //Initalize Statistics           
        Statistics.refreshScanner();
		
		setVisible(true);
	}

    public static void main(String [] args) {
        MainWindow mw = new MainWindow();
    }
}

