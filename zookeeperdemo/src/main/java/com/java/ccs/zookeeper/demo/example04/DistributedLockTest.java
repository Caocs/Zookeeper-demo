package com.java.ccs.zookeeper.demo.example04;

import java.util.concurrent.TimeUnit;

/**
 * @author caocs
 * @date 2021/11/13
 */
public class DistributedLockTest {

    public static void main(String[] args) {
        DistributedLock lock01 = new DistributedLock();
        DistributedLock lock02 = new DistributedLock();

        Runnable task01 = () -> {
            lock01.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock01.unlock();
        };

        Runnable task02 = () -> {
            lock02.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock02.unlock();
        };

        for (int i = 0; i < 10; i++) {
            new Thread(task01, "server01-thread-" + i).start();
            new Thread(task02, "server02-thread-" + i).start();
        }
    }

}
