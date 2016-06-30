package de.lwerner.threads.threadsafequeue.visualization;

import de.lwerner.threads.threadsafequeue.QueueConsumer;
import de.lwerner.threads.threadsafequeue.QueueProducer;

import javax.swing.*;
import java.awt.*;

class QueueCanvas extends JPanel {

    private static final Color CLEAR_COLOR = Color.WHITE;

    private VisualizedQueue queue;

    private QueueProducer producer1;
    private QueueProducer producer2;
    private QueueConsumer consumer1;
    private QueueConsumer consumer2;

    QueueCanvas() {
        queue = new VisualizedQueue(this);

        producer1 = new QueueProducer(queue);
        producer2 = new QueueProducer(queue);
        producer1.addStepListener(this::repaint);
        producer2.addStepListener(this::repaint);
        consumer1 = new QueueConsumer(queue);
        consumer2 = new QueueConsumer(queue);
        consumer1.addStepListener(this::repaint);
        consumer2.addStepListener(this::repaint);

        setPreferredSize(new Dimension(1000, 500));
    }

    QueueProducer getProducer1() {
        return producer1;
    }
    QueueProducer getProducer2() {
        return producer2;
    }

    QueueConsumer getConsumer1() {
        return consumer1;
    }
    QueueConsumer getConsumer2() {
        return consumer2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(CLEAR_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        queue.render(g);
    }
}