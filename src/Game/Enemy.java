package Game;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Enemy extends Role {
    private Pathfinder pathfinder;
    private Role target;
    private List<Grid> path;
    private int refreshPath;
    private int collisionDelay;

    public Enemy(String name, int HP, int radius, int speed,int x, int y, World world){
        super(name, HP, radius, speed, x, y, world);
        this.pathfinder = new Pathfinder(new WorldGrids(world), this);
        this.target = getTarget();
        this.refreshPath = 0;
        this.collisionDelay = 0;
    }

    public Role getTarget() {
        Iterator<GameObject> tmp = world.getObjectsIterator();
        Role target = null;
        double minDistance = 10000000;
        while(tmp.hasNext()){
            GameObject obj = tmp.next();
            if(obj instanceof Hero && obj.getHP() > 0){
                double distance = Math.pow(this.x - obj.getX(), 2) + Math.pow(this.y - obj.getY(), 2);
                target = minDistance <  distance ? target : (Role) obj;
                minDistance = minDistance < distance ? minDistance : distance;
            }
        }
        return target;
    }

    public Role getCurrentTarget(){
        return this.target;
    }

    public int mainTainRefreshPath(int n){
        return refreshPath = (refreshPath + 1) % n;
    }

    public int maintainCollisionDelay(int n){
        return collisionDelay = (collisionDelay + 1) % n;
    }

    public Grid getCurrentGrid(){
        return pathfinder.getWorldGrids().getGrid(this.x, this.y);
    }

    public Grid getTargetGrid(){
        return pathfinder.getWorldGrids().getGrid(target.getX(), target.getY());
    }

    public void getPath(){
        path = pathfinder.shortestPath(getTargetGrid());
    }

    public Direction judgeDirection(int deltaX, int deltaY){
        if (deltaX == 0 && deltaY < 0) return Direction.U;
        else if (deltaX < 0 && deltaY < 0) return Direction.LU;
        else if (deltaX < 0 && deltaY == 0) return Direction.L;
        else if (deltaX < 0 && deltaY > 0) return Direction.LD;
        else if (deltaX == 0 && deltaY > 0) return Direction.D;
        else if (deltaX > 0 && deltaY > 0) return Direction.RD;
        else if (deltaX > 0 && deltaY == 0) return Direction.R;
        else if (deltaX > 0 && deltaY < 0) return Direction.RU;
        else return Direction.STOP;
    }

    public double getDeltaDegree(int anotherX, int anotherY){
        int deltaX = anotherX - this.getX();
        int deltaY = - anotherY + this.getY();
        double D = getDistance(anotherX, anotherY, this.getX(),this.getY());
        double sinA = deltaY / D;
        double cosA = deltaX / D;
        double angle1 = Math.toDegrees(Math.asin(sinA));
        double angle2 = Math.toDegrees(Math.acos(cosA));
        double angle = angle2;
        if(angle1 < 0) angle = 360 - angle;
        return angle;
    }

    public Direction judgeAccurateDir(int anotherX, int anotherY){
        double angle = getDeltaDegree(anotherX, anotherY);
        if((angle >= 0 && angle <= 22.5) || (angle >= 337.5 && angle <= 360)) return Direction.R;
        else if(angle >= 22.5 && angle < 67.5) return Direction.RU;
        else if(angle >= 67.5 && angle < 112.5) return Direction.U;
        else if(angle >= 112.5 && angle < 157.5) return Direction.LU;
        else if(angle >= 157.5 && angle < 202.5) return Direction.L;
        else if(angle >= 202.5 && angle < 247.5) return Direction.LD;
        else if(angle >= 247.5 && angle < 292.5) return Direction.D;
        else if(angle >= 292.5 && angle < 337.5) return Direction.RD;
        else  return Direction.STOP;
    }

    public Direction getNextDir(Grid nextGrid){
        if(nextGrid != null) {
            int deltaX = nextGrid.getX() - this.getX();
            int deltaY = nextGrid.getY() - this.getY();
            return judgeDirection(deltaX, deltaY);
        }
        return Direction.STOP;
    }

    public void locateDirection() {
        if(getTarget() == null) return;
        if(mainTainRefreshPath(30) == 0) {
            this.target = getTarget();
            this.pathfinder = new Pathfinder(new WorldGrids(world), this);
            getPath();
        }
        if(this.path == null || this.path.size() == 0){
            this.dir = Direction.STOP;
            return;
        }
        if(getCurrentGrid().getGridX() == path.get(0).getGridX() && getCurrentGrid().getGridY() == path.get(0).getGridY())
            path.remove(0);
        if(this.path.size() > 0) {
            Grid nextGrid = path.get(0);
            this.oldDir =(this.dir == Direction.STOP) ? oldDir : dir;
            this.dir = getNextDir(nextGrid);
        }
        if(this.getDir() == Direction.STOP) {
            this.oldDir = (judgeAccurateDir(target.getX(), target.getY()) == Direction.STOP) ? oldDir : judgeAccurateDir(target.getX(), target.getY());
        }
    }

    public void draw(Graphics g) {
        locateDirection();
        super.draw(g);
    }

    public void collisionResponse(GameObject object){
        this.dir = Direction.STOP;

        if(maintainCollisionDelay(3) > 0) return;
        else{
            collisionDelay = 3;
            this.pathfinder = new Pathfinder(new WorldGrids(world), this);
            getPath();
        }
        super.collisionResponse(object);
    }
}
