package com.zhengyu.classloader;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPool2 {
    public static void main(String[] args) throws Exception{


        AtomicInteger threadNo = new AtomicInteger(1);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 10, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(5),
                r -> new Thread(r, "ThreadPool thread: " + threadNo.getAndIncrement()),
                (r, executor) -> System.out.println("拒绝策略报警")
        );

        CountDownLatch countDownLatch = new CountDownLatch(16);

        List<Future<Integer>> futureList = Lists.newArrayList();

        for (int i = 0; i < 16; i++) {
            Future<Integer> integerFuture = threadPoolExecutor.submit(() -> {
                try {
                    Thread.sleep(200);
                    Random random = new Random();
                    System.out.println(Thread.currentThread().getName() + " run");
                    int i1 = random.nextInt();
                    return i1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            });
            futureList.add(integerFuture);
            countDownLatch.countDown();

            System.out.println("核心线程数" + threadPoolExecutor.getCorePoolSize());
            System.out.println("线程池数" + threadPoolExecutor.getPoolSize());
            System.out.println("队列任务数" + threadPoolExecutor.getQueue().size());
        }


        countDownLatch.await();

        for (Future future : futureList) {
            System.out.println(future.get());
        }

        Thread.sleep(1000);
        System.out.println("task结束之后线程池数" + threadPoolExecutor.getPoolSize());

    }
}
