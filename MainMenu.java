import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.event.*;
import java.io.File;

/**
    * Main Menu for the game, shows four buttons and title.
    * @author Ben Cuan
    * @since 20 Apr 2017
*/
public class MainMenu extends JPanel {

    public MainMenu () {
       
        setLayout(new BorderLayout());

        //Title on top
        add(new JLabel("Draw Battle", SwingConstants.CENTER) {{setPreferredSize(new Dimension(1000, 100)); setFont(new Font("Impact", Font.PLAIN, 70));}}, BorderLayout.NORTH);

        //Spacers on left, right, bottom
        add(new JLabel() {{setPreferredSize(new Dimension(200, 600));}}, BorderLayout.EAST);
        add(new JLabel() {{setPreferredSize(new Dimension(200, 600));}}, BorderLayout.WEST);
        add(new JLabel() {{setPreferredSize(new Dimension(1000, 20));}}, BorderLayout.SOUTH);

        //Main Menu Buttons
        add(new ButtonPanel(), BorderLayout.CENTER);
    }
}

/**
 * Contains the four buttons for Main Menu [Play, Options, How To Play, Exit]
 * @author Ben Cuan
 * @since 20 Apr 2017
 */
class ButtonPanel extends JPanel implements ActionListener {

    public ButtonPanel () {
        setLayout(new GridLayout(4,1,50,20));
        add(new BetterButton(BetterColors.BLUE, Color.WHITE, "Play", this));
        add(new BetterButton(BetterColors.GREEN, Color.WHITE, "How to Play", this));
        add(new BetterButton(BetterColors.ORANGE, Color.WHITE, "Statistics", this));
        add(new BetterButton(BetterColors.RED, Color.WHITE, "Exit", this));
    }

    public void actionPerformed (ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println(cmd);
        if(cmd.equals("Play")) {

            //Deletes all previously created minions
            for(int i = 1; i < 5; i++) {
                for(int j = 1; j < 3; j++) {
                    File fileToDelete = new File("minions/p" + j + "slot" + i + ".png");
                    fileToDelete.delete();
                }
            }

            //Set initial values
            for(int i = 0; i < 2; i++) {
                MinionProcessor.money[i] = 100000;
                MinionProcessor.revenue[i] = 50;
            }

            //resets per-game stats
            Statistics.softResetStatistics();

            //Spawns barriers
            MainWindow.combat.arena.barrier1 = new Barrier(1);
            MainWindow.combat.arena.barrier2 = new Barrier(2);
            
            Loader.load(3,Container.GameState.DRAWING, "Get ready to draw, player 1!");
        }
        if(cmd.equals("Statistics")) MainWindow.frame.setState(Container.GameState.STATS);
        if(cmd.equals("How to Play")) MainWindow.frame.setState(Container.GameState.TUTORIAL);
        if(cmd.equals("Exit")) System.exit(0);
    }
}