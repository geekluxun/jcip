package com.geekluxun;

import java.util.*;
import java.util.concurrent.*;

/**
 * Memoizer3
 * <p/>
 * Memoizing wrapper using FutureTask
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Memoizer3 <A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache
            = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        /** cache里没有则去计算 ，但  存在 cache.get 和f == null  这个组合操作不是原子性的缺陷*/
        if (f == null) {
            Callable<V> eval = new Callable<V>() {
                public V call() throws InterruptedException {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run(); // call to c.compute happens here
        }
        try {
            /**有计算结果立即返回，否则阻塞等待 ，这样避免一个任务在计算一个值时，另一个任务也请求
             *计算同一个值时，不用重复计算，只要等待结果即可
             */
            return f.get();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }
}