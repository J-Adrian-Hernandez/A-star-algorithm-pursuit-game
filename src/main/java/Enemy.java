public class Enemy extends GameObject {
    public Enemy(Location location, String image) {
        super(location, image);
    }

    public void reactToCollision() {
        System.out.println("Enemy collision! Reacting accordingly.");
    }
}
