package com.cityos.dg.service;

import com.cityos.dg.service.tasks.InfluxDBInsertRunnable;
import com.cityos.dg.service.tasks.InfluxDBInsertTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ForkJoinPool;

/**
 * <p>title:</p> <p>description:定时任务</p>
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

    @Autowired
    private InfluxInsertService influxInsertService;

    @Scheduled(initialDelay = 3000, fixedRate = 1000 * 60 * 24)
    public void timerRate() {
        int insertCount = 300000000;
        InfluxDBInsertTask influxDBInsertTask = applicationContext
                .getBean(InfluxDBInsertTask.class, insertCount,
                        influxInsertService, applicationContext);
        InfluxDBInsertRunnable insertRunnable = applicationContext
                .getBean(InfluxDBInsertRunnable.class, influxDBInsertTask, forkJoinPool);
        insertRunnable.run();
    }
    //每天20点16分50秒时执行
/*  @Scheduled(cron = "50 16 20 * * ?")
  public void timerCron() {
  }*/

    /**
     * 每晚23点执行操作 插入十万条数据
     */
/*  @Scheduled(cron = "0 0 23 * * ?")
  public void insertUser1() {
    int insertCount = 100000;
    InsertTask insertTask = applicationContext
        .getBean(InsertTask.class, "t_user1", insertCount,
            jdbcTemplate.getDataSource());
    InsertRunnable insertRunnable = applicationContext
        .getBean(InsertRunnable.class, insertTask, forkJoinPool);
    insertRunnable.run();

  }

  *//**
     * 每晚22点执行操作 插入一百万条数据
     *//*
  @Scheduled(cron = "0 0 22 * * ?")
  public void insertUser2() {
    int insertCount = 1000000;
    InsertTask insertTask = applicationContext
        .getBean(InsertTask.class, "t_user2", insertCount,
            jdbcTemplate.getDataSource());
    InsertRunnable insertRunnable = applicationContext
        .getBean(InsertRunnable.class, insertTask, forkJoinPool);
    insertRunnable.run();

  }

  *//**
     * 每晚21点执行操作 插入一千万条数据
     *//*
  @Scheduled(cron = "0 0 21 * * ?")
  public void insertUser3() {
    int insertCount = 10000000;
    InsertTask insertTask = applicationContext
        .getBean(InsertTask.class, "t_user3", insertCount,
            jdbcTemplate.getDataSource());
    InsertRunnable insertRunnable = applicationContext
        .getBean(InsertRunnable.class, insertTask, forkJoinPool);
    insertRunnable.run();

  }*/

}
