package com.yangqc.dg.service.tasks;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <p>title:</p> <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-02
 * @modified By yangqc
 */
@Component(value = "insertRunnable")
@Scope(value = "prototype")
@AllArgsConstructor
public class InsertRunnable implements Runnable {

  private final InsertTask insertTask;
  private final ForkJoinPool forkJoinPool;

  @Override
  public void run() {
    long t1 = System.currentTimeMillis();
    ForkJoinTask forkJoinTask = forkJoinPool.submit(insertTask);
    forkJoinTask.join();
    System.out
        .println("插入" + insertTask.getCount() + "条数据,最终耗时:" + (System.currentTimeMillis() - t1));
  }
}
