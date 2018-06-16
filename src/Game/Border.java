package Game;

import java.awt.*;

public class Border extends GameObject {
    private int position;

    public Border(int position, World world){
        super("Border", 100000000, 0, 100000000,0, 0, true, world);
        switch(position){
            case 0:
                this.x = 0;
                this.y = -100000000 + Role.PICOFFSET + 10;
                break;
            case 1:
                this.x = 0;
                this.y = 100000000 + world.getHeight() - Role.PICOFFSET;
                break;
            case 2:
                this.x = -100000000 + 10;
                this.y = 0;
                break;
            case 3:
                this.x = 100000000 + world.getWidth() - 10;
                this.y = 0;
                break;
        }
    }

    public void draw(Graphics g){
        g.drawArc(this.x,this.y, 100000000, 100000000, 0, 360);
    }

    public void collisionResponse(GameObject object){
        return;
    }

    public void onAttack(Weapon weapon){ }
}
