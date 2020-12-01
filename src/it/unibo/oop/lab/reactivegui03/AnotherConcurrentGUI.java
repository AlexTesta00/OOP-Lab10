package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class AnotherConcurrentGUI extends JFrame {

    /* My Components */
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel label = new JLabel("...");
    private final JPanel flowPanel = new JPanel(new FlowLayout());
    private final JButton up = new JButton("Up");
    private final JButton down = new JButton("Down");
    private final JButton stop = new JButton("Stop");
    private final Agent agent = new Agent();
    private final StopAgent stopper = new StopAgent();

    public AnotherConcurrentGUI() {
        /* Setto le dimensioni */
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));

        /* Panel */
        flowPanel.add(this.label);
        flowPanel.add(this.up);
        flowPanel.add(this.down);
        flowPanel.add(this.stop);

        /* Agent */

        /* Listener */
        /* Up */
        up.addActionListener(e -> {
            agent.setUp();
        });
        /* Down */
        down.addActionListener(e -> {
            agent.setDown();
        });
        /* Stop */
        this.stop.addActionListener(e -> {
        });

        /* Thread */
        new Thread(agent).start();
        new Thread(stopper).start();

        /* Imposto il frame */
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(this.flowPanel);
        this.setVisible(true);
    }
    /*Stop function*/
    private void stopGUI() {
        agent.stopCounting();
        up.setEnabled(false);
        down.setEnabled(false);
        stop.setEnabled(false);
    }

    /* Class Agent Counter */
    private class Agent implements Runnable {

        private volatile boolean stop;
        private int counter;
        private volatile boolean upDown = true;

        public void run() {
            while (!this.stop) {
                try {
                    SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.label.setText(Integer.toString(Agent.this.counter)));
                    if (upDown) {
                        this.counter++;
                    } else {
                        this.counter--;
                    }
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        }

        public void stopCounting() {
            this.stop = true;
        }

        public void setUp() {
            this.upDown = true;
        }

        public void setDown() {
            this.upDown = false;
        }
    }

    /*Stop Agent*/
    private class StopAgent implements Runnable {
        private static final int STOP_TIME = 10_000;

        public void run() {
            try {
                Thread.sleep(STOP_TIME);
                stopGUI();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
