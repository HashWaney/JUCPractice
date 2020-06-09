package com.java.juc.volatiles;


import java.util.concurrent.TimeUnit;

/**
 * 1.验证volatile的可见性
 * 1.1 如果int num=0； 没有被添加volatile关键字修饰
 * 1.2 添加volatile 解决了可见性
 */
public class volatileDemo {

    public static void main(String[] args) {
        MyData myData = new MyData();

        // 第一个线程
        new Thread("t1") {
            @Override
            public void run() {
                System.err.println(Thread.currentThread().getName() + "\t come in");
                try {
                    TimeUnit.SECONDS.sleep(2);
                    myData.changeNum();
                    System.err.println(Thread.currentThread().getName() + "\t" + "value:" + myData.num);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // 第二线程是main线程
        while (myData.num == 0) {

        }
        System.err.println(Thread.currentThread().getName() + "\t over" + myData.num);

        // 未加volatile
        // t1	 come in
        //t1	value:30
        // 卡死


        // 加了volatile

//        t1	 come in
//        t1	value:30
//        main	 over30

        // 什么意思呢。也即是说通过用volatile修饰的共享变量，被线程修改了，会被立即刷新到主内存，其他线程操作的话会拿到最新的共享变量的值。

    }
}

class MyData {
    //     int num = 0; // 未被volatile修饰的共享变量
    volatile int num = 0;  // 可以保证可见性，及时通知其他线程，主物理内存的值已经被修改了

    public void changeNum() {
        this.num = 30;
    }

}
