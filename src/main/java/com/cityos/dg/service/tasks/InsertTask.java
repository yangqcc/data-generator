package com.cityos.dg.service.tasks;

import com.cityos.dg.utils.ExtraRandom;
import com.cityos.dg.utils.RandomChinese;
import com.cityos.dg.utils.RandomPhoneNum;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.RecursiveAction;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <p>title:</p> <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-02
 * @modified By yangqc
 */
@Component
@Scope(value = "prototype")
@Slf4j
public class InsertTask extends RecursiveAction {

  private static final Random random = new Random(20);

  @Getter
  private final int count;

  private final String tableName;

  private final int executeBatchCount = 2000;

  private final int threshold;

  private final DataSource dataSource;

  @Autowired
  public InsertTask(String tableName, int count, DataSource dataSource) {
    if (count <= 0) {
      throw new IllegalArgumentException("count不能小于0!");
    }
    this.tableName = tableName;
    this.count = count;
    this.dataSource = dataSource;
    this.threshold = (count - 1) / Runtime.getRuntime().availableProcessors() + 1;
  }

  /**
   * 插入数量
   */
  private InsertTask(String tableName, int count, int threshold, DataSource dataSource) {
    if (count <= 0) {
      throw new IllegalArgumentException("count不能小于0!");
    }
    this.tableName = tableName;
    this.count = count;
    this.threshold = threshold;
    this.dataSource = dataSource;
  }

  /**
   * 获取随机年龄
   */
  private int getRandomAge() {
    return random.nextInt(30);
  }

  public void insert() {
    log.info("开始插入:" + count);
    long startTime = System.currentTimeMillis();
    Connection connection = null;
    PreparedStatement ps = null;
    int i = 0;
    String sql = String.format(
        "insert into %s (PositionName,Grade,Address,Longitude,Latitude,SystemDateTime,RegionID,"
            + "Sequence,区域内码,区域计算,对外公开,异常短信,Longitude_BD,Latitude_BD) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
        tableName);
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      ps = connection.prepareStatement(sql);
      for (; i < count; i++) {
        ps.setString(1, RandomChinese.nextString(10));
        ps.setInt(2, random.nextInt(10));
        ps.setString(3, RandomChinese.nextString(10));
        ps.setBigDecimal(4, new BigDecimal(random.nextInt(1000)));
        ps.setBigDecimal(5, new BigDecimal(random.nextInt(1000)));
        ps.setDate(6, new java.sql.Date(System.currentTimeMillis()));
        ps.setInt(7, random.nextInt());
        ps.setInt(8, random.nextInt());
        ps.setString(9, ExtraRandom.nextString(10, "letter"));
        ps.setBoolean(10, true);
        ps.setBoolean(11, false);
        ps.setString(12, RandomPhoneNum.nextPhoneNum());
        ps.setBigDecimal(13, new BigDecimal(random.nextInt()));
        ps.setBigDecimal(14, new BigDecimal(random.nextInt()));
        ps.addBatch();
        if (i % executeBatchCount == 0) {
          ps.executeBatch();
          connection.commit();
        }
      }
      ps.executeBatch();
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    } finally {
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    long endTime = System.currentTimeMillis();
    log.info(
        "插入:" + count + ", 耗时:" + (endTime - startTime) + ", 线程:" + Thread.currentThread()
            .getName());
  }

  @Override
  protected void compute() {
    if (count <= threshold) {
      this.insert();
    } else {
      int middle = (count - 1) / 2 + 1;
      InsertTask leftTask = new InsertTask(tableName, middle, threshold, dataSource);
      InsertTask rightTask = new InsertTask(tableName, middle, threshold, dataSource);
      invokeAll(leftTask, rightTask);
      if (leftTask.isCompletedNormally() && rightTask.isCompletedNormally()) {
        return;
      } else {
        throw new RuntimeException("出错！");
      }
    }
  }

}
