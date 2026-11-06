import java.awt.Color;

/**
 * Predators hunt prey and eat the meat left behind.
 */
public class Predator extends Animal {
    private static final double STARTING_ENERGY = 90;
    private static final double MEAT_ENERGY = 55;
    private static final double STARVATION_RATE = 1;

    public Predator(double x, double y) {
        super(x, y, World.scale*2, new Color(225, 75, 70), STARTING_ENERGY, 1.2);
    }

    @Override
    public void update(World world) {
        loseEnergy(STARVATION_RATE);
        tickCooldown();

        Prey targetPrey = world.findNearestPrey(x, y);
        Meat targetMeat = world.findNearestMeat(x, y);

        // Hunt nearby prey. A successful hunt creates a meat resource.
        if (targetPrey != null && world.distanceBetween(this, targetPrey) < 100) {
            moveTowards(targetPrey.getX(), targetPrey.getY(), world);

            if (world.distanceBetween(this, targetPrey) < radius + targetPrey.getRadius() + 2) {
                world.removePrey(targetPrey, false);
                targetMeat = world.findNearestMeat(x, y);
            }
        } else if (targetMeat != null) {
            moveTowards(targetMeat.getX(), targetMeat.getY(), world);
        } else {
            wander(world, world.getRandom());
        }

        if (targetMeat != null && world.distanceBetween(this, targetMeat) < radius + targetMeat.getRadius() + 3) {
            world.removeMeat(targetMeat);
            gainEnergy(MEAT_ENERGY);
        }
        
        if (energy >= 100 && reproductionCooldown == 0) {
            world.addPredator(
                x + world.getRandom().nextInt(31) - 15,
                y + world.getRandom().nextInt(31) - 15
            );
            energy -= 35;
            reproductionCooldown = 100;
        }

        if (isDead()) {
            world.removePredator(this, true);
        }
    }

    @Override
    public boolean isDead() {
        return energy <= 0;
    }
}
