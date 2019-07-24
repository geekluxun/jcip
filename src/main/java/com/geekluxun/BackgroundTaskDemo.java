package com.geekluxun;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

/**
 * Copyright,2018-2019,geekluxun Co.,Ltd.
 *
 * @Author: luxun
 * @Create: 2019-03-20 15:25
 * @Description:
 * @Other:
 */
public class BackgroundTaskDemo extends BackgroundTask {

    public static void main(String[] argc) throws Exception {

        BigInteger[] a = new BigInteger[]{BigInteger.valueOf(3L)};

        BackgroundTaskDemo demo = new BackgroundTaskDemo();
        // TODO 阻塞在此??
        demo.run();

        while (true) {
            System.out.println("等待用户输入指令...");
            int value = System.in.read();
            if (value == 33) {
                demo.cancel(true);
            }
        }


    }

    @Override
    protected Object compute() throws Exception {
        System.out.println("开始执行计算任务...");
        TimeUnit.SECONDS.sleep(2);
        setProgress(12, 99);

        TimeUnit.SECONDS.sleep(2);
        setProgress(34, 99);

        TimeUnit.SECONDS.sleep(2);
        setProgress(51, 99);

        TimeUnit.SECONDS.sleep(2);
        setProgress(79, 99);

        TimeUnit.SECONDS.sleep(2);
        setProgress(98, 99);

        System.out.println("计算任务执行完成...");
        return "completed";
    }

    /**
     * 任务完成的时候通知
     *
     * @param result
     * @param exception
     * @param cancelled
     */
    @Override
    protected void onCompletion(Object result, Throwable exception, boolean cancelled) {
        if (cancelled) {
            System.out.println("任务被取消了...");
        } else {
            System.out.println("任务完成了,结果:" + result);
        }
    }

    @Override
    protected void onProgress(int current, int max) {
        BigDecimal process = BigDecimal.valueOf(((double) current / max) * 100);
        process = process.setScale(0, RoundingMode.DOWN);
        System.out.println("当前任务完成进度：%" + process.intValue());
    }

}
