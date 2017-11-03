package com.cityos.dg.service;

import com.cityos.dg.service.tasks.InsertRunnable;
import com.cityos.dg.service.tasks.InsertTask;
import java.util.concurrent.ForkJoinPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>title:</p>
 * <p>description:定时任务</p>
 *
 * @author yangqc
 * @date Created in 2017-11-03
 * @modified By yangqc
 */
@Component
public class DataBaseOperateTimer {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ForkJoinPool forkJoinPool;

/*  @Scheduled(fixedRate = 10000)
  public void timerRate() {

  }*/

  //每天20点16分50秒时执行
/*  @Scheduled(cron = "50 16 20 * * ?")
  public void timerCron() {
  }*/

  /**
   * 第一次延迟1秒执行，当执行完后24小时再执行
   */
  //TODO 暂时十秒执行一次
  @Scheduled(initialDelay = 1000, fixedDelay = 10000)
  public void insertUser1() {
    int insertCount = 100000;
    InsertTask insertTask = applicationContext
        .getBean(InsertTask.class, "t_user1", insertCount,
            jdbcTemplate.getDataSource());
    InsertRunnable insertRunnable = applicationContext
        .getBean(InsertRunnable.class, insertTask, forkJoinPool);
    insertRunnable.run();

  }

  /**
   * 第一次延迟10分钟，执行完后24小时再执行
   */
  @Scheduled(initialDelay = 1000 * 60 * 10, fixedDelay = 1000 * 60 * 24)
  public void insertUser2() {
    int insertCount = 1000000;
    InsertTask insertTask = applicationContext
        .getBean(InsertTask.class, "t_user2", insertCount,
            jdbcTemplate.getDataSource());
    InsertRunnable insertRunnable = applicationContext
        .getBean(InsertRunnable.class, insertTask, forkJoinPool);
    insertRunnable.run();

  }

  /**
   * 第一次延迟10分钟，执行完成后24小时再执行
   */
  @Scheduled(initialDelay = 1000 * 60 * 10, fixedDelay = 1000 * 60 * 24)
  public void insertUser3() {
    int insertCount = 10000000;
    InsertTask insertTask = applicationContext
        .getBean(InsertTask.class, "t_user3", insertCount,
            jdbcTemplate.getDataSource());
    InsertRunnable insertRunnable = applicationContext
        .getBean(InsertRunnable.class, insertTask, forkJoinPool);
    insertRunnable.run();

  }

}
