package de.lwerner.threads.threadsafequeue;

/**
 *
 * @author Lukas Werner
 */
public class QueueProgram {

    private static final int QUEUE_SIZE = 10;

    public static void main(String[] args) {
        NonBlockingQueue<Integer> queue = new NonBlockingQueue<>(QUEUE_SIZE);
        QueueProducer producer = new QueueProducer(queue);
        QueueConsumer consumer = new QueueConsumer(queue);
        producer.start(0);
        consumer.start(0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.stop();
        consumer.stop();
    }

}