import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.event.*;
import java.util.ArrayList;
/**
    * Panel where players can summon drawn minions to battle.
    * @author Ben Cuan
    * @since 28 Apr 2017
*/
public class BattlePanel extends JPanel {

    public Arena arena; //The arena class
    public Top topBar; //top bar with stats
    public BattlePanel () {
        setLayout(new BorderLayout());
        reload();
        
    }

    public void reload() { //Resets all states of variables and panels.
        removeAll();
        topBar = new Top();
        add(topBar, BorderLayout.NORTH);
        arena = new Arena();
        add(arena, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}

/** * The panel where minions are painted and will battle.*/
class Arena extends JPanel implements KeyListener, ActionListener, FocusListener {

    public ArrayList<Minion> minions = new ArrayList<Minion> (); //All minions that are currently being drawn in Arena
    protected int DETECT_DIST = 50; //max distance that registers as a collision

    Timer refresh = new Timer(10, this);
    Spawner spawn1, spawn2; //Spawner for each player
    public static Barrier barrier1, barrier2; //Barrier for each player. DO NOT initialize here; it should only be initialized once per run.
   
    public Arena() {
        addKeyListener(this);
        requestFocus();
        refresh.start();

        spawn1 = new Spawner(1);
        spawn2 = new Spawner(2);

        //Refresh all images
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 4; j++) 
                new ImageIcon("minions/p" + (i+1) + "slot" + (j+1) + ".png").getImage().flush();
        }
    }
    /** *Summons minions based on key pressed, or moves spawning center */
    public void keyPressed (KeyEvent e) {
        int key = e.getKeyCode();

        //P1 Minion Spawns
        if(key == KeyEvent.VK_1 && MinionProcessor.minionCost[0][0] < MinionProcessor.money[0] && MinionProcessor.minionCost[0][0] > 0) minions.add(new Minion(1,1,spawn1.xV,spawn1.yV)); 
        if(key == KeyEvent.VK_2 && MinionProcessor.minionCost[0][1] < MinionProcessor.money[0] && MinionProcessor.minionCost[0][1] > 0) minions.add(new Minion(1,2,spawn1.xV,spawn1.yV)); 
        if(key == KeyEvent.VK_3 && MinionProcessor.minionCost[0][2] < MinionProcessor.money[0] && MinionProcessor.minionCost[0][2] > 0) minions.add(new Minion(1,3,spawn1.xV,spawn1.yV)); 
        if(key == KeyEvent.VK_4 && MinionProcessor.minionCost[0][3] < MinionProcessor.money[0] && MinionProcessor.minionCost[0][3] > 0) minions.add(new Minion(1,4,spawn1.xV,spawn1.yV)); 
        //P2 Minion Spawns
        if(key == KeyEvent.VK_7 && MinionProcessor.minionCost[1][0] < MinionProcessor.money[1] && MinionProcessor.minionCost[1][0] > 0) minions.add(new Minion(2,1,spawn2.xV,spawn2.yV)); 
        if(key == KeyEvent.VK_8 && MinionProcessor.minionCost[1][1] < MinionProcessor.money[1] && MinionProcessor.minionCost[1][1] > 0) minions.add(new Minion(2,2,spawn2.xV,spawn2.yV)); 
        if(key == KeyEvent.VK_9 && MinionProcessor.minionCost[1][2] < MinionProcessor.money[1] && MinionProcessor.minionCost[1][2] > 0) minions.add(new Minion(2,3,spawn2.xV,spawn2.yV)); 
        if(key == KeyEvent.VK_0 && MinionProcessor.minionCost[1][3] < MinionProcessor.money[1] && MinionProcessor.minionCost[1][3] > 0) minions.add(new Minion(2,4,spawn2.xV,spawn2.yV));
        //P1 Spawner Move 
        if(key == KeyEvent.VK_W || key == KeyEvent.VK_A || key == KeyEvent.VK_S || key == KeyEvent.VK_D) spawn1.move(key, this);
        //P2 Spawner Move
        if(key == KeyEvent.VK_I || key == KeyEvent.VK_J || key == KeyEvent.VK_K || key == KeyEvent.VK_L) spawn2.move(key, this);

    }

    public void keyReleased (KeyEvent e) { //stop the spawners from moving
        int key = e.getKeyCode();
        //P1 Spawner Move 
        if(key == KeyEvent.VK_W || key == KeyEvent.VK_A || key == KeyEvent.VK_S || key == KeyEvent.VK_D) spawn1.keyPressed[key] = false;
        //P2 Spawner Move
        if(key == KeyEvent.VK_I || key == KeyEvent.VK_J || key == KeyEvent.VK_K || key == KeyEvent.VK_L) spawn2.keyPressed[key] = false;
    } 
    public void keyTyped (KeyEvent e) {} //should remain blank- nothing here

