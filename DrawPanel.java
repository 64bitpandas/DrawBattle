import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.event.*;
import java.util.*;
/**
    * Panel where players can draw their own minions.
    * @author Ben Cuan
    * @since 21 Apr 2017
*/
public class DrawPanel extends JPanel implements ActionListener, ChangeListener {

/** 1 or 2. 0 = before first run; >2 = error */
public byte player;
public int brushSize, health, cost, multiplier, slot, speed, count; //All pretty self explanatory. Size of brush, Health of current minion, Cost of current minion, and Cost per brushsize of currently selected color.
public Color brushColor; //Currently selected brush color

//All Swing elements are static, as they are statically referenced
public static BetterSlider healthSlider, brushSlider;
public static JComboBox brushTypeBox;
public static JPanel leftDisplay, rightDisplay;
public static RightPanel rPanel;
public static LeftPanel lPanel;
public static SlotButtons sButtons;

    public DrawPanel () {
        setLayout(new BorderLayout(5,5));
        setBackground(Color.BLACK);
        reload();
    }

    /** *Sets up the DrawPanel upon loading from Container. */
    public void reload() {
       removeAll();
       add(new TopBar(), BorderLayout.NORTH);
       lPanel = new LeftPanel();
       add(lPanel, BorderLayout.WEST);
        rPanel = new RightPanel();
        add(rPanel, BorderLayout.CENTER);

        brushSize = 20; health = 10; cost = 0; multiplier = 1; brushColor = BetterColors.RED; slot = 1; count = 0; //Reset all variables for each run
        revalidate();

        //Managing state
        if(player == 0) player = 1;
        else if(player == 1) player = 2;
        else if(player == 2) player = 0;
    }

    /** *For the color buttons and slot radiobuttons. This should be the only ActionListener in DrawPanel.*/
    public void actionPerformed (ActionEvent e) {

        for(int i = 0; i < 4; i++)
            if(e.getSource().equals(sButtons.buttons[i])) slot = i + 1;
            
        
        Color cmd = BetterColors.stringToColor(e.getActionCommand());
        int cmd2 = BetterColors.stringToCost(e.getActionCommand());
        brushColor = cmd.equals(BetterColors.NULL) ? brushColor : cmd;
        multiplier = cmd2 == -1 ? multiplier : cmd2;
   
    }
    /** *For the sliders */
    public void stateChanged (ChangeEvent e) {
        health = healthSlider.getValue();
        brushSize = brushSlider.getValue();
        leftDisplay.repaint();
        rightDisplay.repaint();
    }
    /** *Runs when the counter reaches zero. Determines what the next state should be based on player going. (P1 -> P2 paint, P2 -> Battle) */
    public void switchState () {

        if(player == 2) { //currently player 1
            MinionProcessor.createMinion(1, health * count, cost * health, slot, speed);
            Loader.load(3, Container.GameState.DRAWING, "Player 2's Turn!"); 
        } else if (player == 1) { //currently player 2
            MinionProcessor.createMinion(2, health * count, cost * health, slot, speed);
            Loader.load(3, Container.GameState.COMBAT, "Get ready to fight!");
        }

        else if(player > 2) {
            System.err.println("ERROR: Invalid player number " + player);
            System.exit(1);
        }
    }
}
/** *Contains important information such as money, time left, minion cost. */
class TopBar extends JPanel {

    public TopBar () {
        removeAll();
        setPreferredSize(new Dimension(1000, 100));
        setLayout(new BorderLayout());

        try { //This will only run AFTER initial instantiation since MainWindow.frame will return null first time!
            if(MainWindow.frame.currState != Container.GameState.MENU) {
            MainWindow.currCounter = new Counter(15,30) { 
                public void actionPerformed(ActionEvent e) { //Overrides actionPerformed
                    super.actionPerformed(e);
                    if(secsLeft <= 0.5f)
                        MainWindow.drawing.switchState();
                }
            };
            add(MainWindow.currCounter, BorderLayout.CENTER);
         }
        } catch (Exception e) {}


        //Minion Cost Indicator 
        MainWindow.drawing.leftDisplay = new JPanel() {
            { setPreferredSize(new Dimension(450, 100)); }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(FontLoader.load("BebasNeue Bold", 50));
                g.drawString("Minion Cost: " + MainWindow.drawing.cost * MainWindow.drawing.health, 50, 70);
            }
        };
        add(MainWindow.drawing.leftDisplay, BorderLayout.WEST);

        //Money and health
         MainWindow.drawing.rightDisplay = new JPanel() {
            { setPreferredSize(new Dimension(450, 100)); }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(FontLoader.load("BebasNeue Bold", 50));
                g.drawImage(new ImageIcon("images/health.png").getImage(), 50, 20, 50, 50, this);
                g.drawString("" + MainWindow.drawing.health * MainWindow.drawing.count, 120, 70);
                g.drawString("Speed: " + (MainWindow.drawing.speed), 220, 70);
            }
        };
        add(MainWindow.drawing.rightDisplay, BorderLayout.EAST);

        revalidate();
        repaint();
    }
}
/** *Panel where players can draw by dragging the mouse. Image is saved to /minions/.*/
class LeftPanel extends JPanel implements MouseMotionListener {

private ArrayList<String> xValues = new ArrayList<String>(), yValues = new ArrayList<String>(), sizes = new ArrayList<String>(), runningAverage = new ArrayList<String>();
private ArrayList<Color> colors = new ArrayList<Color>();

