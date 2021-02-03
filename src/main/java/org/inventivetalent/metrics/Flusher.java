package org.inventivetalent.metrics;

import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Flusher {

    private final Metrics handler;

    public Flusher(Metrics handler) {
        this.handler = handler;
    }

    public void flush() {
        this.flush(this.handler.metrics);
    }

    protected void flush(Set<Metric> metrics) {
        Map<String, BatchPoints.Builder> pointsByDatabase = collectPointsByDatabase(metrics);
        pointsByDatabase.forEach((db, pointsBuilder) -> {
            if (pointsBuilder != null) {
                handler.influx.write(pointsBuilder.build());
            }
        });
    }

    protected static Map<String, BatchPoints.Builder> collectPointsByDatabase(Set<Metric> metrics) {
        Map<String, BatchPoints.Builder> pointsByDatabase = new HashMap<>();
        metrics.forEach(m -> {
            pointsByDatabase.compute(m.database, (db, builder) -> {
                if (builder == null) {
                    builder = BatchPoints.builder();
                }
                Point.Builder point = Point.measurement(m.key);
                m.cache.forEach((tags, counts) -> {
                    point.tag(tags);
                    counts.forEach((f, c) -> point.addField(f, c.intValue()));
                });
                if (point.hasFields()) {
                    builder.point(point.build());
                    return builder;
                }
                return null;
            });
            m.cache.clear();
        });
        return pointsByDatabase;
    }

}
