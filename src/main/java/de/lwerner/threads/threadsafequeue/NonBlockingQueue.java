package de.lwerner.threads.threadsafequeue;

import java.util.ArrayList;
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
    private final ArrayList<T> fields;

    private int readIndex;
    private int writeIndex;

    private Lock enqueueLock = new ReentrantLock(); // ReentrantLock because the lock-holder can enter other methods locked with the same lock
    private Lock dequeueLock = new ReentrantLock();

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
            fields.add(null);
        }
    }

    /**
     * Enqueues a new element to the current read index
     *
     * @param element element the element to enqueue
     * @param abortOnWait let the method return if wait would be called
     */
    public void enqueue(T element, boolean abortOnWait) {
        enqueueLock.lock();
        // TODO: Wait as long the queue isn't writable
        /*while (!writable()) {
            try {
                if (abortOnWait) {
                    return;
                }
                wait();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }*/
        LOGGER.info(String.format("Enqueuing element %s at index %d.", element.toString(), writeIndex));
        fields.set(writeIndex, element);
        writeIndex = (writeIndex + 1) % size; // Thread-safe because only here it'll be updated
        // notifyAll();
        enqueueLock.unlock();
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
        dequeueLock.lock();
        // TODO: Wait as long the queue isn't readable
        /*while (!readable()) {
            try {
                if (abortOnWait) {
                    return null;
                }
                wait();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }*/
        T element = fields.get(readIndex);
        fields.set(readIndex, null);
        LOGGER.info(String.format("Dequeuing element %s from index %d.", element.toString(), readIndex));
        readIndex = (readIndex + 1) % size; // Thread-safe because only here it'll be updated
        // notifyAll();
        dequeueLock.unlock();
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
     * Checks if the queue is readable at the current read index
     *
     * The condition is fields[readIndex] != null
     *
     * @return true, if readable
     */
    private boolean readable() {
        dequeueLock.lock();
        boolean readable = !isEmpty(readIndex);
        LOGGER.info("readable() returns " + readable + ".");
        dequeueLock.unlock();
        return readable;
    }

    /**
     * Checks if the queue is writable at the current write index
     *
     * The condition is fields[writeIndex] == null
     *
     * @return true, if writable
     */
    private boolean writable() {
        enqueueLock.lock();
        boolean writable = isEmpty(writeIndex);
        LOGGER.info("writable() returns " + writable + ".");
        enqueueLock.unlock();
        return writable;
    }

    /**
     * Checks if the value at the given index is empty
     *
     * @todo currently checks against null, should be done better, because you can add null to a queue
     *
     * @param index the index of the value to check
     * @return true, if empty
     */
    private boolean isEmpty(int index) {
        enqueueLock.lock();
        dequeueLock.lock();
        boolean ret = getFieldContent(index) == null;
        enqueueLock.lock();
        dequeueLock.lock();
        return ret;
    }

    /**
     * Helper method for testing purposes. Clears the fields and indexes.
     */
    protected void clear() {
        enqueueLock.lock();
        dequeueLock.lock();
        readIndex = 0;
        writeIndex = 0;
        fields.clear();
        enqueueLock.unlock();
        dequeueLock.unlock();
    }

    /**
     * Thread-safe getter for the contents of a field
     *
     * @param index the index of the field
     * @return the contents of the field specified by the index
     */
    protected T getFieldContent(int index) {
        enqueueLock.lock(); // TODO: Check if safe
        dequeueLock.lock();
        T ret = fields.get(index);
        enqueueLock.unlock();
        dequeueLock.unlock();
        return ret;
    }

    /**
     * Returns the current write index
     *
     * @return current write index
     */
    protected int getWriteIndex() {
        enqueueLock.lock();
        int ret = writeIndex;
        enqueueLock.unlock();
        return ret;
    }

    /**
     * Returns the current read index
     *
     * @return current read index
     */
    protected int getReadIndex() {
        dequeueLock.lock();
        int ret = readIndex;
        dequeueLock.unlock();
        return ret;
    }
}