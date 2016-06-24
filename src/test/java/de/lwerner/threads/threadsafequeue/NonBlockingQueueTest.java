package de.lwerner.threads.threadsafequeue;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test of the NonBlockingQueue class
 *
 * @todo implement further tests
 *
 * @author Lukas Werner
 * @author Toni Pohl
 */
public class NonBlockingQueueTest {

    private static final int QUEUE_SIZE = 10;

    private NonBlockingQueue<Integer> queue;

    @Before
    public void setUp() throws Exception {
        queue = new NonBlockingQueue<>(QUEUE_SIZE);
    }

    /**
     * Der Pegel für das Schreiben darf den Pegel für das Lesen nicht überholen können
     */
    @Test
    public void writeIndexCannotPassReadIndex() {
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i, true);
        }
        for (int i = 0; i < 5; i++) {
            queue.dequeue(true);
        }
        for (int i = 10; i < 12; i++) {
            queue.enqueue(i, true);
        }
        for (int i = 12; i < 16; i++) {
            queue.enqueue(i, true);
        }
        assertEquals(5, queue.getWriteIndex());
    }

    /**
     * Der Pegel für das Lesen darf den Pegel für das Schreiben nicht überholen können
     */
    @Test
    public void readIndexCannotPassWriteIndex() {
        for (int i = 0; i < 3; i++) {
            queue.enqueue(i, true);
        }
        for (int i = 0; i < 4; i++) {
            queue.dequeue(true);
        }
        assertEquals(3, queue.getReadIndex());
    }

    /**
     * Lesende und schreibende Zugriffe sollen sich nicht gegebseitig blockieren,
     * solange die jeweilige Bedingung erfüllt ist
     */
    @Test
    public void simultaneouslyReadableAndWritable() {

    }

}