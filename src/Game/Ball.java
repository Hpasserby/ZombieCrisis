package Game;

import java.awt.*;

public class Ball extends Weapon implements Cloneable{
    private final int attackRange = 40;
    private boolean ultimateState;
    private int num;
    protected int picOffset;
    private int[] imgOrder = {4,7,5,6,1,2,3,0};

    public Ball(String name, int radius, int speed, int damage, int coldDownTime, Role role, World world){
        super(name, radius, speed, damage, coldDownTime, role, 300, 360,true, world);
    }

    public void initFireball(Direction dir){
        if(this.num <= 0) return;
        this.setDir(dir);
        double cosA = (Math.cos(Math.toRadians(Direction.toDegree(dir))));
        double sinA = -(Math.sin(Math.toRadians(Direction.toDegree(dir))));
        this.x = (int)(this.host.getX() + this.host.getRadius() * cosA);
        this.y = (int)(this.host.getY() + this.host.getRadius() * sinA);
        this.xIncrement = (int)(this.speed * cosA);
        this.yIncrement = (int)(this.speed * sinA);
        world.addObject((Ball) this.clone());
        this.num--;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void Attack(){
    }

    @Override
    public boolean collisionDetection(GameObject object) {
        return ((!object.equals(this.host)) && !(object instanceof Weapon) && super.collisionDetection(object));
    }

    public void draw(Graphics g){
        int picY = imgOrder[dir == Direction.STOP ? oldDir.ordinal() : dir.ordinal()];
        int picX = maintainState(3);
        this.x += xIncrement;
        this.y += yIncrement;
        drawOneImage(g, this.name, getPicOffset(), this.x, this.y, picX, picY);
        world.collisionDetection(this);
    }

    public void setState(){
        initFireball(this.host.getDir() == Direction.STOP ? host.getOldDir() : host.getDir());
        super.setState();
    }

    public void setUltimateState(){
        if(getNum() < 8) return;
        for(Direction dir : Direction.values()){
            if(dir == Direction.STOP) continue;
            initFireball(dir);
        }
        super.setState();
    }

    public int maintainState(int n){
        this.state++;
        if(state >= n) {
            state = 0;
        }
        return state;
    }

    public void collisionResponse(GameObject object){
        world.removeObject(this);
        resetState();
        object.onAttack(this);
    }

    public int getPicOffset(){
        return picOffset;
    }

    @Override
    public String toString(){
        String strBuf = name;
        strBuf += ":";
        strBuf += String.valueOf(getNum());
        return strBuf;
    }
}
