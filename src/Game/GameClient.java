package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameClient extends Frame {
    public static final int WORLD_WIDTH = 960;
    public static final int WORLD_HEIGHT = 720;
    private Image offScreenImage;
    private World world;

    public GameClient(boolean Doubleplayer){
        this.world = new World(WORLD_WIDTH, WORLD_HEIGHT, Doubleplayer);
        offScreenImage = null;
    }

    private class PaintThread implements Runnable {
        public void run() {
            while(true) {
                repaint();     //璋冪敤鐨勫閮ㄧ被鐨刾aint()鏂规硶锛�   repaint()棣栧厛璋冪敤update()鏂规硶锛屽啀璋冪敤paint()鏂规硶
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void paint(Graphics g) {
    	if(!world.End())
    		world.drawWorld(g);
    	else {
    		world.drawEnd(g);
    	}
    }

    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(WORLD_WIDTH, WORLD_HEIGHT);   //鍒涘缓鍥剧墖
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.lightGray);
        gOffScreen.fillRect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void lauchFrame() {
        this.setLocation(400, 100);
        this.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        this.setTitle("ZombieCrisis");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false); //鍙皟鏁村ぇ灏�
        this.setBackground(Color.lightGray);
        this.addKeyListener(new KeyMonitor((Hero) world.getObject(0)));
        if(world.getObject(1) instanceof Hero)
            this.addKeyListener(new KeyMonitor((Hero) world.getObject(1)));
        setVisible(true);
        new Thread(new PaintThread()).start();
    }

    private class KeyMonitor extends KeyAdapter {
        Hero hero;
        public KeyMonitor(Hero hero){
            this.hero = hero;
        }
        public void keyReleased(KeyEvent e) {
            this.hero.keyReleased(e);
        }
        public void keyPressed(KeyEvent e) {
            this.hero.KeyPressed(e);
        }
    }
}