    int h1, h2, h3, h4; //temporary indicators for health
    public void actionPerformed (ActionEvent e) { //repaints & moves minions every 10 ms
        try {
             if(MainWindow.frame.currState == Container.GameState.COMBAT) {
                //Money management
                for(int i = 0; i < 2; i++)
                    MinionProcessor.money[i] += (MinionProcessor.revenue[i]);

                try { //Collision detection: If the distance between two minions OR a minion and a BarrierBlock is less than DETECT_DIST, then collision occurs
                    for(Minion mi : minions) {

                        h1 = mi.maxHealth / 10; //Amount to reduce health by on collision

                        if(mi.dead) { 
                            new Explosion(mi.x, mi.y);
                            minions.remove(mi);
                        }

                        for(Minion mi2 : minions) { 
                            if(mi2.dead) minions.remove(mi2);
                            if((Math.abs(mi2.adjX - mi.adjX) < DETECT_DIST) && (Math.abs(mi2.adjY - mi.adjY) < DETECT_DIST) && !mi.equals(mi2) && mi.playerNo != mi2.playerNo && mi2.currHealth >= 0 && mi.currHealth >= 0) {
                               
                                h2 = mi2.maxHealth / 10;

                                mi.currHealth -= h1;
                                mi2.currHealth -= h1;
                                Statistics.gameDamageDealt[mi.playerNo-1] += h1;
                                Statistics.gameDamageDealt[mi2.playerNo-1] += h1;
                            }
                            if(mi2.dead) minions.remove(mi2);
                        }

                        for(BarrierBlock block : barrier1.blocks) {
                            if(block.dead) barrier1.blocks.remove(block);
                            if((Math.abs(block.x - mi.adjX) < DETECT_DIST) && (Math.abs(block.y - mi.adjY) < DETECT_DIST) && mi.playerNo == 2 && mi.currHealth >= 0) {
                                h3 = block.maxHealth;
                                block.currHealth -= h1;
                                mi.currHealth -= h1;
                                Statistics.gameDamageDealt[mi.playerNo - 1] += h1;
                            }
                        }

                        for(BarrierBlock block : barrier2.blocks) {
                            if(block.dead) barrier2.blocks.remove(block);
                            if((Math.abs(block.x - mi.adjX) < DETECT_DIST) && (Math.abs(block.y - mi.adjY) < DETECT_DIST) && mi.playerNo == 1 && mi.currHealth >= 0) {
                                h4 = block.maxHealth;
                                block.currHealth -= h1;
                                mi.currHealth -= h1;

                                Statistics.gameDamageDealt[mi.playerNo - 1] += h1;
                            }
                        }
                        
                        if(mi.adjX < 0 || mi.adjX > 1000) { //Someone got a minion through to win!
                            Statistics.winner = mi.playerNo;
                            minions.clear();
                            MainWindow.frame.setState(Container.GameState.ENDGAME); 
                            
                        }

                        if(mi.dead) { 
                            new Explosion(mi.x, mi.y);
                            minions.remove(mi);
                        }
                    }
                } catch(Exception ex) {} //Throws a long but useless error when minion dies. Don't worry it does not affect anything.

                repaint();
                try{MainWindow.combat.topBar.refresh();} catch(Exception ex) {} //another useless error that only runs once before topBar is initialized.

                try { //moves spawner if key(s) pressed
                spawn1.tryMove();
                spawn2.tryMove();
                } catch (Exception ex) {System.out.println("[Info] Spawners not yet initialized. Spawner.tryMove() did not run.");}
            }
        } catch (Exception exe) {} //This will always throw a NullPointerException on initialization because BattlePanel is initalized before Container
       
    } 

    public void detectCollision(Object obj1, Object obj2) {

    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        requestFocus();

        //Middle barrier
        g.setColor(Color.BLACK);
        g.fillRect(495,0,10,500);

        for(BarrierBlock block : barrier1.blocks) block.draw(g);
        for(BarrierBlock block : barrier2.blocks) block.draw(g);

        spawn1.draw(g);
        spawn2.draw(g);

        for(Minion mi : minions) mi.update(g);
        for(Explosion exp : Explosion.explosions) g.drawImage(exp.currImage, exp.x, exp.y, 100, 100, this);
        
    }

    //FocusEvents start and stop timer in order to lower CPU usage.
    public void focusGained (FocusEvent e) {
        refresh.start();
    }

    public void focusLost (FocusEvent e) {
        refresh.stop();
    }
}

/** *The top panel where time left and stats are shown for both players. */
class Top extends JPanel {

