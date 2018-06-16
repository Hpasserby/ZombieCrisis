package Game;

import javax.naming.event.ObjectChangeListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class GameObject implements Cloneable{
    protected String name;
    protected int radius; //半径
    protected int speed;
    protected int xIncrement;
    protected int yIncrement;
    protected Direction dir;
    protected Direction oldDir;
    protected int x, y;
    protected int HP;
    protected int onAttackState;
    protected boolean collidable;
    protected World world;
    protected static Toolkit tk = Toolkit.getDefaultToolkit();
    protected static Image[] imgs = null;
    protected static Map<String, Image> imgMap = new HashMap<String, Image>();

    public abstract void draw(Graphics g);
    public abstract void collisionResponse(GameObject object);
    public abstract void onAttack(Weapon weapon);

    static {
        imgs = new Image[] {
                tk.getImage(GameObject.class.getClassLoader().getResource("images/hero.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/monster.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/fireball.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/ghost.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/ghostball.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/wall.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/blood.png")),
                tk.getImage(GameObject.class.getClassLoader().getResource("images/box.png"))
        };
        imgMap.put("Hero", imgs[0]);
        imgMap.put("Monster", imgs[1]);
        imgMap.put("Fireball", imgs[2]);
        imgMap.put("Ghost", imgs[3]);
        imgMap.put("Ghostball", imgs[4]);
        imgMap.put("Wall", imgs[5]);
        imgMap.put("Blood", imgs[6]);
        imgMap.put("Box", imgs[7]);
    }

    public GameObject(String name, int radius, int speed, int HP, int x, int y, boolean collidable, World world) {
        this.name = name;
        this.radius = radius;
        this.speed = speed;
        this.xIncrement = 0;
        this.yIncrement = 0;
        this.onAttackState = 0;
        this.HP = HP;
        this.dir = Direction.STOP;
        this.oldDir = Direction.D;
        this.x = x;
        this.y = y;
        this.collidable = collidable;
        this.world = world;
    }

    public GameObject(String name, int radius, int speed, Direction dir, int HP, int x, int y, boolean collidable, World world) {
        this.name = name;
        this.radius = radius;
        this.speed = speed;
        this.xIncrement = 0;
        this.yIncrement = 0;
        this.onAttackState = 0;
        this.dir = dir;
        this.HP = HP;
        this.oldDir = Direction.D;
        this.x = x;
        this.y = y;
        this.collidable = collidable;
        this.world = world;
    }

    public void drawOneImage(Graphics g, String name, int picOffset,int x, int y, int picX, int picY){
        g.drawImage(
                imgMap.get(name),
                x - picOffset - 4,
                y - picOffset - 4,
                x + picOffset + 4,
                y + picOffset + 4,
                picX * picOffset * 2,
                picY * picOffset * 2,
                picX * picOffset * 2 + picOffset * 2 - 1,
                picY * picOffset * 2 + picOffset * 2 - 1,
                null);

    }

    public boolean collisionDetection(GameObject object){
        if(!object.isCollidable()) return false;
        if(this.getDir() == Direction.STOP  && this.checkOnAttack() <= 0) return false;
        double deltaX = this.x - object.getX();
        double deltaY = (this.y - object.getY());
        double d = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        int R = this.getRadius() + object.getRadius();
        if(d <= R){
            double cosValue = deltaX / d;
            double sinValue = deltaY / d;
            int offsetY = (int)((R - d + 2) * sinValue);
            int offsetX = (int)((R - d + 2) * cosValue);
            this.x += offsetX;
            this.y += offsetY;
            return true;
        }
        return false;
    }

    public int checkOnAttack(){
        return (this.onAttackState);
    }

    public void resetOnAttackState(){
        this.onAttackState = 0;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getRadius() {
        return this.radius;
    }

    public Direction getDir() {
        return this.dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public Direction getOldDir() {
        return this.oldDir;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHP() {
        return this.HP;
    }

    public void setHP(int HP) {
    	if(HP == 0 && !(this instanceof Hero))
    		this.collidable = false;
        this.HP = HP;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public int getxIncrement() {
        return xIncrement;
    }

    public void setxIncrement(double degree, int speed) {
        this.xIncrement = (int)(getSpeed() * Math.cos(Math.toRadians(degree)));
    }

    public int getyIncrement() {
        return yIncrement;
    }

    public void setyIncrement(double degree, int speed) {
        this.yIncrement = -(int)(getSpeed() * Math.sin(Math.toRadians(degree)));
    }

    public double getDistance(int x1, int y1, int x2, int y2){
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    @Override
    public Object clone() {
        GameObject obj = null;
        try{
            obj = (GameObject) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
