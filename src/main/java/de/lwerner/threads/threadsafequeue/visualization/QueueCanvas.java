package de.lwerner.threads.threadsafequeue.visualization;

import de.lwerner.threads.threadsafequeue.consumerProducer.QueueConsumer;
import de.lwerner.threads.threadsafequeue.consumerProducer.QueueProducer;

import javax.swing.*;
import java.awt.*;

class QueueCanvas extends JPanel {

    private static final Color CLEAR_COLOR = Color.WHITE;

    private VisualizedQueue queue;

    private QueueProducer[] producers;
    private QueueConsumer[] consumers;

    QueueCanvas(int numberOfConsumersProducers) {
        queue = new VisualizedQueue(this);

        producers = new QueueProducer[numberOfConsumersProducers];
        consumers = new QueueConsumer[numberOfConsumersProducers];

        for (int i = 0; i < numberOfConsumersProducers; i++) {
            producers[i] = new QueueProducer(queue);
            producers[i].addStepListener(this::repaint);
            consumers[i] = new QueueConsumer(queue);
            consumers[i].addStepListener(this::repaint);
        }

        setPreferredSize(new Dimension(1000, 500));
    }

    QueueProducer[] getProducers() {
        return producers;
    }

    QueueConsumer[] getConsumers() {
        return consumers;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(CLEAR_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        queue.render(g);
    }
}