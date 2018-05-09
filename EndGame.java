import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Date;

import javax.swing.event.*;
import java.util.ArrayList;
/**
 * Screen that is shown when a player wins the game. Contains statistics and commands that players may find interesting or useful.
 * @author Ben Cuan
 * @since 10 May 2017
 */
public class EndGame extends JPanel {
    Font dFont = FontLoader.load("BebasNeue Regular", 25); //Default font
    Font bFont = FontLoader.load("BebasNeue Bold", 30); //Bold font
    Font tFont = FontLoader.load("BebasNeue Bold", 60); //Title font

    public EndGame () {
        setLayout(new BorderLayout());
        reload();
    }

    //Basically the constructor without actually creating a new object
    public void reload () {
        removeAll();

        //Header
        add(new JLabel("Player " + Statistics.winner + " wins!!!!", SwingConstants.CENTER) {{ 
            setFont(tFont);
            setPreferredSize(new Dimension(1000, 100));
        }}, BorderLayout.NORTH);

        //Statistics for P1 and P2
        add(new JLabel() {{
            setPreferredSize(new Dimension(1000, 400));
            setLayout(new GridLayout(5,2));
            add(new JLabel("PLAYER 1 STATS", SwingConstants.CENTER) {{setFont(bFont);}});
            add(new JLabel("PLAYER 2 STATS", SwingConstants.CENTER) {{setFont(bFont);}});
            add(new JLabel(Statistics.gameMinions[0] + " Minions", SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel(Statistics.gameMinions[1] + " Minions", SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("$" + Statistics.gameMoney[0] + " Spent", SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("$" + Statistics.gameMoney[1] + " Spent", SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel(Statistics.gameDamageDealt[0] + " Damage Dealt", SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel(Statistics.gameDamageDealt[1] + " Damage Dealt", SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("$" + Statistics.mostExpensiveMinion[0] + " Most Expensive Minion", SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("$" + Statistics.mostExpensiveMinion[1] + " Most Expensive Minion", SwingConstants.CENTER) {{setFont(dFont);}});
        }}, BorderLayout.CENTER);

        //Add onto global statistics
        for(int i = 0; i < 2; i++) {
            Statistics.addToValue(1, Statistics.gameMinions[i]);
            Statistics.addToValue(2, Statistics.gameMoney[i]);
            Statistics.addToValue(4, Statistics.gameDamageDealt[i]);
            Statistics.addToValue(3, Statistics.mostExpensiveMinion[i]);

            if(Statistics.gameMinions[i] > Statistics.readStatistic(5) && Statistics.gameMinions[i] > ((i == 0) ? Statistics.gameMinions[1] : Statistics.gameMinions[0])) new RecordPopup(5, i);
            if(Statistics.gameMoney[i] > Statistics.readStatistic(6) && Statistics.gameMoney[i] > ((i == 0) ? Statistics.gameMoney[1] : Statistics.gameMoney[0])) new RecordPopup(6, i);
            if(Statistics.gameDamageDealt[i] > Statistics.readStatistic(8) && Statistics.gameDamageDealt[i] > ((i == 0) ? Statistics.gameDamageDealt[1] : Statistics.gameDamageDealt[0])) new RecordPopup(8, i);
            if(Statistics.mostExpensiveMinion[i] > Statistics.readStatistic(7) && Statistics.mostExpensiveMinion[i] > ((i == 0) ? Statistics.mostExpensiveMinion[1] : Statistics.mostExpensiveMinion[0])) new RecordPopup(7, i);
        }

        //Buttons
        add(new BottomButtonPanel(), BorderLayout.SOUTH);

        revalidate();
    }
}

/** *Bottom two buttons- Save Minions & Back to Menu */
class BottomButtonPanel extends JPanel implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println(cmd);
        if(cmd.equals("Save Minions")) {
            String date = String.format("%tc", new Date()).replace(':', '-');
           new File ("minions/" + date).mkdirs();

           //Copy minion files
            for(int i = 1; i < 5; i++) {
                for(int j = 1; j < 3; j++) {
                    try {
                        Files.copy(new File("minions/p" + j + "slot" + i + ".png").toPath(), new File("minions/" + date + "/p" + j + "slot" + i + ".png").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING,java.nio.file.StandardCopyOption.COPY_ATTRIBUTES, java.nio.file.LinkOption.NOFOLLOW_LINKS);
                    } catch (IOException ex) {} //No minion found, that's accepted.
                 
                }
            }
        }
        if(cmd.equals("Back to Menu")) MainWindow.frame.setState(Container.GameState.MENU); 
        
    }

    public BottomButtonPanel () {
            setPreferredSize(new Dimension(1000, 100));
            setLayout(new GridLayout(1,2));

            add(new BetterButton(BetterColors.BLUE, Color.WHITE, "Save Minions", this) {{setPreferredSize(new Dimension(400,80));}});
            add(new BetterButton(BetterColors.ORANGE, Color.WHITE, "Back to Menu", this) {{setPreferredSize(new Dimension(400,80));}});
    }
}