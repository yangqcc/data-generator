package com.cityos.service;

import com.cityos.utils.ExtraRandom;
import com.cityos.utils.RandomChinese;
import com.cityos.utils.RandomEmail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

/**
 * <p>title:</p> <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-02
 * @modified By yangqc
 */
public class InsertTask extends RecursiveAction {


  private final DbInfo dbInfo;

  private static final Random random = new Random(20);

  private final int count;

  private final String tableName;

  private final int executeBatchCount = 2000;

  private final int threshold;

  private long timeCost;

  public InsertTask(String tableName, int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("count不能小于0!");
    }
    this.tableName = tableName;
    this.count = count;
    this.threshold = (count - 1) / Runtime.getRuntime().availableProcessors() + 1;
    dbInfo = new DbInfo();
  }

  /**
   * 插入数量
   */
  private InsertTask(String tableName, int count, int threshold) {
    if (count <= 0) {
      throw new IllegalArgumentException("count不能小于0!");
    }
    this.tableName = tableName;
    this.count = count;
    this.threshold = threshold;
    dbInfo = new DbInfo();
  }

  /**
   * 获取随机年龄
   */
  private int getRandomAge() {
    return random.nextInt(30);
  }

  public void insert() {
    System.out.println("开始插入" + count);
    long startTime = System.currentTimeMillis();
    Date date = new Date();
    PreparedStatement ps = null;
    int i = 0;
    Connection connection = dbInfo.getConnection();
    String sql = String.format(
        "insert into %s (chsname,enname,birthday,age,weight,salary,sex,email) values(?,?,?,?,?,?,?,?)",
        tableName);
    try {
      connection.setAutoCommit(false);
      ps = connection.prepareStatement(sql);
      for (; i < count; i++) {
        ps.setString(1, RandomChinese.nextString(10));
        ps.setString(2, ExtraRandom.nextString(20, "letter"));
        ps.setDate(3, new java.sql.Date(date.getTime()));
        ps.setInt(4, getRandomAge());
        ps.setFloat(5, random.nextFloat());
        ps.setDouble(6, Math.random() + 12);
        ps.setBoolean(7, true);
        ps.setString(8, RandomEmail.nextEmail());
        ps.addBatch();
        if (i % executeBatchCount == 0) {
          ps.executeBatch();
          connection.commit();
        }
      }
      ps.executeBatch();
      connection.commit();
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
    System.out.println(
        "插入:" + count + ", 耗时:" + (endTime - startTime) + ", 线程:" + Thread.currentThread()
            .getName());
    timeCost = endTime - startTime;
  }

  @Override
  protected void compute() {
    if (count <= threshold) {
      this.insert();
    } else {
      int middle = (count - 1) / 2 + 1;
      InsertTask leftTask = new InsertTask(tableName, middle, threshold);
      InsertTask rightTask = new InsertTask(tableName, middle, threshold);
      invokeAll(leftTask, rightTask);
      if (leftTask.isCompletedNormally() && rightTask.isCompletedNormally()) {
        return;
      } else {
        throw new RuntimeException("出处！");
      }
    }
  }

}
