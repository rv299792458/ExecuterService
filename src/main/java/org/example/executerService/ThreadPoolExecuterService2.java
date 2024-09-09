package org.example.executerService;

import com.sun.istack.internal.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolExecuterService2 extends AbstractExecuterService2{

    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    private volatile int corePoolSize;
    private volatile int maxPoolSize;

    private volatile long keepAliveTime;

    private final ReentrantLock mainLock = new ReentrantLock();
    private final BlockingQueue<Runnable> workQueue;


    private static int ctlOf(int rs, int wc) { return rs | wc; }
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }



    @Override
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();

        int currentCtl = ctl.get();

        if(workerCountOf(currentCtl) < corePoolSize ){
            addWorker(command, true);
        }else if(workQueue.offer(command)){
            return;
        }else{
            System.out.println("This task is rejected");
        }


    }

    public ThreadPoolExecuterService2( int corePoolSize, int maxPoolSize, long keepAliveTime, BlockingQueue<Runnable> workQueue ){

        if (corePoolSize < 0 ||
                maxPoolSize <= 0 ||
                maxPoolSize < corePoolSize ||
                keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null)
            throw new NullPointerException();

        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.workQueue = workQueue;

    }

    public boolean addWorker(Runnable command, boolean core){

        int c = ctl.get();
        int rs = runStateOf(c);
        if(rs >= SHUTDOWN || command==null){
            return false;
        }
        int wc = workerCountOf(c);
        if (wc >= CAPACITY || wc >= (core ? corePoolSize : maxPoolSize))
            return false;

        Worker w = new Worker(command);

        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            w.thread.start();
        }finally {
            mainLock.unlock();
            return true;
        }

    }

    private final class Worker implements  Runnable{

        Thread thread;
        Runnable runnable;

        @Override
        public void run() {
            runnable.run();
        }

        Worker(Runnable task){
            runnable = task;
            thread = new Thread();

        }
    }






}
