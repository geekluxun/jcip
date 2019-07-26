package com.geekluxun;

import net.jcip.annotations.ThreadSafe;

/**
 * SleepyBoundedBuffer
 * <p/>
 * Bounded buffer using crude blocking
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    int SLEEP_GRANULARITY = 60;

    public SleepyBoundedBuffer() {
        this(100);
    }

    public SleepyBoundedBuffer(int size) {
        super(size);
    }

    /**
     * 这里封装了对先验条件（缓存满则等待），而不是让调用者处理
     * 抛出InterruptedException是表示睡眠可以被中断
     * @param v
     * @throws InterruptedException
     */
    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            // 注意，线程睡眠时候不能再持有锁，所以这行代码不能放在同步块中！！！（如果还持有锁，就不能让其他线程来完成这个先验条件）
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }

    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty())
                    return doTake();
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
}
