package de.lwerner.threads.threadsafequeue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe queue implementation with pre-defined length and to indexes (read and write).
 *
 * @param <T> Type of queue elements
 *
 * @author Lukas Werner
 * @author Toni Pohl
 */
public class NonBlockingQueue<T> {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final int size;

    private Index writeIndex;
    private Index readIndex;

    private final ArrayList<QueueField<T>> queue = new ArrayList<>();
    private ReentrantLock fieldLock = new ReentrantLock();
    private Condition fieldCondition = fieldLock.newCondition();


    /**
     * Sets the size of the queue.
     *
     * @param size the size (number of elements the queue can contain simultaneously)
     */
    public NonBlockingQueue(int size) {
        LOGGER.info(String.format("Creating Queue with size %d.", size));
        this.size = size;
        for (int i = 0; i < size; i++) {
            queue.add(new QueueField<T>());
        }

        this.writeIndex = new Index(size);
        this.readIndex = new Index(size);
    }

    /**
     * Enqueues a new element to the current read index
     *
     * @param element element the element to enqueue
     * @param abortOnWait let the method return if wait would be called
     */
    public void enqueue(T element, boolean abortOnWait, String threadName) {
        int index = writeIndex.get();
        //fieldLock.lock();
        QueueField<T> field = queue.get(index);
        field.lock.lock();
        //fieldLock.unlock();

        while (! (field.empty)) {
            try {
                field.condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        field.data = element;
        field.empty = false;
        LOGGER.info(threadName + ": Enqueue element " + element + " at index " + index);
        field.condition.signalAll();
        field.lock.unlock();
        writeIndex.inc();
    }

    /**
     * Enqueues a new element to the current read index
     *
     * @param element the element to enqueue
     */
    public void enqueue(T element, String threadName) {
        enqueue(element, false, threadName);
    }

    /**
     * Dequeues the current element (specified by the read index)
     *
     * @param abortOnWait let the method return if wait would be called
     * @return the fetched element
     */
    public T dequeue(boolean abortOnWait, String threadName) {
        int index = readIndex.get();
        //fieldLock.lock();
        QueueField<T> field = queue.get(index);
        field.lock.lock();
        //fieldLock.unlock();

        while (field.empty) {
            try {
                field.condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        T data = field.data;
        field.empty = true;
        LOGGER.info(threadName + ": Dequeue element " + data + " at index " + index);
        field.condition.signalAll();
        field.lock.unlock();
        readIndex.inc();

        return data;
    }

    /**
     * Dequeues the current element (specified by the read index)
     *
     * @return the fetched element
     */
    public T dequeue(String threadName) {
        return dequeue(false, threadName);
    }


    private class QueueField<T> {
        T data;
        boolean empty = true;
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
    }

    private class Index {
        private int index = 0;
        private int max;
        private ReentrantLock lock = new ReentrantLock();

        public Index(int max) {
            this.max = max;
        }

        public int get() {
            lock.lock();
            return index;
        }

        public void inc() {
            index = (index + 1) % max;
            lock.unlock();
        }
    }
}