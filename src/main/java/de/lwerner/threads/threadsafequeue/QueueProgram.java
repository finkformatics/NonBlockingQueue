package de.lwerner.threads.threadsafequeue;

/**
 *
 * @author Lukas Werner
 */
public class QueueProgram {

    private static final int QUEUE_SIZE = 10;

    public static void main(String[] args) {
        NonBlockingQueue<Integer> queue = new NonBlockingQueue<>(QUEUE_SIZE);
        QueueProducer[] producers = new QueueProducer[5];
        QueueConsumer[] consumers = new QueueConsumer[5];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new QueueConsumer(queue);
            long sleepTime = (long)(Math.random() * 400);
            consumers[i].start(sleepTime, false);
        }
        for (int i = 0; i < producers.length; i++) {
            producers[i] = new QueueProducer(queue);
            long sleepTime = (long)(Math.random() * 400);
            producers[i].start(sleepTime, false);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) { }
        System.out.println("Stop the bastards");
        for (int i = 0; i < producers.length; i++) {
            producers[i].stop();
        }
        for (int i = 0; i < consumers.length; i++) {
            consumers[i].stop();
        }
        System.out.println("Stopped");
    }

}