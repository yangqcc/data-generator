package com.cityos.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <p>title:</p> <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-02
 * @modified By yangqc
 */
public class DbInfo {

  private String filePath = "/";
  private String propertiesName = "databaseinfo.properties";
  private final String driver;
  private final String path;
  private final String instance;
  private final String url;
  private final String username;
  private final String password;

  public DbInfo() {
    Properties pro = new Properties();
    InputStream in;
    try {
      in = this.getClass().getResourceAsStream(filePath + propertiesName);
      pro.load(in);
      in.close();
      driver = pro.getProperty("dbDriver");
      path = pro.getProperty("dbPath");
      instance = pro.getProperty("dbInstance");
      url = String.format("jdbc:sqlserver://%s;DatabaseName=%s", path, instance);
      username = pro.getProperty("dbUserName");
      password = pro.getProperty("dbPassword");
    } catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * 获取数据连接
   */
  public Connection getConnection() {
    Connection conn;
    try {
      //classLoader,加载对应驱动
      Class.forName(driver);
      conn = DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
    return conn;
  }
}
