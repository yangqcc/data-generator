package com.yangqc.dg.service.tasks;/**
 * Created by yangqc on 17-11-14
 */

import com.yangqc.dg.service.InfluxInsertService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.RecursiveAction;

/**
 * Created by yangqc on 17-11-14
 */
@Component
@Scope(value = "prototype")
@Slf4j
public class InfluxDBInsertTask extends RecursiveAction {

    @Getter
    private final int count;

    @Getter
    private final InfluxInsertService influxInsertService;

    private final int threshold;

    private final ApplicationContext applicationContext;

    @Autowired
    public InfluxDBInsertTask(int count, InfluxInsertService influxInsertService, ApplicationContext applicationContext) {
        if (count <= 0) {
            throw new IllegalArgumentException("count不能小于0!");
        }
        Assert.notNull(influxInsertService);
        this.influxInsertService = influxInsertService;
        this.count = count;
        //设置阈值
        this.threshold = (count + 1) / Runtime.getRuntime().availableProcessors();
        this.applicationContext = applicationContext;
    }

    private InfluxDBInsertTask(int count, InfluxInsertService influxInsertService, int threshold, ApplicationContext applicationContext) {
        if (count <= 0) {
            throw new IllegalArgumentException("count不能小于0!");
        }
        if (threshold <= 0) {
            throw new IllegalArgumentException("threshold不能小于0！");
        }
        Assert.notNull(influxInsertService);
        this.influxInsertService = influxInsertService;
        this.count = count;
        //设置阈值
        this.threshold = threshold;
        this.applicationContext = applicationContext;
    }

    private void insert() {
        influxInsertService.insert(count);
    }

    @Override
    protected void compute() {
        if (count <= threshold) {
            insert();
        } else {
            int middle = (count + 1) / 2;
            InfluxDBInsertTask leftTask = new InfluxDBInsertTask(middle, applicationContext.getBean(InfluxInsertService.class), threshold, applicationContext);
            InfluxDBInsertTask rightTask = new InfluxDBInsertTask(middle, applicationContext.getBean(InfluxInsertService.class), threshold, applicationContext);
            invokeAll(leftTask, rightTask);
            if (leftTask.isCompletedNormally() && rightTask.isCompletedNormally()) {
                return;
            } else {
                throw new RuntimeException("出错！");
            }
        }
    }
}
