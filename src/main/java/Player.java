public class Player extends GameObject {
    public Player(Location location, String image) {
        super(location, image);
    }

    public void reactToCollision() {
        System.out.println("Enemy collision! Reacting accordingly.");
        // Example response: move to a new position, change direction, or alter behavior.
    }
}
