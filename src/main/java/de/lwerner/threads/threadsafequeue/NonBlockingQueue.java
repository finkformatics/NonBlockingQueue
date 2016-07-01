package de.lwerner.threads.threadsafequeue;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
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

    /**
     * The logger object
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * The size of this queue
     */
    private final int size;

    /**
     * The write index
     */
    private Index writeIndex;
    /**
     * The read index
     */
    private Index readIndex;

    /**
     * The fields (actual queue)
     */
    private final ArrayList<QueueField<T>> queue = new ArrayList<>();

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
     * Get the size of this queue
     *
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the read index (thread-safe)
     *
     * @return read index
     */
    public int getReadIndex() {
        readIndex.lock();
        int index = readIndex.index;
        readIndex.unlock();
        return index;
    }

    /**
     * Get the write index (thread-safe)
     *
     * @return write index
     */
    public int getWriteIndex() {
        writeIndex.lock();
        int index = writeIndex.index;
        writeIndex.unlock();
        return index;
    }

    /**
     * Enqueues a new element to the current read index
     *
     * @param element element the element to enqueue
     * @param abortOnWait let the method return if wait would be called
     */
    public void enqueue(T element, boolean abortOnWait) {
        writeIndex.lock();
        QueueField<T> field = queue.get(writeIndex.get());
        field.lock();

        while (!(field.empty)) {
            try {
                if (abortOnWait) {
                    field.unlock();
                    writeIndex.unlock();
                    return;
                }
                field.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        field.data = element;
        field.empty = false;
        LOGGER.info(Thread.currentThread().getName() + ": Enqueue element " + element + " at index " + writeIndex.get());
        field.signalAll();
        field.unlock();
        writeIndex.inc();
        writeIndex.unlock();
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
        readIndex.lock();
        QueueField<T> field = queue.get(readIndex.get());
        field.lock();

        while (field.empty) {
            try {
                if (abortOnWait) {
                    readIndex.unlock();
                    field.unlock();
                    return null;
                }
                field.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        T data = field.data;
        field.empty = true;
        LOGGER.info(Thread.currentThread().getName() + ": Dequeue element " + data + " at index " + readIndex.get());
        field.signalAll();
        field.unlock();
        readIndex.inc();
        readIndex.unlock();
        return data;
    }

    /**
     * Get the field at given index.
     *
     * @param index given index
     * @return the field to fetch
     */
    public QueueField<T> getField(int index) {
        return queue.get(index).clone();
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
     * QueueField class to represent an entry in the queue
     *
     * @param <T> the type of the QueueField.
     *
     * @author Lukas Werner
     * @author Toni Pohl
     */
    protected class QueueField<T> {

        /**
         * Physical field data
         */
        T data;
        /**
         * Indicator if this field is empty (allows null values)
         */
        boolean empty = true;
        /**
         * The lock of this field
         */
        ReentrantLock lock = new ReentrantLock();
        /**
         * The condition of the field's lock
         */
        Condition condition = lock.newCondition();

        /**
         * Wrapper-method for condition.await()
         *
         * @throws InterruptedException if condition.await() throws it
         */
        void await() throws InterruptedException {
            condition.await();
        }

        /**
         * Wrapper-method for condition.signalAll()
         */
        void signalAll() {
            condition.signalAll();
        }

        /**
         * Lock this
         */
        void lock() {
            lock.lock();
        }

        /**
         * Unlock this
         */
        void unlock() {
            lock.unlock();
        }

        /**
         * Is this field empty?
         *
         * @return true, if empty
         */
        public boolean empty() {
            return empty;
        }

        /**
         * Get the physical data
         *
         * @return physical data
         */
        public T data() {
            return data;
        }

        /**
         * Clone this object
         *
         * @return clone
         */
        public QueueField<T> clone() {
            lock();
            QueueField<T> f = new QueueField<>();
            f.empty = empty;
            f.data = data;
            unlock();
            return f;
        }
    }

    /**
     * Index class to take care of concurrency
     *
     * @Author Toni Pohl
     * @Author Lukas Werner
     */
    private class Index {

        /**
         * The physical index
         */
        int index = 0;
        /**
         * Max value to role (via modulo)
         */
        int max;
        /**
         * The lock for this index
         */
        ReentrantLock lock = new ReentrantLock();

        /**
         * Sets the maximum index (practically the maximum index - 1)
         *
         * @param max the maximum index (modulo)
         */
        Index(int max) {
            this.max = max;
        }

        /**
         * Get the physical index
         *
         * @return the physical index
         */
        int get() {
            return index;
        }

        /**
         * Increment index
         */
        void inc() {
            index = (index + 1) % max;
        }

        /**
         * Lock this index
         */
        void lock() {
            lock.lock();
        }

        /**
         * Unlock this index
         */
        void unlock() {
            lock.unlock();
        }
    }
}