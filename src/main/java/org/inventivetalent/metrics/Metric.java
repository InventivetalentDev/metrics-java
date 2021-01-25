package org.inventivetalent.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Metric {

    protected final Metrics handler;
    protected final String database;
    protected final String key;

    protected final Map<Map<String, String>, Map<String, AtomicInteger>> cache = new ConcurrentHashMap<>();

    public Metric(Metrics handler, String database, String key) {
        this.handler = handler;
        this.database = database;
        this.key = key;
    }

    protected void inc(int amount, String field, Map<String, String> tags) {
        this.cache.compute(tags, (key, value) -> {
            if (value == null) {
                value = new HashMap<>();
            }
            value.compute(field, (k, v) -> {
                if (v == null) {
                    v = new AtomicInteger();
                }
                v.addAndGet(amount);
                return v;
            });
            return value;
        });
    }

    public MetricDataBuilder field(String field) {
        return new MetricDataBuilder(this).field(field);
    }

    public MetricDataBuilder tag(String tag, String value) {
        return new MetricDataBuilder(this).tag(tag, value);
    }

    public void inc(int amount) {
        new MetricDataBuilder(this).inc(amount);
    }

    public void inc() {
        inc(1);
    }

}
