package com.java.juc.volatiles;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
class MyDatas {

    volatile int num = 0;

    public void addSelf() {
        num++; // i++;

    }

    AtomicInteger atomicInt = new AtomicInteger();

    public void addByAtomicSelf() {
        atomicInt.getAndIncrement(); //i++ 与上面保持一致。、
//        atomicInt.incrementAndGet(); // ++i;
    }
}
/**
 * 不保证原子性
 * 原子性：不可分割，完整性，即某个线程正在做某个具体业务的时候，不可以被打断，不可以被阻塞。
 * 要么同时成功，要么就是失败
 * <p>
 * 举一个自增的案例
 */
public class volatileAtomicDemo {

    public static void main(String[] args) {
        MyDatas myData = new MyDatas();
        int n =1000;
        int m =20;
        for (int i = 0; i < m; i++) {
            new Thread("Thread " + String.valueOf(i)) {
                @Override
                public void run() {
                    for (int j = 0; j <n; j++) {
                        myData.addSelf(); // m * n = 20 * 1000 =20000
                        myData.addByAtomicSelf();

                    }

                }
            }.start();
        }

        // 等待上面线程执行完成，通过main线程获取计算结果
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 2 是怎么来的 除了上述的20个线程 还有GC线程 main线程两个，while条件是针对上述的线程还没完成
        // 出让自己的时间片 交给其他线程
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        //main	  num 18173
        //main	  atomic value 20000
        //是不是加的操作被其他线程打断，以至于出现写被覆盖，从而导致写丢失，数据不安全。
        System.err.println(Thread.currentThread().getName() + "\t  num " + myData.num);
        System.err.println(Thread.currentThread().getName() + "\t atomic value " + myData.atomicInt);

    }


}


