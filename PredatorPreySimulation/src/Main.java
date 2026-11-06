import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        World world;
        try {
            world = new World(900, 600);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                "Simulation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        SimulationPanel simulationPanel = new SimulationPanel(world);
        world.addObserver(simulationPanel);

        JFrame frame = new JFrame("Predator-Prey Ecosystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(simulationPanel, BorderLayout.CENTER);

        JButton pauseButton = new JButton("Pause");
        JButton resetButton = new JButton("Reset");
        JLabel statsLabel = new JLabel();
        JSlider speedSlider = new JSlider(10, 150, 50);
        speedSlider.setToolTipText("Simulation update delay in milliseconds");

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controls.add(pauseButton);
        controls.add(resetButton);
        controls.add(new JLabel("Speed:"));
        controls.add(speedSlider);
        controls.add(statsLabel);
        frame.add(controls, BorderLayout.SOUTH);

        Timer timer = new Timer(50, event -> {
            world.update();
            simulationPanel.repaint();
            statsLabel.setText(world.getStatistics());
        });

        pauseButton.addActionListener(event -> {
            if (timer.isRunning()) {
                timer.stop();
                pauseButton.setText("Start");
            } else {
                timer.start();
                pauseButton.setText("Pause");
            }
        });

        resetButton.addActionListener(event -> {
            world.reset();
            simulationPanel.repaint();
            statsLabel.setText(world.getStatistics());
        });

        speedSlider.addChangeListener(event -> timer.setDelay(speedSlider.getValue()));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        timer.start();
    }
}
