package de.lwerner.threads.threadsafequeue;

public class QueueProducer extends Stepper {

    private NonBlockingQueue<Integer> queue;

    private int counter;

    public QueueProducer(NonBlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void step() {
        queue.enqueue(++counter, true);
        super.step();
    }

}