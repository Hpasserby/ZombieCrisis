package Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

class Grid implements Cloneable{
    public static final int LENGTH = 10;
    private int gridX, gridY;
    private int x, y;
    private boolean accessible;
    private GameObject object;
    private boolean isBorder;

    public Grid(int gridX, int gridY, boolean isBorder) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.x = gridX * LENGTH;
        this.y = gridY * LENGTH;
        this.isBorder = isBorder;
        this.accessible = !isBorder;
        this.object = null;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public GameObject getObject() {
        return object;
    }

    public void setObject(GameObject object) {
        this.object = object;
    }

    @Override
    public Object clone() {
        Grid tmp = null;
        try{
            tmp = (Grid) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return tmp;
    }
}

public class WorldGrids {
    private List<Grid> grids;
    private World world;
    private List<Grid> unaccessibleGrids;
    private int w, h;

    public WorldGrids(World world) {
        this.world = world;
        w = world.getWidth() / Grid.LENGTH;
        h = world.getHeight() / Grid.LENGTH;
        this.grids = new ArrayList<>();
        this.unaccessibleGrids = new ArrayList<>();
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                if(j == 0 || j == 1 || i <= 5)
                    grids.add(new Grid(j, i, true));
                else
                    grids.add(new Grid(j, i, false));
            }
        }
    }

    public void updateGrids(){
        resetGrid();;

        Iterator<GameObject> objIter = world.getObjectsIterator();
        while(objIter.hasNext()){
            GameObject object = objIter.next();
            if(!object.isCollidable() || object instanceof Border) continue;
            Iterator<Grid> tmpIt = getGrid(object).iterator();
            while(tmpIt.hasNext()){
                Grid tmp = tmpIt.next();
                tmp.setObject(object);
                tmp.setAccessible(false);
                unaccessibleGrids.add(tmp);
            }
        }
    }

    public void resetGrid(){
        Iterator<Grid> unAcGridIter = getUnaccessibleGridsIterator();
        while(unAcGridIter.hasNext()){
            Grid tmp = unAcGridIter.next();
            tmp.setAccessible(true);
            tmp.setObject(null);
            unAcGridIter.remove();
        }
    }

    public List<Grid> getGrid(GameObject obj){
        List<Grid> grids = new ArrayList<>();
        int objX = obj.getX() - obj.getRadius();
        int objY = obj.getY() - obj.getRadius();
        int length = obj.getRadius() * 2;
        int t = length / Grid.LENGTH + 1;
        for(int x = objX, i = 0; i < t && i < w; i++, x += Grid.LENGTH)
            for(int y = objY, j = 0; j < t && j < h; j++, y += Grid.LENGTH) {
                Grid tmp = getGrid(x+Grid.LENGTH, y+Grid.LENGTH);
                if(grids.indexOf(tmp) < 0)
                    grids.add(tmp);
            }
        return grids;
    }

    public Grid getGrid(int x, int y){
        int gridX = x / Grid.LENGTH;
        int gridY = y / Grid.LENGTH;
        return this.grids.get(gridY * w + gridX);
    }

    public Grid get(int gridX, int gridY){
        if(gridX < 0) gridX = 0;
        if(gridY < 0) gridY = 0;
        if(gridX >= w) gridX = w-1;
        if(gridY >= h) gridY = h-1;
        return this.grids.get(gridY * w + gridX);
    }

    public Iterator<Grid> getUnaccessibleGridsIterator(){
        return unaccessibleGrids.iterator();
    }

    public Iterator<Grid> getGridsIterator(){
        return grids.iterator();
    }
}
