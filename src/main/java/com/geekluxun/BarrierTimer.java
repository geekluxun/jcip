package com.geekluxun;

/**
 * BarrierTimer
 * <p/>
 * Barrier-based timer
 * 基于关卡的计时器
 * @author Brian Goetz and Tim Peierls
 */
public class BarrierTimer implements Runnable {
    private boolean started;
    private long startTime, endTime;

    @Override
    public synchronized void run() {
        long t = System.nanoTime();
        if (!started) {
            started = true;
            startTime = t;
        } else
            endTime = t;
    }

    public synchronized void clear() {
        started = false;
    }

    public synchronized long getTime() {
        return endTime - startTime;
    }
}
