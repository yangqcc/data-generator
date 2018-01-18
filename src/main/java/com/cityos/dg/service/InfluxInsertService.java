package com.cityos.dg.service;

import com.cityos.dg.config.ApplicationProperties;
import com.cityos.dg.config.InfluxDBConstants;
import com.cityos.dg.utils.ExtraRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author yangqc
 */
@Service
@Scope(scopeName = "prototype")
@Slf4j
public class InfluxInsertService {

  //每次提交数量
  private final int batchCount = 250000;

  private InfluxDB influxDB;

  @Autowired
  private ApplicationProperties applicationProperties;

  private long sum = 0;

  /**
   * 保存数据，采用默认数据保留策略(默认保留 168h)
   */
  public void insert(Map<String, String> tags, List<Map<String, Object>> valueMapList) {
    Assert.notEmpty(valueMapList);
    BatchPoints.Builder builder = BatchPoints.database(InfluxDBConstants.DB_NAME);
    if (tags != null) {
      for (Map.Entry<String, String> entry : tags.entrySet()) {
        builder = builder.tag(entry.getKey(), entry.getValue());
      }
    }
    BatchPoints batchPoints = builder.build();
    for (int i = 0; i < valueMapList.size(); i++) {
      Point.Builder pointBuilder = Point.measurement(InfluxDBConstants.MEASUREMENT_NAME);
      pointBuilder.fields(valueMapList.get(i)).time(System.nanoTime(), TimeUnit.NANOSECONDS);
      Point point = pointBuilder.build();
      batchPoints.point(point);
      if (i % batchCount == 0) {
        getInfluxDBConnection().write(batchPoints);
        batchPoints = builder.build();
      }
    }
    if (batchPoints.getPoints().size() > 0) {
      getInfluxDBConnection().write(batchPoints);
    }
    sum += valueMapList.size();
    log.debug("已写入" + sum + "条数据!");
    valueMapList.clear();
  }

  public void insert(int count) {
    InfluxDB influxDB = getInfluxDBConnection();
    BatchPoints.Builder builder = BatchPoints.database(InfluxDBConstants.DB_NAME);
    BatchPoints batchPoints = builder.build();
    for (int i = 0; i < count; i++) {
      Point.Builder pointBuilder = Point.measurement(InfluxDBConstants.MEASUREMENT_NAME);
      pointBuilder.time(System.nanoTime(), TimeUnit.NANOSECONDS);
      pointBuilder.addField("veh_license", ExtraRandom.nextString(30, "letter"));
      pointBuilder.addField("bus_line_id", ExtraRandom.nextString(9, "digit"));
      pointBuilder.addField("ori_flag", "2");
      pointBuilder.addField("run_state", "2");
      pointBuilder.addField("station_id", ExtraRandom.nextString(9, "digit"));
      pointBuilder.addField("gps_report_dt", System.currentTimeMillis());
      Point point = pointBuilder.build();
      batchPoints.point(point);
      if (i % batchCount == 0) {
        influxDB.write(batchPoints);
        influxDB.close();
        influxDB = getInfluxDBConnection();
        builder = BatchPoints.database(InfluxDBConstants.DB_NAME);
        batchPoints = builder.build();
        log.info("{},写入！", Thread.currentThread().getName());
      }
    }
    if (batchPoints.getPoints().size() > 0) {
      getInfluxDBConnection().write(batchPoints);
    }
  }

  private InfluxDB getInfluxDBConnection() {
    if (influxDB == null) {
      influxDB = InfluxDBFactory
          .connect(applicationProperties.getInfluxDBUrl(), InfluxDBConstants.USER_NAME,
              InfluxDBConstants.PASSWORD);
      if (!influxDB.databaseExists(InfluxDBConstants.DB_NAME)) {
        influxDB.createDatabase(InfluxDBConstants.DB_NAME);
      }
      influxDB.setDatabase(InfluxDBConstants.DB_NAME);
      influxDB.enableBatch(1000, 500, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory());
    }
    return influxDB;
  }
}