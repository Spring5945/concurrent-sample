package com.zhengyu.concurrent.component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CountDownLatch
 * A synchronization aid that allows one or more threads to wait until
 * a set of operations being performed in other threads completes.
 */
public class MyCountDownLatch {

    public static void main(String[] args) throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(5);

        AtomicInteger threadNo = new AtomicInteger(1);

        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                5, 10, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(5),
                r -> new Thread(r, "ThreadPool thread: " + threadNo.getAndIncrement()),
                (r, executor) -> {
                    System.out.println("拒绝策略报警");
                    throw new RuntimeException("线程池已满,拒绝策略报警");
                }
        );

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "开始执行");
                    Thread.sleep(2000);
                    System.out.println("线程" + Thread.currentThread().getName() + "执行完毕");
                    countDownLatch.countDown();
                } catch (Exception ex) {
                }
            });
        }

        countDownLatch.await(1000,TimeUnit.MILLISECONDS);
        System.out.println("主线程等待其他线程执行完毕,到达终点");
    }
}
