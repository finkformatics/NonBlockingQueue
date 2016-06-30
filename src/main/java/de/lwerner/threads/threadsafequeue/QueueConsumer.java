package de.lwerner.threads.threadsafequeue;

import java.util.logging.Logger;

public class QueueConsumer extends Stepper {

    private static int consumerIndex = 0;

    private NonBlockingQueue<Integer> queue;

    public QueueConsumer(NonBlockingQueue<Integer> queue) {
        super("Consumer " + consumerIndex++);
        this.queue = queue;
    }

    @Override
    public void step() {
        queue.dequeue(true, thread.getName());
        super.step();
    }

}