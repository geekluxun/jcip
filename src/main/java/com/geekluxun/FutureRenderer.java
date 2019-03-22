package com.geekluxun;

import java.util.*;
import java.util.concurrent.*;

import static com.geekluxun.LaunderThrowable.launderThrowable;

/**
 * FutureRenderer
 * <p/>
 * Waiting for image download with \Future
 *
 * @author Brian Goetz and Tim Peierls
 */
public abstract class FutureRenderer {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    
    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task =
                new Callable<List<ImageData>>() {
                    @Override
                    public List<ImageData> call() throws Exception{
                        int count = 0;
                        List<ImageData> result = new ArrayList<ImageData>();
                        for (ImageInfo imageInfo : imageInfos)
                            result.add(imageInfo.downloadImage());
                        while (true){
                            TimeUnit.SECONDS.sleep(2);
                            System.out.println("图片下载中...");
//                            if (count++ > 5){
//                                Thread.currentThread().interrupt();
//                            }
                        }
//                          if (true){
//                             throw new RuntimeException("下载图片发生了异常");
//                          }
                        //return result;
                    }
                };

        Future<List<ImageData>> future = executor.submit(task);
        // 控制台任务
        executor.execute(new ConsoleTask(future, executor, Thread.currentThread()));
        renderText(source);

        try {
            /** 此处只是创建了一个任务来执行所有的图片下载 请对比Renderer.java实现*/
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData)
                renderImage(data);
        } catch (InterruptedException e) {
            /** 这个异常是在当前线程被中断的时候抛出，不是future所对应的task*/
            // Re-assert the thread's interrupted status
            Thread.currentThread().interrupt();
            // We don't need the result, so cancel the task too
            future.cancel(true);
            System.out.println("任务线程被中断");
        } catch (ExecutionException e) {
            /** 任务内部执行出现异常的话会转换成ExecutionException异常*/
 //           throw launderThrowable(e.getCause());
            System.out.println("任务执行内部发生了异常");
        } catch (CancellationException e){
            System.out.println("任务被取消了");
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);
    
    private class ConsoleTask implements Runnable {
        
        private final Future future;
        private final  ExecutorService executor;
        private final Thread mainThread;
        
        public ConsoleTask(Future future, ExecutorService executor, Thread thread){
            this.future  = future;
            this.executor = executor;
            this.mainThread = thread;
        }
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (true){
                System.out.println("请输入命令...");
                int command = scanner.nextInt();
                switch (command){
                    case 1:
                        future.cancel(true);
                        break;
                    case 2:
                        executor.shutdownNow();
                        break;
                    case 3:
                        mainThread.interrupt();
                        break;
                }
            }
        }
    }
}
