package com.zhengyu.threadpool;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPool {
    public static void main(String[] args) throws Exception {

        AtomicInteger threadNo = new AtomicInteger(1);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 10, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(5),
                r -> new Thread(r, "ThreadPool thread: " + threadNo.getAndIncrement()),
                (r, executor) -> System.out.println("拒绝策略报警")
        );

        List<CompletableFuture<Integer>> completableFutureList = Lists.newArrayList();

        for (int i = 0; i < 15; i++) {
            CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(200);
                    System.out.println(Thread.currentThread().getName() + " run");
                    Random random = new Random();
                    int i1 = random.nextInt();
                    return i1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }, threadPoolExecutor);

            completableFutureList.add(integerCompletableFuture);
            System.out.println("核心线程数" + threadPoolExecutor.getCorePoolSize());
            System.out.println("线程池数" + threadPoolExecutor.getPoolSize());
            System.out.println("队列任务数" + threadPoolExecutor.getQueue().size());
        }

        CompletableFuture[] completableFutures = completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]);

        CompletableFuture.allOf(completableFutures).join();

        for (CompletableFuture future : completableFutures) {
            System.out.println(future.get());
        }

        Thread.sleep(1000);
        System.out.println("task结束之后线程池数" + threadPoolExecutor.getPoolSize());
    }
}
