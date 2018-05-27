package com.zhengyu.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MyThreadPool3 {
    public static void main(String[] args) {


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ExecutorService executorService1 = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();

        executorService.submit(()->{
            System.out.println("helloworld");
        });
    }
}
