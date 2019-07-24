package com.geekluxun;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicPseudoRandom
 * <p/>
 * Random number generator using AtomicInteger
 * 原子变量实现随机数
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class AtomicPseudoRandom extends PseudoRandom {
    private AtomicInteger seed;

    public static void main(String[] argc) {
        AtomicPseudoRandom random = new AtomicPseudoRandom(100);
        for (int i = 0; i < 10; i++) {
            System.out.println("random:" + random.nextInt(500));
        }
    }

    AtomicPseudoRandom(int seed) {
        this.seed = new AtomicInteger(seed);
    }

    /**
     * 产生n之内的随机数
     *
     * @param n
     * @return
     */
    public int nextInt(int n) {
        while (true) {
            int s = seed.get();
            int nextSeed = calculateNext(s);
            /**
             * 当前值和s值相同则设置，否则循环直到成功
             */
            if (seed.compareAndSet(s, nextSeed)) {
                int remainder = s % n;
                return remainder > 0 ? remainder : remainder + n;
            }
        }
    }
}
