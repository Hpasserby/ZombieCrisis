package Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Hero extends Role {
    public static final int MAX_HP = 1200;
    private int[] keys;
    private boolean bL=false, bU=false, bR=false, bD=false; //閿洏鏂瑰悜閿�

    public Hero(int x, int y, int keyGroup, World world) {
        super("Hero", MAX_HP,14, 5, x, y, world);
        addWeapon(new Sword(this, world));
        addWeapon(new Fireball(this, world));
        setCurrentWeapon(getWeapons().get(0));
        keys = new int[7];
        if(keyGroup == 0){
            keys[0] = KeyEvent.VK_LEFT;
            keys[1] = KeyEvent.VK_UP;
            keys[2] = KeyEvent.VK_RIGHT;
            keys[3] = KeyEvent.VK_DOWN;
            keys[4] = KeyEvent.VK_NUMPAD1;
            keys[5] = KeyEvent.VK_NUMPAD2;
            keys[6] = KeyEvent.VK_NUMPAD3;
        } else {
            keys[0] = KeyEvent.VK_A;
            keys[1] = KeyEvent.VK_W;
            keys[2] = KeyEvent.VK_D;
            keys[3] = KeyEvent.VK_S;
            keys[4] = KeyEvent.VK_J;
            keys[5] = KeyEvent.VK_K;
            keys[6] = KeyEvent.VK_L;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == keys[0]) bL = false;
        else if(key == keys[1]) bU = false;
        else if(key == keys[2]) bR = false;
        else if(key == keys[3]) bD = false;
        else if(key == keys[4]);
        else if(key == keys[5]);
        else if(key == keys[6]);
        locateDirection();
    }

    public void KeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == keys[0]) bL = true;
        else if(key == keys[1]) bU = true;
        else if(key == keys[2]) bR = true;
        else if(key == keys[3]) bD = true;
        else if(key == keys[4] && this.getCurrentWeapon().getColdDown() == 0) {
            this.getCurrentWeapon().setState();
            this.getCurrentWeapon().setColdDown();
        } else if(key == keys[5]){
            this.NextWeapon();
        } else if(key == keys[6] && this.getCurrentWeapon().getColdDown() == 0){
            Weapon weapon = this.getCurrentWeapon();
            if(weapon instanceof Fireball) {
                ((Fireball) this.getCurrentWeapon()).setUltimateState();
                this.getCurrentWeapon().setColdDown();
            }
        }
        locateDirection();
    }


    public void locateDirection() {
        if(bL && !bU && !bR && !bD) dir = Direction.L;
        else if(bL && bU && !bR && !bD) dir = Direction.LU;
        else if(!bL && bU && !bR && !bD) dir = Direction.U;
        else if(!bL && bU && bR && !bD) dir = Direction.RU;
        else if(!bL && !bU && bR && !bD) dir = Direction.R;
        else if(!bL && !bU && bR && bD) dir = Direction.RD;
        else if(!bL && !bU && !bR && bD) dir = Direction.D;
        else if(bL && !bU && !bR && bD) dir = Direction.LD;
        else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }

    public void draw(Graphics g){
        g.drawString(getCurrentWeapon().toString(), this.x - 20, this.y - 45);
        //g.drawString("HP: " + String.valueOf(this.HP),this.x-8,this.y-28);
        drawBloodBar(g);
        int b = this.getBegin();
        if(b > 0 && (b / 3) % 2 == 0) {
        	this.getCurrentWeapon().maintainColdDown();
        	mainTainWalkState(16);
            move();
        } else {
        	super.draw(g);
        }
    }
    
    public void setDeadState(){
    	this.setxIncrement(0, 0);
    	this.setyIncrement(0, 0);
    	this.onAttackState = 0;
        this.deadState = 600;
    }
    
    public void resetBegin() {
        this.x = (new Random().nextInt(100) % 2 == 0) ? 340 : 620;
        this.y = 280;
        this.setHP(MAX_HP);
        this.deadState = -1;
        super.resetBegin();
    }
    
    public void maintainDeadState() {
    	world.searchHero();
    	if(deadState <= 0) {
        	if(world.searchHero()) {
        		this.resetBegin();
        	} else {
        		world.objDead(this);
        	}
    	} else {
    		this.deadState--;
    	}
    }
}
