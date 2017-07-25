package com.epam.mentoring.lessfour;
import java.util.Random;

public class Opposition {
    public class Counter {
        private int count = 0;

        public void increment() {
            count++;
        }
        public void decrement() {
            count--;
        }
        public int get() {
            return count;
        }
    }

    public class Wrestler implements Runnable {
        private Counter counter;
        private boolean increment;
        private Random rand;

        public Wrestler(Counter counter, boolean increment) {
            this.counter = counter;
            this.increment = increment;
            rand = new Random();
        }

        @Override
        public void run() {
            while (true) {

                synchronized (counter) {
                    if (increment) {
                        counter.increment();
                    } else if (counter.get() == 0) {
                        if (sleep()) break;
                    } else {
                        counter.decrement();
                    }
                }

                int x = counter.get();
                if (x < 0) {
                    t1.interrupt();
                    t2.interrupt();
                    throw new IllegalStateException("We have below zero!");
                }

                System.out.println("Wrestler" + Thread.currentThread().getName() + " " + x);

                if (sleep()) {
                    break;
                }
            }
        }

        private boolean sleep() {
            try {
                Thread.sleep(rand.nextInt(100));
            } catch (InterruptedException e) {
                return true;
            }
            return false;
        }

    }

    private Thread t1, t2;
    public static void main(String[] args) {
        new Opposition().start();
    }

    private void start() {
        Counter counter = new Counter();
        t1 = new Thread(new Wrestler(counter, true));
        t2 = new Thread(new Wrestler(counter, false));
        t1.start();
        t2.start();
        try {
            while (true) {
                Thread.sleep(100);
                if (!(t1.isAlive() && t2.isAlive())) {
                    break;
                }
            }
        } catch (InterruptedException e) {
        }
        System.out.println("Finished");
    }
}