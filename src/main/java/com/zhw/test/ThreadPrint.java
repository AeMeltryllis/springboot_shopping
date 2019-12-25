package com.zhw.test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 创建两个线程，其中一个输出1-52，另外一个输出A-Z。输出格式要求：12A 34B 56C 78D ...
 */
public class ThreadPrint {
    private static Object sync = new Object() ;

    static class PrintNum extends Thread{
        private Object object  = new Object();
        @Override
        public   void run() {
            synchronized (object){
                for (int i = 1; i <=52 ; i++) {
                    System.out.println(i);
                    if (i%2==0){
                        try {
                            object.notifyAll();
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }}
        }
    }

    static class PrintChar implements Runnable {
        private Object object  = new Object();
        @Override
        public  void run(){
                synchronized (object){
                for (char i = 'a'; i <='z' ; i++) {
                    System.out.println(i);
                    System.out.println(" ");
                    object.notifyAll();
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }}

        }
    }

    public static void main(String[] args) throws Exception {
        Thread num = new PrintNum();
        num.start();
        num.join();
        new Thread(new PrintChar()).start();



    }
}
