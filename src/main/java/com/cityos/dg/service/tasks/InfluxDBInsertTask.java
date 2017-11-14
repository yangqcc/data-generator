package com.cityos.dg.service.tasks;/**
 * Created by yangqc on 17-11-14
 */

import com.cityos.dg.service.InfluxInsertService;
import com.cityos.dg.utils.ExtraRandom;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.RecursiveAction;

/**
 * Created by yangqc on 17-11-14
 */
@Service
public class InfluxDBInsertTask extends RecursiveAction {

    @Getter
    private final int count;

    @Getter
    private final InfluxInsertService influxInsertService;

    private final int threshold;

    @Autowired
    public InfluxDBInsertTask(int count, InfluxInsertService influxInsertService) {
        if (count <= 0) {
            throw new IllegalArgumentException("count不能小于0!");
        }
        Assert.notNull(influxInsertService);
        this.influxInsertService = influxInsertService;
        this.count = count;
        //设置阈值
        this.threshold = (count + 1) / Runtime.getRuntime().availableProcessors();
    }

    private void insert() {
        Map value;
        List list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            value = new HashMap<>();
            value.put("veh_license", ExtraRandom.nextString(30, "letter"));
            value.put("bus_line_id", Integer.parseInt(ExtraRandom.nextString(9, "digit")));
            value.put("ori_flag", "2");
            value.put("run_state", "2");
            value.put("station_id", Integer.parseInt(ExtraRandom.nextString(9, "digit")));
            value.put("gps_report_dt", new Date().getTime());
            list.add(value);
        }
        influxInsertService.insert(null, list);
    }

    @Override
    protected void compute() {
        if (count < threshold) {
            insert();
        } else {
            int middle = (count + 1) / 2;
            InfluxDBInsertTask leftTask = new InfluxDBInsertTask(middle, influxInsertService);
            InfluxDBInsertTask rightTask = new InfluxDBInsertTask(middle, influxInsertService);
            invokeAll(leftTask, rightTask);
            if (leftTask.isCompletedNormally() && rightTask.isCompletedNormally()) {
                return;
            } else {
                throw new RuntimeException("出错！");
            }
        }
    }
}
