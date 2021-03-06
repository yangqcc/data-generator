package com.yangqc.dg.service;

import com.yangqc.dg.service.tasks.DeleteTask;
import com.yangqc.dg.service.tasks.InsertRunnable;
import com.yangqc.dg.service.tasks.InsertTask;
import com.yangqc.dg.service.tasks.UpdateTask;
import java.util.concurrent.ForkJoinPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
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

  private static final String TABLE_NAME = "dbo.EC_Station";

  /**
   * 每小时执行操作 插入一百五十条数据
   */
//  @Scheduled(initialDelay = 1000, fixedRate = 1000 * 60 * 5)
  public void insertUser1() {
    int insertCount = 200;
    InsertTask insertTask = applicationContext
        .getBean(InsertTask.class, TABLE_NAME, insertCount,
            jdbcTemplate.getDataSource());
    InsertRunnable insertRunnable = applicationContext
        .getBean(InsertRunnable.class, insertTask, forkJoinPool);
    insertRunnable.run();

  }

  /**
   * 每小时跟新一百条数据
   */
//  @Scheduled(initialDelay = 1000 * 60 * 2, fixedRate = 1000 * 60 * 5)
  public void update() {
    UpdateTask updateTask = applicationContext
        .getBean(UpdateTask.class, TABLE_NAME,
            jdbcTemplate.getDataSource());
    updateTask.update();
  }

  /**
   * 每小时删除一百条数据
   */
//  @Scheduled(initialDelay = 1000 * 60 * 4, fixedRate = 1000 * 60 * 5)
  public void delete() {
    DeleteTask deleteTask = applicationContext
        .getBean(DeleteTask.class, TABLE_NAME,
            jdbcTemplate.getDataSource());
    deleteTask.delete();
  }

}
