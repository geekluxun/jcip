package com.geekluxun;

import net.jcip.annotations.ThreadSafe;

/**
 * BoundedBuffer
 * <p/>
 * Bounded buffer using condition queues
 * 使用内部条件队列实现基于状态的条件
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer() {
        this(100);
    }

    public BoundedBuffer(int size) {
        super(size);
    }

    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        /** 从wait返回并一定表示条件满足，有可能被其他线程又改变了条件，
         *所以，需要再一次验证条件，此处所以用了while循环
         */
        while (isFull())
            wait();
        doPut(v);
        notifyAll();
    }

    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        while (isEmpty())
            wait();
        V v = doTake();
        notifyAll();
        return v;
    }

    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull())
            wait();
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();
    }
}