    public StatBar stat1, stat2;
    public Top () {
        removeAll();
        setPreferredSize(new Dimension(1000, 100));
        setLayout(new BorderLayout());

        try { //This will only run AFTER initial instantiation since MainWindow.frame will return null first time!
            if(MainWindow.frame.currState != Container.GameState.MENU) {
            MainWindow.currCounter = new Counter(45,30, Container.GameState.DRAWING, "Player 1, get ready to draw!") {
                public void actionPerformed(ActionEvent e) { //Overrides actionPerformed
                    super.actionPerformed(e);
                    if(MainWindow.frame.currState == Container.GameState.ENDGAME) stepTimer.stop();

                }
            };
            add(MainWindow.currCounter, BorderLayout.CENTER);
         }
        } catch (Exception e) {}

        //Player 1 StatBar
        stat1 = new StatBar(1);
        add(stat1, BorderLayout.WEST);
        //Player 2 StatBar
        stat2 = new StatBar(2);
        add(stat2, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    public void refresh() {
        stat1.refresh();
        stat2.refresh();
    }
}

/** *Statistics Bar that is embedded into Top */
class StatBar extends JPanel {
    
    public int player;
    /**
     * @param playerNo Player number. Either 1 or 2 is accepted.
     */
    public StatBar (int playerNo) {
        setLayout(new GridLayout(1,5));
        setPreferredSize(new Dimension(450,100));
        player = playerNo;
        refresh();
    }

    public void refresh () {
        removeAll();

        final Font customFont = FontLoader.load("BebasNeue Bold", 20);
        //Name of player
        add(new JLabel("PLAYER " + player) {{setFont(customFont);}});

        //Player money indicator
        add(new JLabel("$ " + MinionProcessor.money[player - 1]) {
            {
                setFont(customFont);
            }
        });

        //Player revenue indicator
        add(new JLabel("" + MinionProcessor.revenue[player - 1], SwingConstants.CENTER) {{
            setIcon(new ImageIcon("images/revenue.png"));
            setFont(customFont);
            setText("$ " + MinionProcessor.revenue[player - 1] + "/frame");
         }});

        revalidate();
        repaint();
    }
}

/** *Location for the spawner where minions will originate. */
class Spawner {
    
    /** *Player number that the spawner belongs to. */
    public int playerNo;
    /** *Current coordinates of the spawner in its Arena. */
     public int xV, yV;
    /** *Current keys being pressed. Each index corresponds to the keyCode of the key */
    public boolean[] keyPressed = new boolean[100];
    /** *Is the spawner being moved currently? */
    public boolean moving = false;

    private final int SPEED = 5; //how much to move per repaint cycle
    private Image img;
    private int xMax, yMax, xMin, yMin; //The bounds in which the Spawner is allowed to move in
    /** 
     * *Creates a Spawner for a player
     * @param player number that the spawner belongs to.
     */
    public Spawner (int player) {
        img = (player == 1) ? new ImageIcon("images/blackhole-red.png").getImage() : new ImageIcon("images/blackhole-blue.png").getImage();
        playerNo = player;

        xV = (playerNo == 1) ? 50 : 950;
        yV = 250;
    }

    /**
     * Moves the spawner by 10 units left/right or 10 units up/down.
     * @param keyCode The integer value of the key pressed event.
     * @param arena The Arena Panel. Should be 'this' in most cases.
     */
    public void move(int keyCode, Arena arena) {
        keyPressed[keyCode] = true;

        //Set bounds
        xMax = (playerNo == 1) ? arena.getWidth() / 2 - 50 : arena.getWidth() - 50;
        xMin = (playerNo == 1) ? 50 : arena.getWidth() / 2 + 50;
        yMax = arena.getHeight();
        yMin = 50;
       
    }

    /** *Place this in a repeating timer to run. Moves if all conditions are right. */
    public void tryMove () {
        if(playerNo == 1) {
            if(keyPressed[KeyEvent.VK_W] && yV > yMin) yV -= SPEED; 
            if(keyPressed[KeyEvent.VK_A] && xV > xMin) xV -= SPEED; 
            if(keyPressed[KeyEvent.VK_S] && yV < yMax) yV += SPEED; 
            if(keyPressed[KeyEvent.VK_D] && xV < xMax) xV += SPEED; 
        } else if(playerNo == 2) {
            if(keyPressed[KeyEvent.VK_I] && yV > yMin) yV -= SPEED; 
            if(keyPressed[KeyEvent.VK_J] && xV > xMin) xV -= SPEED; 
            if(keyPressed[KeyEvent.VK_K] && yV < yMax) yV += SPEED; 
            if(keyPressed[KeyEvent.VK_L] && xV < xMax) xV += SPEED; 
        }
    }

    /** * Draws the spawner image at its x-y location.*/
    public void draw(Graphics g) {
        g.drawImage(img, xV - 50, yV - 50, 100, 100, null);
    }
}