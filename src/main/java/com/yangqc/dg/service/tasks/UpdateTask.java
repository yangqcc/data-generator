package com.yangqc.dg.service.tasks;

import com.yangqc.dg.utils.ExtraRandom;
import com.yangqc.dg.utils.RandomChinese;
import com.yangqc.dg.utils.RandomPhoneNum;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * <p>title:</p> <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-29
 * @modified By yangqc
 */
@Service
@Scope(value = "prototype")
@Slf4j
public class UpdateTask {

  private static final Random random = new Random(20);

  private final int executeBatchCount = 2000;

  private final String tableName;

  private final DataSource dataSource;

  @Autowired
  public UpdateTask(String tableName, DataSource dataSource) {
    this.tableName = tableName;
    this.dataSource = dataSource;
  }

  public void update() {
    int maxId = -1;
    String selectSql = String
        .format("SELECT top 100 StationID FROM %s order by StationID desc ", tableName);
    String updateSql =
        String.format(
            "update %s SET PositionName=?,Grade=?,Address=?,Longitude=?,Latitude=?,SystemDateTime=?,RegionID=?,"
                + "Sequence=?,区域内码=?,区域计算=?,对外公开=?,异常短信=?,Longitude_BD=?,Latitude_BD=? where StationID = ?",
            tableName);
    Connection connection = null;
    PreparedStatement ps2;
    PreparedStatement ps1;
    int i = 0;
    try {
      connection = dataSource.getConnection();
      ps1 = connection.prepareStatement(selectSql);
      ResultSet rs = ps1.executeQuery();
      connection.setAutoCommit(false);
      ps2 = connection.prepareStatement(updateSql);
      while (rs.next()) {
        ps2.setString(1, RandomChinese.nextString(10));
        ps2.setInt(2, random.nextInt(10));
        ps2.setString(3, RandomChinese.nextString(10));
        ps2.setBigDecimal(4, new BigDecimal(random.nextInt(1000)));
        ps2.setBigDecimal(5, new BigDecimal(random.nextInt(1000)));
        ps2.setDate(6, new java.sql.Date(System.currentTimeMillis()));
        ps2.setInt(7, random.nextInt());
        ps2.setInt(8, random.nextInt());
        ps2.setString(9, ExtraRandom.nextString(10, "letter"));
        ps2.setBoolean(10, true);
        ps2.setBoolean(11, false);
        ps2.setString(12, RandomPhoneNum.nextPhoneNum());
        ps2.setBigDecimal(13, new BigDecimal(random.nextInt()));
        ps2.setBigDecimal(14, new BigDecimal(random.nextInt()));
        maxId = rs.getInt(1);
        ps2.setInt(15, maxId);
        ps2.addBatch();
        if (i % executeBatchCount == 0) {
          ps2.executeBatch();
          connection.commit();
        }
      }
      ps2.executeBatch();
      connection.commit();
      connection.setAutoCommit(true);
      log.info("更新100条,最大ID为:{}", maxId);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
