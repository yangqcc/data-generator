package com.cityos.dg.service;

import com.cityos.dg.service.tasks.InfluxDBInsertRunnable;
import com.cityos.dg.service.tasks.InfluxDBInsertTask;
import java.util.concurrent.ForkJoinPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>title:</p> <p>description:定时任务</p>
 *
 * @author yangqc
 * @date Created in 2017-11-03
 * @modified By yangqc
 */
@Component
public class InfluxdbOperateTimer {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ForkJoinPool forkJoinPool;

  @Autowired
  private InfluxInsertService influxInsertService;

  @Scheduled(initialDelay = 3000, fixedRate = 1000 * 60 * 24)
  public void timerRate() {
    int insertCount = 30000;
    InfluxDBInsertTask influxDBInsertTask = applicationContext
        .getBean(InfluxDBInsertTask.class, insertCount,
            influxInsertService, applicationContext);
    InfluxDBInsertRunnable insertRunnable = applicationContext
        .getBean(InfluxDBInsertRunnable.class, influxDBInsertTask, forkJoinPool);
    insertRunnable.run();
  }

}
