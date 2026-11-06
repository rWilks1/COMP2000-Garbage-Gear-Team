import java.awt.Color;

/**
 * Prey seek food, gain energy and reproduce when healthy.
 */
public class Prey extends Animal {
    private static final double STARTING_ENERGY = 65;
    private static final double FOOD_ENERGY = 35;
    private static final double STARVATION_RATE = 0.12;

    public Prey(double x, double y) {
        super(x, y, World.scale*2, new Color(70, 210, 100), STARTING_ENERGY, 1);
    }

    @Override
    public void update(World world) {
        loseEnergy(STARVATION_RATE);
        tickCooldown();

        Food target = world.findNearestFood(x, y);
        if (target != null) {
            moveTowards(target.getX(), target.getY(), world);

            if (world.distanceBetween(this, target) < radius + target.getRadius() + 3) {
                world.removeFood(target);
                gainEnergy(FOOD_ENERGY);
            }
        } else {
            wander(world, world.getRandom());
        }

        if (energy >= 100 && reproductionCooldown == 0) {
            world.addPrey(new Prey(
                x + world.getRandom().nextInt(31) - 15,
                y + world.getRandom().nextInt(31) - 15
            ));
            energy -= 35;
            reproductionCooldown = 100;
        }

        if (isDead()) {
            world.removePrey(this, true);
        }
    }

    @Override
    public boolean isDead() {
        return energy <= 0;
    }
}
