package de.lwerner.threads.threadsafequeue;

import java.util.ArrayList;
import java.util.List;

public abstract class Stepper {

    private List<StepListener> listeners = new ArrayList<>();

    private Thread thread;

    public void addStepListener(StepListener listener) {
        listeners.add(listener);
    }

    private void fireStepEvent() {
        for (StepListener listener: listeners) {
            listener.stepExecuted();
        }
    }

    public void step() {
        fireStepEvent();
    }

    public void start(long sleepTime) {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(() -> {
                while (true) {
                    step();
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            thread.start();
        }
    }

    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}