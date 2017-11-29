package com.cityos.dg.service.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
public class DeleteTask {

  private final String tableName;

  private final DataSource dataSource;

  @Autowired
  public DeleteTask(String tableName, DataSource dataSource) {
    this.tableName = tableName;
    this.dataSource = dataSource;
  }

  public void delete() {
    String deleteSql = String
        .format("DELETE FROM %s WHERE StationID IN (SELECT TOP 100 StationID FROM %s "
                + "WHERE StationID IN (SELECT TOP 200 StationID FROM %s ORDER BY StationID DESC) ORDER BY StationID ASC)",
            tableName, tableName, tableName);
    Connection connection = null;
    PreparedStatement ps;
    try {
      connection = dataSource.getConnection();
      ps = connection.prepareStatement(deleteSql);
      ps.execute();
      log.info("删除倒数第100条至倒数低200条!");
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
