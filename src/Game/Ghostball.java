package Game;

public class Ghostball extends Ball {
    public Ghostball(Role role, World world) {
        super("Ghostball", 7, 8, 80, 30, role, world);
        this.setNum(10000);
        this.picOffset = 8;
    }
}
