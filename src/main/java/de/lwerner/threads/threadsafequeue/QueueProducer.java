package de.lwerner.threads.threadsafequeue;

import java.util.logging.Logger;

public class QueueProducer extends Stepper {

    private static int producerIndex = 0;
    private NonBlockingQueue<Integer> queue;

    private int counter;

    public QueueProducer(NonBlockingQueue<Integer> queue) {
        super("Producer " + producerIndex++);
        this.queue = queue;
    }

    @Override
    public void step() {
        queue.enqueue(++counter, true, thread.getName());
        super.step();
    }

}