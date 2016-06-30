package de.lwerner.threads.threadsafequeue;

import java.util.ArrayList;
import java.util.Queue;
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

    protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected final int size;
    protected final ArrayList<QueueField<T>> fields;

    protected int readIndex;
    protected int writeIndex;

    protected Lock fieldLock = new ReentrantLock();

    protected Lock enqueueLock = new ReentrantLock(); // ReentrantLock because the lock-holder can enter other methods locked with the same lock
    protected Lock dequeueLock = new ReentrantLock();

    protected Condition enqueueCondition = enqueueLock.newCondition();
    protected Condition dequeueCondition = dequeueLock.newCondition();

    /**
     * Sets the size of the queue.
     *
     * @param size the size (number of elements the queue can contain simultaneously)
     */
    public NonBlockingQueue(int size) {
        LOGGER.info(String.format("Creating Queue with size %d.", size));
        this.size = size;
        fields = new ArrayList<>(size);
        while (size-- > 0) {
            fields.add(new QueueField<>());
        }
    }

    /**
     * Enqueues a new element to the current read index
     *
     * @param element element the element to enqueue
     * @param abortOnWait let the method return if wait would be called
     */
    public void enqueue(T element, boolean abortOnWait) {
        fieldLock.lock();
        QueueField<T> field = fields.get(writeIndex);
        field.lock.lock();
        fieldLock.unlock();
        while (!field.empty) {
            try {
                if (abortOnWait) {
                    field.lock.unlock();
                    return;
                }
                field.enqueueCondition.await();
            } catch (InterruptedException e) {
                // field.lock.unlock();
            }
        }
        field.data = element;
        field.empty = false;
        LOGGER.info("Enqueuing element " + element + " at index " + writeIndex);
        writeIndex = (writeIndex + 1) % size;
        field.dequeueCondition.signalAll();
        field.lock.unlock();
    }

    /**
     * Enqueues a new element to the current read index
     *
     * @param element the element to enqueue
     */
    public void enqueue(T element) {
        enqueue(element, false);
    }

    /**
     * Dequeues the current element (specified by the read index)
     *
     * @param abortOnWait let the method return if wait would be called
     * @return the fetched element
     */
    public T dequeue(boolean abortOnWait) {
        T element = null;
        fieldLock.lock();
        QueueField<T> field = fields.get(readIndex);
        field.lock.lock();
        fieldLock.unlock();
        while (field.empty) {
            try {
                if (abortOnWait) {
                    field.lock.unlock();
                    return null;
                }
                field.dequeueCondition.await();
            } catch (InterruptedException e) {
                // field.lock.unlock();
            }
        }
        element = field.data;
        field.data = null;
        field.empty = true;
        LOGGER.info("Dequeuing element " + element + " at index " + readIndex);
        readIndex = (readIndex + 1) % size;
        field.enqueueCondition.signalAll();
        field.lock.unlock();
        return element;
    }

    /**
     * Dequeues the current element (specified by the read index)
     *
     * @return the fetched element
     */
    public T dequeue() {
        return dequeue(false);
    }

    /**
     * Helper method for testing purposes. Clears the fields and indexes.
     */
    protected synchronized void clear() {
        readIndex = 0;
        writeIndex = 0;
        fields.clear();
    }

    /**
     * Thread-safe getter for the contents of a field
     *
     * @param index the index of the field
     * @return the contents of the field specified by the index
     */
    protected T getFieldContent(int index) {
        fieldLock.lock();
        T ret = fields.get(index).data;
        fieldLock.unlock();
        return ret;
    }

    /**
     * Returns the current write index
     *
     * @return current write index
     */
    protected int getWriteIndex() {
        return writeIndex;
    }

    /**
     * Returns the current read index
     *
     * @return current read index
     */
    protected int getReadIndex() {
        return readIndex;
    }

    protected static class QueueField<T> {

        T data;
        boolean empty;
        ReentrantLock lock = new ReentrantLock();
        Condition enqueueCondition = lock.newCondition();
        Condition dequeueCondition = lock.newCondition();

        QueueField() {
            empty = true;
        }
    }
}