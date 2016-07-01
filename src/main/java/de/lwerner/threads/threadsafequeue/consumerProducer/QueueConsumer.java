package de.lwerner.threads.threadsafequeue.consumerProducer;

import de.lwerner.threads.threadsafequeue.NonBlockingQueue;

public class QueueConsumer extends Stepper {

    private static int consumerIndex = 0;

    private NonBlockingQueue<Integer> queue;

    public QueueConsumer(NonBlockingQueue<Integer> queue) {
        super("Consumer " + consumerIndex++);
        this.queue = queue;
    }

    @Override
    public void step() {
        queue.dequeue(true);
        super.step();
    }

}