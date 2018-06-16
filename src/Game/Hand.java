package Game;

import java.util.Iterator;

public class Hand extends Weapon{
    private int attackRange;

    public Hand(Role role, World world){
        super("Hand", 0, 15, 60,12, role, 55, 70, false, world);
    }

    public void Attack(){
        super.Attack();
    }

    public int getX(){
        return this.host.getX();
    }

    public int getY(){
        return this.host.getY();
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }
}
