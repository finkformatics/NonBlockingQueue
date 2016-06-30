package de.lwerner.threads.threadsafequeue;

import java.util.logging.Logger;

public class QueueConsumer extends Stepper {

    private static int consumerNumber = 0;

    private NonBlockingQueue<Integer> queue;

    public QueueConsumer(NonBlockingQueue<Integer> queue) {
        super("Consumer" + consumerNumber++);
        this.queue = queue;
    }

    @Override
    public void step(boolean abortOnWait) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Thread '" + thread.getName() + "' step.");
        queue.dequeue(abortOnWait);
        step();
    }

}