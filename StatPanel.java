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
 * Screen that is shown when the "Statistics" button is clicked in MainMenu. Displays global statistic records based on records.stats file.
 * This is based on EndGame.java.
 * @author Ben Cuan
 * @since 15 May 2017
 */
public class StatPanel extends JPanel {
    Font dFont = FontLoader.load("BebasNeue Regular", 25); //Default font
    Font bFont = FontLoader.load("BebasNeue Bold", 30); //Bold font
    Font tFont = FontLoader.load("BebasNeue Bold", 60); //Title font

    public StatPanel () {
        setLayout(new BorderLayout());
        reload();
    }

    //Basically the constructor without actually creating a new object
    public void reload () {
        removeAll();

        //Header
        add(new JLabel("Global Records", SwingConstants.CENTER) {{ 
            setFont(tFont);
            setPreferredSize(new Dimension(1000, 100));
        }}, BorderLayout.NORTH);

        //Statistics for P1 and P2
        add(new JLabel() {{
            setPreferredSize(new Dimension(1000, 400));
            setLayout(new GridLayout(5,2));
            add(new JLabel("Totals [All Games]", SwingConstants.CENTER) {{setFont(bFont);}});
            add(new JLabel("Best [Single Game]", SwingConstants.CENTER) {{setFont(bFont);}});
            add(new JLabel("Total Minions Sent: " + Statistics.readStatistic(1) , SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("Minions Sent: " + Statistics.readStatisticString(5) , SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("Total Money Spent: $" + Statistics.readStatistic(2) , SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("Money Spent: $" + Statistics.readStatisticString(6) , SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("Cost of all Most Expensive Minions: $" + Statistics.readStatistic(3) , SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("Most Expensive Minion: $" + Statistics.readStatisticString(7) , SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("Total Damage Dealt: " + Statistics.readStatistic(4) , SwingConstants.CENTER) {{setFont(dFont);}});
            add(new JLabel("Most Damage Dealt: " + Statistics.readStatisticString(8) , SwingConstants.CENTER) {{setFont(dFont);}});
        }}, BorderLayout.CENTER);

        //Buttons
        add(new BottomButtonPanel2(), BorderLayout.SOUTH);

        revalidate();
    }
}

/** *Bottom two buttons- Clear Stats & Back to Menu */
class BottomButtonPanel2 extends JPanel implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println(cmd);
        if(cmd.equals("Clear Records"))
            Statistics.resetStatistics();
        if(cmd.equals("Back to Menu")) MainWindow.frame.setState(Container.GameState.MENU); 
        
    }

    public BottomButtonPanel2 () {
            setPreferredSize(new Dimension(1000, 100));
            setLayout(new GridLayout(1,2));

            add(new BetterButton(BetterColors.BLUE, Color.WHITE, "Clear Records", this) {{setPreferredSize(new Dimension(400,80));}});
            add(new BetterButton(BetterColors.ORANGE, Color.WHITE, "Back to Menu", this) {{setPreferredSize(new Dimension(400,80));}});
    }
}