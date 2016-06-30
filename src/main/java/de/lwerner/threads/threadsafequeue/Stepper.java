package de.lwerner.threads.threadsafequeue;

import java.util.ArrayList;
import java.util.List;

public abstract class Stepper {

    private List<StepListener> listeners = new ArrayList<>();

    protected Thread thread;

    private String threadName;

    public Stepper(String threadName) {
        this.threadName = threadName;
    }

    public void addStepListener(StepListener listener) {
        listeners.add(listener);
    }

    private void fireStepEvent() {
        for (StepListener listener: listeners) {
            listener.stepExecuted();
        }
    }

    public abstract void step(boolean abortOnWait);

    protected void step() {
        fireStepEvent();
    }

    public void start(long sleepTime, boolean abortOnWait) {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(() -> {
                while (true) {
                    step(abortOnWait);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }, threadName);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}