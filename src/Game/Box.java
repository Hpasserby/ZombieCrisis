package Game;

import java.awt.*;
import java.util.Random;

public class Box extends GameObject{
    public static final int DELAYTIME = 800;
    private int delay = 0;

    public Box(int x, int y, World world) {
        super("Box", 14, 0, 99999, x, y, false, world);
    }

    public void draw(Graphics g){
        world.collisionDetection(this);
        g.drawImage(imgMap.get(name), x - 20, y - 10, 60, 60, null);
    }
    public void collisionResponse(GameObject object){
        if(object instanceof Hero) {
            Random rand = new Random();
            int n = Math.abs(rand.nextInt()) % 100;
            if(n < 45)
            	fireballBox((Hero) object);
            else if(n < 90)
                    bloodBox((Hero) object);
        //    System.out.println(n);
            world.pickUpBox(this);
            setDelay(DELAYTIME);
        }
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void onAttack(Weapon weapon){
    }

    public void fireballBox(Hero hero) {
        for (Weapon weapon : hero.getWeapons())
            if (weapon instanceof Fireball) {
                ((Fireball) weapon).setNum(((Fireball) weapon).getMaxNum());
                break;
            }
    }

    public void bloodBox(Hero hero) {
        hero.setHP(Hero.MAX_HP);
    }

    public boolean collisionDetection(GameObject object){
        if(!(object instanceof Hero)) return false;
        double deltaX = this.x - object.getX();
        double deltaY = (this.y - object.getY());
        double d = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        int R = this.getRadius() + object.getRadius();
        if(d <= R) return true;
        return false;
    }
}
