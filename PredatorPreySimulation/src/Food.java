import java.awt.Color;

/**
 * Food is a stationary resource that prey can consume.
 */
public class Food extends Entity {
    public Food(double x, double y) {
        super(x, y, World.scale, new Color(245, 205, 65));
    }

    @Override
    public void update(World world) {
        // Food does not move. World manages its spawning and removal.
    }
}
