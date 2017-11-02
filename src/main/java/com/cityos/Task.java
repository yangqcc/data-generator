package com.cityos;

import com.cityos.service.InsertTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>title:</p> <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-02
 * @modified By yangqc
 */
public class Task {

  public static void main(String[] args) {
    ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
    // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
    InsertTask insertTask1 = new InsertTask("t_user1", 100000);
    InsertTask insertTask2 = new InsertTask("t_user2", 1000000);
    InsertTask insertTask3 = new InsertTask("t_user3", 10000000);
//    service.scheduleAtFixedRate(insertTask1, 10, 1, TimeUnit.SECONDS);
    service.scheduleAtFixedRate(insertTask3, 1, 10, TimeUnit.MINUTES);
  }
}
