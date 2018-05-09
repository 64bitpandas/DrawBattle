import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
/**
    * Uses CardLayout to create a tutorial section for new players.
    * @author Ben Cuan
    * @since 15 May 2017
*/
public class Tutorial extends JPanel {

    public CardLayout layout;

    public Tutorial () {
        //initalize CardLayout
        layout = new CardLayout();
        setLayout(layout);

        //add panels to frame
        add(new TutorialPanel(1));
        add(new TutorialPanel(2));
        add(new TutorialPanel(3));
        add(new TutorialPanel(4));
        add(new TutorialPanel(5));

        //setState(GameState.MENU);
    }
}

class TutorialPanel extends JPanel implements ActionListener {

    final ActionListener listener = this; //To be passed to the buttons

    public TutorialPanel(int panelNo) {
        final int panel = panelNo;
        setLayout(new BorderLayout(10,10));
        setBackground(Color.WHITE);

        //Title
        add(new JLabel("How to Play", SwingConstants.CENTER) {{setFont(FontLoader.load("BebasNeue Bold", 50)); setPreferredSize(new Dimension(1000, 100));}}, BorderLayout.NORTH);

        //FileIO - read a txt file and draw string
         final ArrayList<String> content = new ArrayList<String> ();
        try {
            Scanner fileScanner = new Scanner(new File("tutorial/" + panel + ".txt"));
            while(fileScanner.hasNext())
                content.add(fileScanner.nextLine());
        } catch (FileNotFoundException e) {System.err.println("File " + panel + " not found!");}

        //Add new JTextArea with the file content
        add(new JTextArea() {{
            setPreferredSize(new Dimension(500,400));
            setFont(new Font("Helvetica", Font.PLAIN, 24));
            setEditable(false);

            for(int i = 0; i < content.size(); i++)
                setText(" " + getText() + "\n" + content.get(i));
            
        }}, BorderLayout.WEST);

        //Image panel
        add(new JPanel() {
            {
            setPreferredSize(new Dimension(500,400));
            setBackground(Color.WHITE);
            }
        
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("tutorial/" + panel + ".png").getImage(), 0,0,null);
            }
        }, BorderLayout.EAST);

        //Buttons
        add(new JPanel() {{
            setPreferredSize(new Dimension(1000, 100));
            setLayout(new GridLayout(1,3));
            add(new BetterButton(BetterColors.BLUE, Color.WHITE, "Back", listener));
            add(new BetterButton(BetterColors.RED, Color.WHITE, "Close", listener));
            add(new BetterButton(BetterColors.BLUE, Color.WHITE, "Next", listener));
        }}, BorderLayout.SOUTH);
    }

    public void actionPerformed (ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println(cmd);
        if(cmd.equals("Next"))
            MainWindow.tutorial.layout.next(MainWindow.tutorial);
        if(cmd.equals("Back"))
            MainWindow.tutorial.layout.previous(MainWindow.tutorial);
        if(cmd.equals("Close"))
            MainWindow.frame.setState(Container.GameState.MENU);
    }
}