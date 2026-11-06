import javax.swing.*;
import java.awt.*;

/**
 * Draws the current state of the world. It does not control simulation rules.
 */
public class SimulationPanel extends JPanel implements WorldObserver {
    private final World world;

    public SimulationPanel(World world) {
        this.world = world;
        setPreferredSize(new Dimension(world.getWidth(), world.getHeight()));
        setBackground(new Color(32, 48, 38));
    }

    @Override
    public void worldUpdated(World world) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        double scaleX = (double) getWidth() / world.getWidth();
        double scaleY = (double) getHeight() / world.getHeight();

        // Preserve the original world proportions.
        double scale = Math.min(scaleX, scaleY);

        // Centre the world inside the panel.
        double offsetX = (getWidth() - world.getWidth() * scale) / 2;
        double offsetY = (getHeight() - world.getHeight() * scale) / 2;

        g.translate(offsetX, offsetY);
        g.scale(scale, scale);

        // Draw resources first so animals appear on top of them.
        drawEntities(g, world.getFood());
        drawEntities(g, world.getMeat());
        drawEntities(g, world.getPrey());
        drawEntities(g, world.getPredators());

        g.setColor(Color.WHITE);
        g.drawString("Green: prey   Red: predators   Yellow: food   Brown: meat", 12, 20);
        g.dispose();
    }

    private void drawEntities(Graphics2D g, Iterable<? extends Entity> entities) {
        for (Entity entity : entities) {
            g.setColor(entity.getColor());
            int diameter = entity.getRadius() * 2;
            int drawX = (int) Math.round(entity.getX()) - entity.getRadius();
            int drawY = (int) Math.round(entity.getY()) - entity.getRadius();

            if (entity instanceof Food || entity instanceof Meat) {
                g.fillRect(drawX, drawY, diameter, diameter);
            } else {
                g.fillOval(drawX, drawY, diameter, diameter);
            }
        }
    }
}
