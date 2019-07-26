package com.geekluxun;

import java.util.concurrent.*;

/**
 * ThreadDeadlock
 * <p/>
 * Task that deadlocks in a single-threaded Executor
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ThreadDeadlock {
    // 这是一个单线程的线程池
    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        public String call() throws Exception {
            // Here's where we would actually read the file
            return "";
        }
    }

    /**
     * 这个任务等待其他两个任务，会发生线程饥饿死锁
     */
    public class RenderPageTask implements Callable<String> {
        public String call() throws Exception {
            Future<String> header, footer;
            // 把任务放到工作队列
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            // Will deadlock -- task waiting for result of subtask
            // 因为是单线程池，如果两个子任务无法执行
            return header.get() + page + footer.get();
        }
        
        private String renderBody() {
            // Here's where we would actually render the page
            return "";
        }
    }
}
