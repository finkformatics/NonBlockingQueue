package de.lwerner.threads.threadsafequeue.de.lwerner.threads.threadsafequeue.visualization;

import de.lwerner.threads.threadsafequeue.QueueConsumer;
import de.lwerner.threads.threadsafequeue.QueueProducer;

import javax.swing.*;
import java.awt.*;

class QueueCanvas extends JPanel {

    private static final Color CLEAR_COLOR = Color.WHITE;

    private VisualizedQueue queue;

    private QueueProducer producer;
    private QueueConsumer consumer;

    QueueCanvas() {
        queue = new VisualizedQueue(this);

        producer = new QueueProducer(queue);
        producer.addStepListener(this::repaint);
        consumer = new QueueConsumer(queue);
        consumer.addStepListener(this::repaint);

        setPreferredSize(new Dimension(1000, 500));
    }

    QueueProducer getProducer() {
        return producer;
    }

    QueueConsumer getConsumer() {
        return consumer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(CLEAR_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        queue.render(g);
    }
}