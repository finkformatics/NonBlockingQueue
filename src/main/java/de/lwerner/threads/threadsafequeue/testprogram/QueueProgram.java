package de.lwerner.threads.threadsafequeue.testprogram;

import de.lwerner.threads.threadsafequeue.NonBlockingQueue;
import de.lwerner.threads.threadsafequeue.consumerProducer.QueueConsumer;
import de.lwerner.threads.threadsafequeue.consumerProducer.QueueProducer;

/**
 * Test program
 *
 * @author Lukas Werner
 */
public class QueueProgram {

    private static final int DEFAULT_QUEUE_SIZE = 100;
    private static final int DEFAULT_PRODUCERS_COUNT = 5;
    private static final int DEFAULT_CONSUMERS_COUNT = 5;

    /**
     * Test application
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        int queueSize = DEFAULT_QUEUE_SIZE;
        int producersCount = DEFAULT_PRODUCERS_COUNT;
        int consumersCount = DEFAULT_CONSUMERS_COUNT;
        try {
            queueSize = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_QUEUE_SIZE;
            producersCount = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PRODUCERS_COUNT;
            consumersCount = args.length > 2 ? Integer.parseInt(args[2]) : DEFAULT_CONSUMERS_COUNT;
        } catch (Exception e) {
            System.out.println("Usage: <program call> [queue size] [producers count] [consumers count]");
            throw e;
        }

        NonBlockingQueue<Integer> queue = new NonBlockingQueue<>(queueSize);
        QueueProducer producer[] = new QueueProducer[producersCount];
        QueueConsumer consumer[] = new QueueConsumer[consumersCount];

        for (int i = 0; i < consumersCount; i++) {
            int sleep = (int) (Math.random() * 200);
            consumer[i] = new QueueConsumer(queue);
            consumer[i].start(sleep);
        }

        for (int i = 0; i < producersCount; i++) {
            int sleep = (int) (Math.random() * 200);
            producer[i] = new QueueProducer(queue);
            producer[i].start(sleep);
        }

    }

}