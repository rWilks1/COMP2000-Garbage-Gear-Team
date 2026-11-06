import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Owns the simulation entities and advances the ecosystem.
 */
public class World {
    private final int width;
    private final int height;
    private final Random random = new Random();
    public static final int scale = 1;

    private final List<Prey> prey = new ArrayList<>();
    private final List<Predator> predators = new ArrayList<>();
    private final List<Food> food = new ArrayList<>();
    private final List<Meat> meat = new ArrayList<>();
    private final List<WorldObserver> observers = new ArrayList<>();

    private int ticks;

    public World(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                "World dimensions must be greater than zero."
            );
        }
        
        this.width = width;
        this.height = height;
        reset();
    }

    public void reset() {
        prey.clear();
        predators.clear();
        food.clear();
        meat.clear();
        ticks = 0;

        for (int i = 0; i < 45; i++) {
            addPrey(randomX(), randomY());
        }
        for (int i = 0; i < 2; i++) {
            addPredator(randomX(), randomY());
        }
        for (int i = 0; i < 85; i++) {
            addFood(randomX(), randomY());
        }
        notifyObservers();
    }

    public void update() {
        ticks++;

        // Iterate over copies because entities may be added or removed during updates.
        for (Prey animal : new ArrayList<>(prey)) {
            animal.update(this);
        }
        for (Predator animal : new ArrayList<>(predators)) {
            animal.update(this);
        }
        for (Meat resource : new ArrayList<>(meat)) {
            resource.update(this);
        }

        // New food appears periodically, up to a population limit.
        if (ticks % 2 == 0 && food.size() < 1000 / scale) {
            for (int i = 0; i < 4; i++) {
                addFood(randomX(), randomY());
            }
        }
        notifyObservers();
    }

    public Prey findNearestPrey(double x, double y) {
        return prey.stream()
            .min(Comparator.comparingDouble(
                p -> distance(x, y, p.getX(), p.getY())
            ))
            .orElse(null);
    }

    public Food findNearestFood(double x, double y) {
        return food.stream()
            .min(Comparator.comparingDouble(
                f -> distance(x, y, f.getX(), f.getY())
            ))
            .orElse(null);
    }

    public Meat findNearestMeat(double x, double y) {
        return meat.stream()
            .min(Comparator.comparingDouble(
                m -> distance(x, y, m.getX(), m.getY())
            ))
            .orElse(null);
    }

    private void notifyObservers() {
        for (WorldObserver observer : new ArrayList<>(observers)) {
            observer.worldUpdated(this);
        }
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void addPrey(Prey animal) {
        prey.add(animal);
    }

    public void addPrey(double x, double y) {
        prey.add(new Prey(clampX(x), clampY(y)));
    }

    public void addPredator(double x, double y) {
        predators.add(new Predator(clampX(x), clampY(y)));
    }

    public void addFood(double x, double y) {
        food.add(new Food(clampX(x), clampY(y)));
    }

    public void addObserver(WorldObserver observer) {
        observers.add(observer);
    }

    public void removeFood(Food item) {
        food.remove(item);
    }

    public void removeMeat(Meat item) {
        meat.remove(item);
    }

    public void removePrey(Prey animal, boolean createMeat) {
        if (prey.remove(animal) && createMeat) {
            meat.add(new Meat(animal.getX(), animal.getY()));
        } else if (!createMeat) {
            meat.add(new Meat(animal.getX(), animal.getY()));
        }
    }

    public void removePredator(Predator animal, boolean createMeat) {
        if (predators.remove(animal) && createMeat) {
            meat.add(new Meat(animal.getX(), animal.getY()));
        }
    }

    private double randomX() {
        return 10 + random.nextInt(Math.max(1, width - 20));
    }

    private double randomY() {
        return 10 + random.nextInt(Math.max(1, height - 20));
    }

    private double clampX(double x) {
        return Math.max(8, Math.min(width - 8, x));
    }

    private double clampY(double y) {
        return Math.max(8, Math.min(height - 8, y));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Random getRandom() {
        return random;
    }

    public List<Prey> getPrey() {
        return Collections.unmodifiableList(prey);
    }

    public List<Predator> getPredators() {
        return Collections.unmodifiableList(predators);
    }

    public List<Food> getFood() {
        return Collections.unmodifiableList(food);
    }

    public List<Meat> getMeat() {
        return Collections.unmodifiableList(meat);
    }

    public String getStatistics() {
        return "Prey: " + prey.size()
            + "   Predators: " + predators.size()
            + "   Food: " + food.size()
            + "   Meat: " + meat.size();
    }

    public double shortestDeltaX(double from, double to) {
        double dx = to - from;

        if (dx > width / 2.0) {
            dx -= width;
        } else if (dx < -width / 2.0) {
            dx += width;
        }

        return dx;
    }

    public double shortestDeltaY(double from, double to) {
        double dy = to - from;

        if (dy > height / 2.0) {
            dy -= height;
        } else if (dy < -height / 2.0) {
            dy += height;
        }

        return dy;
    }

    public double distanceBetween(Entity a, Entity b) {
        double dx = Math.abs(a.getX() - b.getX());
        double dy = Math.abs(a.getY() - b.getY());

        dx = Math.min(dx, width - dx);
        dy = Math.min(dy, height - dy);

        return Math.sqrt(dx * dx + dy * dy);
    }


}
