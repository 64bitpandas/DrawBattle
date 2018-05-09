import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
/**
    * Creates a new JFrame that allows players to type their name in to save a new record.
    * @author Ben Cuan
    * @since 15 May 2017
    * @param statID The id of the statistic to be written. Only 5-8 allowed.
    * @param IDVALUES:
    * @param 5 gameMinionsRecord
    * @param 6 gameMoneyRecord
    * @param 7 mostExpensiveMinionRecord
    * @param 8 gameDamageDealtRecord
*/
public class RecordPopup extends JFrame implements ActionListener {

    String text; //Player name
    InputField input; //Input field object
    int stat, playerNo; //Statistic Number and Player Number

    public RecordPopup (int statID, int player) {

        //Basic JFrame initialization
		super("New Record! : DrawBattle");
		setSize(500,300);				
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); 		
		setLocation(300, 300);
        setResizable(false);

        //Create new input field
        input = new InputField(this);

        //Create anonymous JPanel that will be shown
        setContentPane(new PopupContent(this, statID, input));

        //Set icon on top left
        setIconImage(new ImageIcon("images/favicon.png").getImage());
		
        stat = statID;
        playerNo = player;

		setVisible(true);
	}

    public void actionPerformed (ActionEvent e) {
        String cmd = e.getActionCommand();

        //Input field
        text = input.getText();

        //button pressed
        if(cmd.equals("Submit")) {
            Statistics.writeStatistic(stat, (stat == 5) ? Statistics.gameMinions[playerNo] : (stat == 6) ? Statistics.gameMoney[playerNo] : (stat == 7) ? Statistics.mostExpensiveMinion[playerNo] : (stat == 8) ? Statistics.gameDamageDealt[playerNo] : 0, text);
            setVisible(false);
            dispose();
        }
    }
}

/** *JPanel containing all components of popup */
class PopupContent extends JPanel {
    public PopupContent (ActionListener listener, int statID, InputField in) {
        setLayout(new BorderLayout(10,10));

        //Title
        add(new JLabel("New Record! " + ((statID == 5) ? "Most Minions Spawned" : (statID == 6) ? "Most Money Earned" : (statID == 7) ? "Most Expensive Minion" : (statID == 8) ? "Most Damage Dealt" : ""), SwingConstants.CENTER) 
            {{setFont(FontLoader.load("BebasNeue Bold", 40)); setPreferredSize(new Dimension(500,80));}}, BorderLayout.NORTH);

        //Input field
        add(in, BorderLayout.CENTER);

        //Submit button
        add(new BetterButton(BetterColors.GREEN, Color.WHITE, "Submit", listener) {{setPreferredSize(new Dimension(500,80));}}, BorderLayout.SOUTH);
        
    }
}

/** *Editable text field for players to enter their name */
class InputField extends JTextField {
    public InputField (ActionListener listener) {
        super("Your name");
        setActionCommand("Input");
        setFont(FontLoader.load("BebasNeue Regular", 50));
    }
}