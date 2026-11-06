import java.awt.Color;
import java.util.Random;

/**
 * Shared state and movement behaviour for prey and predators.
 */
public abstract class Animal extends Entity {
    protected double energy;
    protected double movementSpeed;
    protected int reproductionCooldown;

    protected Animal(double x, double y, int radius, Color color,
                     double startingEnergy, double movementSpeed) {
        super(x, y, radius, color);
        this.energy = startingEnergy;
        this.movementSpeed = movementSpeed;
        this.reproductionCooldown = 0;
    }

    public double getEnergy() {
        return energy;
    }

    protected void loseEnergy(double amount) {
        energy -= amount;
    }

    protected void gainEnergy(double amount) {
        energy += amount;
    }

    protected void moveTowards(double targetX, double targetY, World world) {
        double dx = world.shortestDeltaX(x, targetX);
        double dy = world.shortestDeltaY(y, targetY);

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0.1) {
            x += (dx / distance) * movementSpeed;
            y += (dy / distance) * movementSpeed;
        }

        wrapInsideWorld(world);
    }

    protected void wander(World world, Random random) {
        x += (random.nextDouble() - 0.5) * movementSpeed * 2;
        y += (random.nextDouble() - 0.5) * movementSpeed * 2;
        wrapInsideWorld(world);
    }

    private void wrapInsideWorld(World world) {
        x = ((x % world.getWidth()) + world.getWidth())
            % world.getWidth();

        y = ((y % world.getHeight()) + world.getHeight())
            % world.getHeight();
    }

    protected void tickCooldown() {
        if (reproductionCooldown > 0) {
            reproductionCooldown--;
        }
    }

    public abstract boolean isDead();
}
