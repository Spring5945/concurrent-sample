package com.zhengyu.threadlocal;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalOps {
    public static void main(String[] args) {


//        threadLocalWithForkThread();
        threadLocalWithThreadPool();


    }

    private static void threadLocalWithForkThread() {
        LanguageThreadLocal.setLanguageContext(new LanguageContext.Builder().language("en-us").code("EN").locale("en-hk").build());
        new Thread(() -> System.out.println(LanguageThreadLocal.getLanguageContext())).start();
    }

    private static void threadLocalWithThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = initialThreadPool();
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                if (finalI % 2 == 0) {
                    LanguageThreadLocal.setLanguageContext(new LanguageContext.Builder().language("en-us").code("EN").locale("en-hk").build());
                }
                System.out.println(LanguageThreadLocal.getLanguageContext());
                LanguageThreadLocal.remove();
            }, threadPoolExecutor);

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
