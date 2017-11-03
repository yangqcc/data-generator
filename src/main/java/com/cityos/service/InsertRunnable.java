package com.cityos.service;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * <p>title:</p> <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-02
 * @modified By yangqc
 */
public class InsertRunnable implements Runnable {

  private final InsertTask insertTask;
  private final ForkJoinPool forkJoinPool;

  public InsertRunnable(InsertTask insertTask, ForkJoinPool forkJoinPool) {
    this.insertTask = insertTask;
    this.forkJoinPool = forkJoinPool;
  }

  @Override
  public void run() {
    long t1 = System.currentTimeMillis();
    ForkJoinTask forkJoinTask = forkJoinPool.submit(insertTask);
    forkJoinTask.join();
    System.out.println(System.currentTimeMillis() - t1 + ",终极耗时!");
  }
}
