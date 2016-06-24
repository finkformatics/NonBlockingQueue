package de.lwerner.threads.threadsafequeue;

public class QueueConsumer extends Stepper {

    private NonBlockingQueue<Integer> queue;

    public QueueConsumer(NonBlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void step() {
        queue.dequeue(true);
        super.step();
    }

}