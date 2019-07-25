package com.geekluxun;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * ReaderThread
 * <p/>
 * Encapsulating nonstandard cancellation in a Thread by overriding interrupt
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ReaderThread extends Thread {
    private static final int BUFSZ = 512;
    private final Socket socket;
    private final InputStream in;


    public static void main(String[] argc) {

        try {
            ReaderThread readerThread = new ReaderThread(new Socket("localhost", 8080));
            readerThread.start();

            TimeUnit.SECONDS.sleep(5);
            readerThread.interrupt();
            readerThread.join();
        } catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

    }

    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }

    /**
     * 因为socket是不可中断的，只能通过IOException才能终止线程
     */
    @Override
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[BUFSZ];
            while (true) {
                // 阻塞
                int count = in.read(buf);
                if (count < 0)
                    break;
                else if (count > 0)
                    processBuffer(buf, count);
            }
        } catch (IOException e) { /* Allow thread to exit */
            System.out.println("我被中止了...");
        }
    }

    public void processBuffer(byte[] buf, int count) {
    }
}
