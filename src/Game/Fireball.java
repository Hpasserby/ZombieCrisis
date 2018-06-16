package Game;

public class Fireball extends Ball {
    int maxNum = 80;
    public Fireball(Role role, World world) {
        super("Fireball", 15, 15, 55, 10, role, world);
        setNum(maxNum);
        this.picOffset = 16;
    }

    public int getMaxNum() {
        return maxNum;
    }
}


