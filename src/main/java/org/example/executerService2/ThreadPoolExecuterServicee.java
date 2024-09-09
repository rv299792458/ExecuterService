package org.example.executerService2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolExecuterServicee{


    private class Work extends ReentrantLock implements  Runnable{
        private  String name;
        private Runnable runnable;

        Work(Runnable runnable, String name){
            this.name = name;
            this.runnable=runnable;

        }

        public void run(){
            runningThread.getAndIncrement();
            while(true){
                Work work = null;
                if(runnable!=null || ( work = getTask())!=null) {
                    this.lock();
                    if(work!=null)
                    {
                        runnable = work.runnable;
                        name = work.name;
                    }
                    runnable.run();
                    System.out.println(name + "is running");
                    try {
                       Thread.sleep(1000);
                        System.out.println("try");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        runnable = null;
                    }
                    this.unlock();

                }else{
                    runningThread.getAndDecrement();
                    System.out.println("tasks are emptied for name "+name);
                    break;
                }
            }
        }

        Work getTask(){
            return workQueue.poll();
        }

    }
    private final BlockingQueue<Work> workQueue;
    private final AtomicInteger runningThread;
    private final int corePoolSize;
    private final int maxPoolSize;

    public ThreadPoolExecuterServicee( int corePoolSize, int maxPoolSize, BlockingQueue<Work> workQueue) {
        this.workQueue = workQueue;
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        runningThread = new AtomicInteger(0);
    }

    public void submit(Runnable task, String name){

        System.out.println("current size -> "+runningThread.get());
        if(runningThread.get() < corePoolSize)
        {
            Work work = new Work(task, name);
            Thread t = new Thread(work);
            t.start();
            return ;
        }
        if(runningThread.get() < maxPoolSize){
            Work work = new Work(task, name);
            workQueue.add(work);
            return;
        }
        if(runningThread.get() > maxPoolSize){
            Work work = new Work(task, name);
            Thread t = new Thread(work);
            t.start();
        }

    }
}


