package org.example;

import org.example.executerService2.ThreadPoolExecuterServicee;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {

        System.out.println("Start");

        ThreadPoolExecuterServicee ser = new ThreadPoolExecuterServicee(10,10,new LinkedBlockingQueue<>());
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            ser.submit(()->{
                System.out.println("task is running"+ finalI);

            }, "Thread-"+i);
        }


        System.out.println("end");
    }
}