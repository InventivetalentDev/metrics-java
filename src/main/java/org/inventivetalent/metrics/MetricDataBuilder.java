package org.inventivetalent.metrics;

import java.util.HashMap;
import java.util.Map;

public class MetricDataBuilder {

    private final Metric metric;

    private String field = "count";
    private final Map<String, String> tags = new HashMap<>();

    public MetricDataBuilder(Metric metric) {
        this.metric = metric;
    }

    public MetricDataBuilder field(String field) {
        this.field = field;
        return this;
    }

    public MetricDataBuilder tag(String tag, String value) {
        this.tags.put(tag, value);
        return this;
    }

    public void inc(int amount) {
        this.metric.inc(amount, this.field, this.tags);
    }

    public void inc() {
        inc(1);
    }

}
