package com.java.ccs.zookeeper.demo.example03;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * @author caocs
 * @date 2021/11/13
 */
public class DistributedLockTest {

    /**
     * 模拟两个线程争抢分布式锁
     * 注意：
     * （1）每个DistributedLock只能被使用一次，因为内部通过CountDownLatch计数。
     */
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        // 创建分布式锁 1
        final DistributedLock lock1 = new DistributedLock();
        // 创建分布式锁 2
        final DistributedLock lock2 = new DistributedLock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    lock1.zkLock();
                    System.out.println("线程 1 获取锁");
                    Thread.sleep(5 * 1000);
                    lock1.zkUnlock();
                    System.out.println("线程 1 释放锁");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    lock2.zkLock();
                    System.out.println("线程 2 获取锁");
                    Thread.sleep(5 * 1000);
                    lock2.zkUnlock();
                    System.out.println("线程 2 释放锁");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

