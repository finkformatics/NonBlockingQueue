package de.lwerner.threads.threadsafequeue;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe queue implementation with pre-defined length and to indexes (read and write).
 *
 * @param <T> Type of queue elements
 *
 * @author Lukas Werner
 */
public class NonBlockingQueue<T> {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final int size;
    protected final ArrayList<T> fields;

    protected int readIndex;
    protected int writeIndex;

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
    public synchronized void enqueue(T element, boolean abortOnWait) {
        while (!writable()) {
            try {
                if (abortOnWait) {
                    return;
                }
                wait();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        LOGGER.info(String.format("Enqueuing element %s at index %d.", element.toString(), writeIndex));
        fields.set(writeIndex, element);
        writeIndex = (writeIndex + 1) % size;
        notifyAll();
    }

    /**
     * Enqueues a new element to the current read index
     *
     * @param element the element to enqueue
     */
    public synchronized void enqueue(T element) {
        enqueue(element, false);
    }

    /**
     * Dequeues the current element (specified by the read index)
     *
     * @param abortOnWait let the method return if wait would be called
     * @return the fetched element
     */
    public synchronized T dequeue(boolean abortOnWait) {
        while (!readable()) {
            try {
                if (abortOnWait) {
                    return null;
                }
                wait();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        T element = fields.get(readIndex);
        fields.set(readIndex, null);
        LOGGER.info(String.format("Dequeuing element %s from index %d.", element.toString(), readIndex));
        readIndex = (readIndex + 1) % size;
        notifyAll();
        return element;
    }

    /**
     * Dequeues the current element (specified by the read index)
     *
     * @return the fetched element
     */
    public synchronized T dequeue() {
        return dequeue(false);
    }

    /**
     * Checks if the queue is readable at the current read index
     *
     * The condition is writeIndex != readIndex || fields[readIndex] != null
     *
     * @return true, if readable
     */
    private synchronized boolean readable() {
        boolean readable = writeIndex != readIndex;
        readable = readable || fields.get(readIndex) != null;
        LOGGER.info("readable() returns " + readable + ".");
        return readable;
    }

    /**
     * Checks if the queue is writable at the current write index
     *
     * The condition is writeIndex != readIndex || fields[writeIndex] == null
     *
     * @return true, if writable
     */
    private synchronized boolean writable() {
        boolean writable = writeIndex != readIndex;
        writable = writable || fields.get(writeIndex) == null;
        LOGGER.info("writable() returns " + writable + ".");
        return writable;
    }

    /**
     * Helper methods for testing purposes. Clears the fields and indexes
     */
    protected synchronized void clear() {
        readIndex = 0;
        writeIndex = 0;
        fields.clear();
    }

    /**
     * Returns the current write index
     *
     * @return current write index
     */
    public int getWriteIndex() {
        return writeIndex;
    }

    /**
     * Returns the current read index
     *
     * @return current read index
     */
    public int getReadIndex() {
        return readIndex;
    }
}