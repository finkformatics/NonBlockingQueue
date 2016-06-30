package de.lwerner.threads.threadsafequeue.visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class QueueWindow extends JFrame {

    private QueueCanvas canvas;

    private JTextField tfProducerSleepTime;
    private JToggleButton btProducerRun;
    private JButton btProducerStep;
    private JTextField tfConsumerSleepTime;
    private JToggleButton btConsumerRun;
    private JButton btConsumerStep;

    private long producerSleepTime = 1000;
    private long consumerSleepTime = 1000;

    private QueueWindow() {
        super("Feinkörnige Warteschlangen");

        JPanel contentPane = new JPanel(new BorderLayout());

        canvas = new QueueCanvas();
        contentPane.add(canvas, BorderLayout.CENTER);

        JPanel toolbarPane = new JPanel();
        btProducerRun = new JToggleButton("Run Producer");
        btProducerRun.setFocusPainted(false);
        btProducerRun.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                btProducerStep.setEnabled(false);
                btProducerRun.setText("Producer running...");
                canvas.getProducer1().start(producerSleepTime, true);
                canvas.getProducer2().start(producerSleepTime, true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                btProducerStep.setEnabled(true);
                btProducerRun.setText("Run Producer");
                canvas.getProducer1().stop();
                canvas.getProducer2().stop();
            }
        });
        tfProducerSleepTime = new JTextField("1000");
        tfProducerSleepTime.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        long sleepTime = Long.parseLong(tfProducerSleepTime.getText());
                        if (sleepTime > 0) {
                            producerSleepTime = sleepTime;
                        } else {
                            tfProducerSleepTime.setText("" + producerSleepTime);
                        }
                    } catch (Exception ignored) { }
                }
            }
        });
        btProducerStep = new JButton("Producer step");
        btProducerStep.setFocusPainted(false);
        btProducerStep.addActionListener(e -> {
            if (Math.random() < 0.5) {
                canvas.getProducer1().step(true);
            } else {
                canvas.getProducer2().step(true);
            }
        });
        btConsumerRun = new JToggleButton("Run Consumer");
        btConsumerRun.setFocusPainted(false);
        btConsumerRun.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                btConsumerStep.setEnabled(false);
                btConsumerRun.setText("Consumer running...");
                canvas.getConsumer1().start(consumerSleepTime, true);
                canvas.getConsumer2().start(consumerSleepTime, true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                btConsumerStep.setEnabled(true);
                btConsumerRun.setText("Run Consumer");
                canvas.getConsumer1().stop();
                canvas.getConsumer2().stop();
            }
        });
        tfConsumerSleepTime = new JTextField("1000");
        tfConsumerSleepTime.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        long sleepTime = Long.parseLong(tfConsumerSleepTime.getText());
                        if (sleepTime > 0) {
                            consumerSleepTime = sleepTime;
                            System.out.println("Consumer sleep time: " + consumerSleepTime);
                        } else {
                            tfConsumerSleepTime.setText("" + consumerSleepTime);
                        }
                    } catch (Exception ignored) { }
                }
            }
        });
        btConsumerStep = new JButton("Consumer step");
        btConsumerStep.setFocusPainted(false);
        btConsumerStep.addActionListener(e -> {
            if (Math.random() < 0.5) {
                canvas.getConsumer1().step(true);
            } else {
                canvas.getConsumer2().step(true);
            }
        });
        toolbarPane.add(new JLabel("Producer sleep time:"));
        toolbarPane.add(tfProducerSleepTime);
        toolbarPane.add(btProducerRun);
        toolbarPane.add(btProducerStep);
        toolbarPane.add(new JLabel("Consumer sleep time:"));
        toolbarPane.add(tfConsumerSleepTime);
        toolbarPane.add(btConsumerRun);
        toolbarPane.add(btConsumerStep);

        contentPane.add(toolbarPane, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new QueueWindow().setVisible(true);
        });
    }

}