package Game;

import java.awt.*;
import java.util.Random;

public class Blood extends GameObject{
    private int picX;
    private int picY;

    public Blood(int x, int y, World world) {
        super("Blood", 0, 0, 99999, x, y, false, world);
        Random rand = new Random();
        int picX = Math.abs(rand.nextInt()) % 2;
        int picY = Math.abs(rand.nextInt()) % 2;
        this.picX = picX * 475;
        this.picY = picY * 475;
    }

    public void draw(Graphics g){

        g.drawImage(imgMap.get(name), x- 30, y, x + 30, y + 45, picX, picY, picX + 475, picY + 475, null);
      //  g.drawImage(imgMap.get(name))
    }
    public void collisionResponse(GameObject object){

    }
    public void onAttack(Weapon weapon){

    }
}
