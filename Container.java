import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.event.*;
/**
    * Uses CardLayout to organize all subpanels. This is the only panel directly added onto MainWindow.
    * Contains MainMenu, DrawPanel, CombatPanel.
    * There should be only ONE Container per application instance.  
    * @author Ben Cuan
    * @since 20 Apr 2017
*/
public class Container extends JPanel {

    /** *Current state of the game: MENU, DRAWING, COMBAT, ENDGAME */
    public GameState currState;
    private CardLayout layout;

    /**
     * Panel that will be shown. <p>
     * MENU: Main Menu <p>
     * DRAWING: When the players draw their minions. <p>
     * COMBAT: When the players are actively fighting using drawn minions. <p>
     * ENDGAME: Who won, with statistics. <p>
     * LOADING: Transition between states. <p>
     * TUTORIAL: How to Play section. <p>
     * STATS: Viewing the global statistics from MainMenu.
     */
    public static enum GameState {
        MENU,
        DRAWING,
        COMBAT,
        ENDGAME,
        LOADING,
        TUTORIAL,
        STATS
    }

    public Container () {
        //initalize CardLayout
        layout = new CardLayout();
        setLayout(layout);

        //add panels to frame
        add(new MainMenu(), "MainMenu");
        add(MainWindow.drawing, "DrawPanel");
        add(MainWindow.loader, "LoadingScreen");
        add(MainWindow.combat, "CombatPanel");
        add(MainWindow.end, "EndgamePanel");
        add(MainWindow.stats, "StatsPanel");
        add(MainWindow.tutorial, "Tutorial");

        setState(GameState.MENU);
    }

    /**
     * Moves game to one of several predefined states.
     * Consider using Loader.load() for better transitions.
     * Also use MainWindow.frame.setState() to access from a static context.
     * @param state GameState.MENU, DRAWING, COMBAT, ENDGAME
     * @author Ben Cuan
     * @since 2017 Apr 20
     */
    public void setState(GameState state) {
        
        if(currState != state || state == GameState.DRAWING) {
            if(state == GameState.MENU) layout.show(this, "MainMenu");
            if(state == GameState.DRAWING) {
                MainWindow.drawing.reload();
                layout.show(this, "DrawPanel");
            }
            if(state == GameState.COMBAT) {
                MainWindow.combat.reload();
                layout.show(this, "CombatPanel");
            }
            if(state == GameState.LOADING) layout.show(this, "LoadingScreen");
            if(state == GameState.ENDGAME) {
                MainWindow.end.reload();
                layout.show(this, "EndgamePanel");
            }
            if(state == GameState.STATS) {
                MainWindow.stats.reload();
                layout.show(this, "StatsPanel");
            }
            if(state == GameState.TUTORIAL) {
                layout.show(this, "Tutorial");
            }

        }
        
        currState = state;
    }
}