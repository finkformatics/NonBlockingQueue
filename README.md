# NonBlockingQueue
This is an implementation of a thread-safe non-blocking queue. This project also provides a visualization for understanding the concept.

The core purpose is to solve an exercise given in the class "Thread Programmierung" at the HTWK Leipzig.

We had to implement a thread-safe queue with the capacity N and two indexes. One pointing at the next write field and one pointing at the next read field.
The indexes are managed by the queue itself (usually incremented after operation). The implementation should make use of locks and condition variables and
the methods enqueue() and dequeue() preferably shouldn't block each other.

Furthermore care has to be taken that you cannot enqueue if the value at the current writeIndex isn't empty and you cannot dequeue if the value at the current
readIndex is empty. This makes sure, but cas has also to be taken that the readIndex cannot pass the writeIndex and vice versa.

## Install
Simple import this Maven project into your favorite IDE and start either de.lwerner.threads.threadsafequeue.testprogram.QueueProgram or de.lwerner.threads.threadsafequeue.visualization.QueueWindow.