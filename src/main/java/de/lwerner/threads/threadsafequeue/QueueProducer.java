package de.lwerner.threads.threadsafequeue;

import java.util.logging.Logger;

public class QueueProducer extends Stepper {

    private static int producerNumber = 0;

    private NonBlockingQueue<Integer> queue;

    private int counter;

    public QueueProducer(NonBlockingQueue<Integer> queue) {
        super("Producer" + producerNumber++);
        this.queue = queue;
    }

    @Override
    public void step(boolean abortOnWait) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Thread '" + thread.getName() + "' step.");
        queue.enqueue(++counter, abortOnWait);
        super.step();
    }

}