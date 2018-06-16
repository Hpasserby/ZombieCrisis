package Game;

import java.util.Iterator;

public class Sword extends Weapon{

    public Sword(Role role, World world){
        super("Sword", 0, 15, 80, 12, role, 80, 42, false, world);
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
