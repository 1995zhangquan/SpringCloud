package com.cloud.thread;

import java.util.concurrent.*;

public class TicketCallable implements Callable {

    private int ticket = 100;

    @Override
    public Object call() throws Exception {
        while (ticket > 0) {
            System.out.println("当前线程：" + Thread.currentThread().getName() + " 卖票，票号为：" + ticket);
            ticket--;
        }
        return "执行结束";
    }
}
class TestCallable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TicketCallable ticketCallable = new TicketCallable(); //创建线程类对象
        FutureTask futureTask = new FutureTask(ticketCallable); //创建FutureTask对象
        //start() 是用于启动线程，而 run() 是线程启动后实际执行的方法体。
        new Thread(futureTask).start(); //将FutureTask类的实例注册进入Thread中运行，
        FutureTask futureTask1 = new FutureTask(ticketCallable); //创建FutureTask对象
        new Thread(futureTask1).start();
        FutureTask futureTask2 = new FutureTask(ticketCallable); //创建FutureTask对象
        new Thread(futureTask2).start();

        System.out.println(futureTask.get());

    }
}

class TicketRunnable implements Runnable {
    private int ticket = 100;

    @Override
    public void run() {
        while (ticket > 0) {
            System.out.println("当前线程：" + Thread.currentThread().getName() + " 卖票，票号为：" + ticket);
            ticket--;
        }
    }
}

class TestExecutors {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //会出现重复买票行为
//        TicketRunnable ticketRunnable1 = new TicketRunnable();
//        new Thread(ticketRunnable1, "窗口1").start(); //启动线程
//        TicketRunnable ticketRunnable2 = new TicketRunnable();
//        new Thread(ticketRunnable2, "窗口2").start(); //启动线程
        //改进
        //Executors：工具类、线程池的工厂类，用于创建并返回不同类型的线程池，提供了如下静态方法
//        Executors.newCachedThreadPool();//创建一个可根据需要创建新线程的线程池
//        Executors.newFixedThreadPool(n);//创建一个可重用固定线程数的线程池
//        Executors.newSingleThreadExecutor();//创建一个只有一个线程的线程池
//        Executors.newScheduledThreadPool(n);//创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行
        ExecutorService es = Executors.newFixedThreadPool(5);
        //调用submit方法
        Future submit = es.submit(new TicketCallable());
//        Future submit2 = es.submit(new TicketCallable());
//        System.out.println(submit.get());
        System.out.println(submit.get());
        //es.submit(new TicketRunnable());

    }
}