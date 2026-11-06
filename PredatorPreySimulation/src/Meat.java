import java.awt.Color;

/**
 * Meat is created when an animal dies and can be consumed by predators.
 */
public class Meat extends Entity {
    private int remainingLife = 900;

    public Meat(double x, double y) {
        super(x, y, World.scale, new Color(155, 95, 65));
    }

    @Override
    public void update(World world) {
        remainingLife--;
        if (remainingLife <= 0) {
            world.removeMeat(this);
        }
    }
}
