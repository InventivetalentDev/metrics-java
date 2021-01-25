package org.inventivetalent.metrics;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import java.util.HashSet;
import java.util.Set;

public class Metrics {

    protected final InfluxDB influx;
    protected final Set<Metric> metrics = new HashSet<>();

    private Flusher flusher;

    public Metrics(String url) {
        this.influx = InfluxDBFactory.connect(url);
    }

    public Metrics(String url, String username, String password) {
        this.influx = InfluxDBFactory.connect(url, username, password);
    }

    public InfluxDB getInflux() {
        return influx;
    }

    public void setFlusher(Flusher flusher) {
        this.flusher = flusher;
    }

    public Metric metric(String database, String key) {
        Metric metric = new Metric(this, database, key);
        this.metrics.add(metric);
        return metric;
    }

}
