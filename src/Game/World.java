package Game;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {
    private CopyOnWriteArrayList<GameObject> objects;
    private CopyOnWriteArrayList<Blood> bloods;
    private List<Box> pickedBoxes;
    private int maxBloodNum = 5000;
    private int bloodNum;
    private int width;
    private int height;
    private int maxEnemyNum;
    private int currentEnemyNum;
    private int producedEnemyNum;
    private int produceDelay;
    private int boxDelay;
    private Image endImg;
    private int end;


    public World(int width, int height, boolean Doubleplayer) {
        this.width = width;
        this.height = height;
        this.objects = new CopyOnWriteArrayList<>();
        this.bloods = new CopyOnWriteArrayList<>();
        this.pickedBoxes = new ArrayList<>();
        this.bloodNum = 0;
        this.maxEnemyNum = 3;
        this.currentEnemyNum = 0;
        this.producedEnemyNum = 0;
        this.boxDelay = 0;
        this.end = -1;
        this.endImg = Toolkit.getDefaultToolkit().getImage(World.class.getClassLoader().getResource("images/gameover.png"));
        objects.add(new Hero(340, 180, 1, this));
        if(Doubleplayer)
        	objects.add(new Hero(620, 180, 0, this));
        objects.add(new Border(0, this));
        objects.add(new Border(1, this));
        objects.add(new Border(2, this));
        objects.add(new Border(3, this));
        objects.add(new Box(320, 360, this));
        objects.add(new Box(640, 360, this));
        for(int i = 1; i <= 2; i++)
            for(int j = 1; j <= 3; j++){
                objects.add(new Wall(width / 4 * j, height / 3 * i, this));
        }
    }

    public Iterator<GameObject> getObjectsIterator(){
        return objects.iterator();
    }

    public void removeObject(GameObject obj){
        objects.remove(obj);
    }

    public void addObject(GameObject obj){
        objects.add(obj);
    }

    public GameObject getObject(int index){
        return objects.get(index);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCurrentEnemyNum() {
        return currentEnemyNum;
    }

    public void setCurrentEnemyNum(int currentEnemyNum) {
        this.currentEnemyNum = currentEnemyNum;
    }

    public void setProduceDelay(){
        this.produceDelay = 50;
    }

    public void pickUpBox(Box box){
        this.removeObject(box);
        pickedBoxes.add(box);
        box.setDelay(Box.DELAYTIME);
    }

    public void produceBox(){
        for(Box box : pickedBoxes){
            if(box.getDelay() == 0){
                this.addObject(box);
                pickedBoxes.remove(box);
                break;
            } else {
                box.setDelay(box.getDelay() - 1);
            }
        }
    }

    public void produceEnemy(){
    	produceDelay = (produceDelay - 1 > 0? produceDelay - 1 : 0);
    	if(currentEnemyNum >= 100) return;
        if(producedEnemyNum < maxEnemyNum && produceDelay <= 0){
            Random rand = new Random();
            int pos = Math.abs(rand.nextInt()) % 4;
            int type = Math.abs(rand.nextInt()) % 100;
            int t = (rand.nextInt() % 2) * Role.PICOFFSET * 2;
            int off = Role.PICOFFSET + 10;
            switch (pos){
                case 0:
                    objects.add(type < 10 ? new Ghost(width / 2 + t, off, this) : new Monster(width / 2 + t, off, this));
                    break;
                case 1:
                    objects.add(type < 10 ? new Ghost(off, height / 2 + t, this) : new Monster(off, height / 2 + t, this));
                    break;
                case 2:
                    objects.add(type < 10 ? new Ghost(width / 2 + t, height - off, this) : new Monster(width / 2 + t, height - off, this));
                    break;
                case 3:
                    objects.add(type < 10 ? new Ghost(width - off, height / 2 + t, this) : new Monster(width -off, height / 2 + t, this));
                    break;
                default:
                    break;
            }
            currentEnemyNum++;
            producedEnemyNum++;
            setProduceDelay();;
        } else if(currentEnemyNum <= 0 && producedEnemyNum == maxEnemyNum) {
            maxEnemyNum += 3;
            producedEnemyNum = 0;
        }
    }

    public boolean collisionDetection(GameObject obj){
        Iterator<GameObject> iter = this.getObjectsIterator();
        int flag = 0;
        while(iter.hasNext()){
            GameObject tmpObj = iter.next();
            if(!obj.equals(tmpObj) && tmpObj.getHP() > 0){
                if(obj.collisionDetection(tmpObj)){
                    obj.collisionResponse(tmpObj);
                    flag = 1;
                }
            }
        }
        if(flag == 1) return true;
        else return false;
    }

    public void objectSort(){
        Collections.sort(objects, new Comparator<GameObject>() {
            @Override
            public int compare(GameObject obj1, GameObject obj2) {
                int i = obj1.getY() - obj2.getY();
                if(i == 0){
                    return obj1.getX() - obj2.getX();
                }
                return i;
            }
        });
    }

    public void drawWorld(Graphics g){
    	if(isEnd()) { 
    		end--;
    	}
        produceEnemy();
        produceBox();
        for(Blood blood : bloods){
            blood.draw(g);
        }
        this.objectSort();
        Iterator<GameObject> iter =this.getObjectsIterator();
        while(iter.hasNext()){
            iter.next().draw(g);
        }
    }

    public void objDead(Object obj){
        if(obj instanceof Enemy) currentEnemyNum--;
        this.objects.remove(obj);
    }

    public int addBloodNum(){
        bloodNum = (bloodNum + 1) % maxBloodNum;
        return bloodNum;
    }

    public boolean searchHero() {
    	Iterator<GameObject> iter = getObjectsIterator();
    	while(iter.hasNext()) {
    		GameObject obj = iter.next();
    		if(obj instanceof Hero && obj.getHP() > 0) return true;
    	}
    	gameOver();
    	return false;
    }
    
    public void addBlood(int x, int y){
        int n = addBloodNum();
        if(bloods.size() < maxBloodNum){
            bloods.add(new Blood(x, y, this));
        } else {
            bloods.set(n, new Blood(x, y, this));
        }
    }
    
    public void drawEnd(Graphics g) {
    	g.drawImage(endImg, 0, 0, width, height, null);
    }
    
    public void gameOver() {
    	if(!isEnd())
    		this.end = 30;
    }
    
    public boolean isEnd() {
    	return this.end >= 0;
    }
    
    public boolean End() {
    	return this.end == 0;
    }
}
