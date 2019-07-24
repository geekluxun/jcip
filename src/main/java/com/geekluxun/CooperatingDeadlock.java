package com.geekluxun;

import net.jcip.annotations.GuardedBy;

import java.util.HashSet;
import java.util.Set;

/**
 * CooperatingDeadlock
 * <p/>
 * Lock-ordering deadlock between cooperating objects
 *
 * @author Brian Goetz and Tim Peierls
 */
public class CooperatingDeadlock {

    public static void main(String[] argc) {
        CooperatingDeadlock demo = new CooperatingDeadlock();
        demo.demo1();
    }

    /**
     * 模拟两线程，彼此都持有对方锁，导致死锁
     */
    private void demo1() {
        Dispatcher dispatcher = new Dispatcher();
        Taxi taxi = new Taxi(dispatcher);

        new Thread(new Runnable() {
            @Override
            public void run() {
                taxi.setLocation(new Point(1, 2));
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dispatcher.getImage();
            }
        }).start();
    }

    // Warning: deadlock-prone!
    class Taxi {
        @GuardedBy("this")
        private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setLocation(Point location) {
            this.location = location;
            if (location.equals(destination))
                dispatcher.notifyAvailable(this);
        }

        public synchronized Point getDestination() {
            return destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }

    class Dispatcher {
        @GuardedBy("this")
        private final Set<Taxi> taxis;
        @GuardedBy("this")
        private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t : taxis)
                image.drawMarker(t.getLocation());
            return image;
        }
    }

    class Image {
        public void drawMarker(Point p) {
        }
    }
}
