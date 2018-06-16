package Game;

import java.awt.*;

public class Wall extends GameObject{

    public Wall(int x, int y, World world) {
        super("Wall", 50, 0, 99999, x, y, true, world);
    }

    public void draw(Graphics g){
        g.drawImage(imgMap.get(name), x - 50, y - 75, 100, 150, null);
    }
    public void collisionResponse(GameObject object){

    }
    public void onAttack(Weapon weapon){

    }
}
