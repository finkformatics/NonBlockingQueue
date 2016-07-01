package de.lwerner.threads.threadsafequeue.consumerProducer;

import de.lwerner.threads.threadsafequeue.NonBlockingQueue;

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
        if (Math.random() < 0.05) {
            try {
                Thread.sleep((long)(Math.random() * 1500));
            } catch (InterruptedException e) { }
        }
        queue.enqueue(++counter, true);
        super.step();
    }

}