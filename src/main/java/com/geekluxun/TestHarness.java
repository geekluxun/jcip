package com.geekluxun;

import java.util.concurrent.*;

/**
 * TestHarness
 * <p/>
 * Using CountDownLatch for starting and stopping threads in timing tests
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task)
            throws InterruptedException {

        /**开始阀门*/
        final CountDownLatch startGate = new CountDownLatch(1);
        /**关闭阀门*/
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        /**等待开始阀门打开*/
                        startGate.await();
                        try {
                            task.run();
                        } finally {

                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }

        long start = System.nanoTime();
        /** 打开开始阀门*/
        startGate.countDown();
        /** 等待关闭阀门打开*/
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }
}
