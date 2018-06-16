package Game;

import java.awt.*;
import java.lang.annotation.Target;

public class Monster extends Enemy {

    public Monster(int x, int y, World world){
        super("Monster", 170, 14, 2, x, y, world);
        addWeapon(new Hand(this, world));
        setCurrentWeapon(getWeapons().get(0));
        getCurrentWeapon().setDamage(100);
        getCurrentWeapon().setColdDownTime(24);
        ((Hand)getCurrentWeapon()).setAttackRange(50);
    }

    public void draw(Graphics g) {
        if(getTarget() != null && getTarget().getHP() > 0) {
            int distance = (int) getDistance(this.getX(), this.getY(), getCurrentTarget().getX(), getCurrentTarget().getY());
            if (distance <= ((Hand) getCurrentWeapon()).getAttackRange() && this.getCurrentWeapon().getColdDown() == 0) {
                Direction dir = judgeAccurateDir(getTarget().getX(), getTarget().getY());
                this.dir = dir;
                this.oldDir = (dir == Direction.STOP ? oldDir : dir);
                this.getCurrentWeapon().setState();
                this.getCurrentWeapon().setColdDown();
                getPath();
            }
        }
        locateDirection();
        super.draw(g);
    }


}
