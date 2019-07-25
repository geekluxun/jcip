package com.geekluxun;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Memoizer2
 * <p/>
 * Replacing HashMap with ConcurrentHashMap
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Memoizer2<A, V> implements Computable<A, V> {
    /**
     * 使用同步的hashmap 伸缩性强
     */
    private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
    private final Computable<A, V> c;

    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }

    /**
     * 如果计算是一个很耗时的操作，会可能导致两个线程重复计算一个任务的问题
     *
     * @param arg
     * @return
     * @throws InterruptedException
     */
    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
