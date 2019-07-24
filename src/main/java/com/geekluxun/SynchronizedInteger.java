package com.geekluxun;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * SynchronizedInteger
 * <p/>
 * Thread-safe mutable integer holder
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SynchronizedInteger {
    @GuardedBy("this")
    private int value;

    /**
     * 读写都要同步，如果只同步写，读有可能读到旧值（cpu、编译器、jvm会有重排序）
     * 锁既保证了同步性，又保证了可见性,volatile只是可见性
     *
     * @return
     */
    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value) {
        this.value = value;
    }
}