    public LeftPanel () {
        setPreferredSize(new Dimension(500, 500));
        addMouseMotionListener(this); 
        setBackground(Color.WHITE);
    }

    /** Draws onto the screen based on the drawing type (FREE, RECT, OVAL) */
    public void mouseDragged (MouseEvent e) {
        
        xValues.add("" + e.getX());
        yValues.add("" + e.getY());
        colors.add(MainWindow.drawing.brushColor);
        sizes.add("" + MainWindow.drawing.brushSize);
        runningAverage.add("" + MainWindow.drawing.multiplier);
        MainWindow.drawing.cost += (MainWindow.drawing.multiplier * MainWindow.drawing.brushSize);
        MainWindow.drawing.count ++;

        int avg = 0;
        for(String itm : runningAverage)
            avg += Integer.parseInt(itm);
        
        MainWindow.drawing.speed = avg/runningAverage.size();
        MainWindow.drawing.leftDisplay.repaint();
        MainWindow.drawing.rightDisplay.repaint();
        repaint();
    }

    public void mouseMoved (MouseEvent e) {repaint();} //Should remain empty- nothing to do here

    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        int x, y, size;

        //Every dot drawn will be painted at the same time
        for(int i = 0; i < xValues.size(); i++) {
            x = Integer.parseInt(xValues.get(i));
            y = Integer.parseInt(yValues.get(i));
            size = Integer.parseInt(sizes.get(i));
            g.setColor(colors.get(i));
            g.fillOval(x-size/2, y-size/2, size, size);
        }
    }
}
/** *Panel where players can customize options: Brush size/fill, brush color, brush type, minion health, minion slot */
class RightPanel extends JPanel {
    public RightPanel () {
        setPreferredSize(new Dimension(500, 500));
        setLayout(new GridLayout(5,1,5,5));

        //Color palette panel. Contains 8 different buttons corresponding to each color.
        add(new JPanel() {
            {
                setLayout(new GridLayout(2,4));
                add(new ColorButton("red", 1));
                add(new ColorButton("green", 2));
                add(new ColorButton("blue", 3));
                add(new ColorButton("orange", 4));
                add(new ColorButton("yellow", 5));
                add(new ColorButton("purple", 6));
                add(new ColorButton("grey", 7));
                add(new ColorButton("darkgrey", 8));
            }
        });

        //Brush Size
        MainWindow.drawing.brushSlider = new BetterSlider("Brush Size");
        add(MainWindow.drawing.brushSlider);

        //Minion Health
        MainWindow.drawing.healthSlider = new BetterSlider("Minion Health");
        add(MainWindow.drawing.healthSlider);

        //Minion Slot
        MainWindow.drawing.sButtons = new SlotButtons();
        add(MainWindow.drawing.sButtons);
    }
}



//Below are utility classes used by the main DrawPanel JPanels (above)



/**
 * Simple preset for JSliders. Since DrawPanel is the only panel to require JSliders, this preset is nested within DrawPanel.java.
 * @param name The name (and command) that corresponds to the JSlider.
 */
class BetterSlider extends JSlider {

    String title = "";

    public BetterSlider (String name) {
        super(JSlider.HORIZONTAL,10,100,20);		// construct slider bar 
        setMajorTickSpacing(20);	// create tick marks on slider every 20 units
        setMinorTickSpacing(5);
        setForeground(BetterColors.PURPLE);
        setPaintTicks(true);
        setLabelTable(createStandardLabels(20) ); // create labels on tick marks
        setPaintLabels(true);
        addChangeListener(MainWindow.drawing);
        title = name;
    }

        public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(title, 10, 10);
    }
}
/**
 * Simple preset for the titleless color-choosing palette on RightPanel
 * @param color The color, in lowercase, that the button represents.
 * @param multiplier The amount of credits required per pixel.
 */
class ColorButton extends BetterButton {

    public int cost;
    public ColorButton (String color, int multiplier) {
        super(BetterColors.stringToColor(color), new Color(1,1,1,0), color, MainWindow.drawing);
        cost = multiplier;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor((isSelected() ? Color.BLACK : Color.WHITE));
        g.setFont(FontLoader.load("BebasNeue Regular", 30));
        g.drawString(cost + "x", 45,30);
    }
}       
/**
 * Contains a ButtonGroup of RadioButtons to determine slot number.
 */
class SlotButtons extends JPanel {

    ButtonGroup bg;
    public JRadioButton[] buttons = new JRadioButton[4]; //Corresponds to slot. 
    public SlotButtons() {
        bg = new ButtonGroup();
        setLayout(new GridLayout(1,4));
        setForeground(BetterColors.PURPLE);

        for(int i = 0; i < 4; i++) {
            buttons[i] = new JRadioButton("Slot " + (i+1)) {{setForeground(BetterColors.PURPLE);}};
            bg.add(buttons[i]);
            buttons[i].addActionListener(MainWindow.drawing);
            add(buttons[i]);
        }

        buttons[0].setSelected(true);
    }
}