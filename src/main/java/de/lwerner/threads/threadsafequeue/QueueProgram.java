package de.lwerner.threads.threadsafequeue;

/**
 *
 * @author Lukas Werner
 */
public class QueueProgram {

    private static final int QUEUE_SIZE = 10;

    public static void main(String[] args) {
        NonBlockingQueue<Integer> queue = new NonBlockingQueue<>(QUEUE_SIZE);
        QueueProducer producer[] = new QueueProducer[5];
        QueueConsumer consumer[] = new QueueConsumer[5];

        for (int i = 0; i < 5; i++) {
            int sleep = (int) (Math.random() * 4000);
            consumer[i] = new QueueConsumer(queue);
            consumer[i].start(sleep);
        }

        for (int i = 0; i < 5; i++) {
            int sleep = (int) (Math.random() * 4000);
            producer[i] = new QueueProducer(queue);
            producer[i].start(sleep);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 5; i++) {
            //producer[i].stop();
        }

        for (int i = 0; i < 5; i++) {
            //producer[i].stop();
        }

        //System.out.println("Stopped");
    }

}