import java.awt.Color;

/**
 * Base type for anything that occupies a position in the simulation.
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected int radius;
    protected Color color;

    protected Entity(double x, double y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public double distanceTo(Entity other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public abstract void update(World world);
}
