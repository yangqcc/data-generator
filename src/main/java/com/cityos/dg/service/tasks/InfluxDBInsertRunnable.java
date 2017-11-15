package com.cityos.dg.service.tasks;/**
 * Created by yangqc on 17-11-14
 */

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by yangqc on 17-11-14
 */
@Component(value = "influxdbInsertRunnable")
@Scope(value = "prototype")
@AllArgsConstructor
@Slf4j
public class InfluxDBInsertRunnable implements Runnable {

    private final InfluxDBInsertTask influxDBInsertTask;
    private final ForkJoinPool forkJoinPool;

    @Override
    public void run() {
        log.info("开始插入:" + influxDBInsertTask.getCount());
        long t1 = System.currentTimeMillis();
        ForkJoinTask forkJoinTask = forkJoinPool.submit(influxDBInsertTask);
        forkJoinTask.join();
        log.info("插入" + influxDBInsertTask.getCount() + "条数据,最终耗时:" + (System.currentTimeMillis() - t1));
    }
}
