package com.zhengyu.threadlocal;

import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalOps {
    public static void main(String[] args) {


//        threadLocalWithForkThread();
        threadLocalWithThreadPool();


    }

    private static void threadLocalWithForkThread() {
        LanguageTransmittableThreadLocal.setLanguageContext(new LanguageContext.Builder().language("en-us").code("EN").locale("en-hk").build());
        new Thread(() -> System.out.println(LanguageTransmittableThreadLocal.getLanguageContext())).start();
    }

    private static void threadLocalWithThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = initialThreadPool();
//            if (finalI % 2 == 0) {
//                LanguageTransmittableThreadLocal.setLanguageContext(new LanguageContext.Builder().language("en-us").code("EN").locale("en-hk").build());
//            }
//            CompletableFuture.runAsync(() -> {
//                System.out.println(finalI + "s1" + LanguageTransmittableThreadLocal.getLanguageContext());
//                CompletableFuture.runAsync(() -> {
//                    System.out.println(finalI + "s2" + LanguageTransmittableThreadLocal.getLanguageContext());
//                }, threadPoolExecutor);
//            }, threadPoolExecutor);
//            LanguageTransmittableThreadLocal.remove();

        Executor ttlExecutor = TtlExecutors.getTtlExecutor(threadPoolExecutor);

        for (int i = 0; i < 2; i++) {
            LanguageTransmittableThreadLocal.setLanguageContext(new LanguageContext.Builder().language("en-us" + i).code("EN").locale("en-hk").build());

            CompletableFuture.runAsync(() -> {
                System.out.println("t1-" + LanguageTransmittableThreadLocal.getLanguageContext());

            }, ttlExecutor);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            CompletableFuture.runAsync(() -> {
                System.out.println("t2-" + LanguageTransmittableThreadLocal.getLanguageContext());
            }, ttlExecutor);

            System.out.println("main-" + i + LanguageTransmittableThreadLocal.getLanguageContext());
//            LanguageInheritableThreadLocal.remove();

        }


//        for (int i = 0; i < 2; i++) {
//            int finalI = i;
//            new Thread(() -> {
//                if (finalI % 2 == 0) {
//                    LanguageThreadLocal.setLanguageContext(new LanguageContext.Builder().language("en-us").code("EN").locale("en-hk").build());
//                }
//                System.out.println(LanguageThreadLocal.getLanguageContext());
//            }).start();
//
//        }
    }

    private static ThreadPoolExecutor initialThreadPool() {
        AtomicInteger threadNo = new AtomicInteger(1);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                r -> {
                    Thread thread = new Thread(r, "ThreadPool thread: " + threadNo.getAndIncrement());
                    thread.setDaemon(false);
                    System.out.println("create thread-" + thread);
                    return thread;
                },
                (r, executor) -> {
                    System.out.println("拒绝策略报警");
                    throw new RuntimeException("线程池已满,拒绝策略报警");
                }
        );
        return threadPoolExecutor;
    }
}
