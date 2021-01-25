package org.inventivetalent.metrics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class IntervalFlusher extends Flusher {

    private final ScheduledExecutorService executor;
    private final ScheduledFuture<?> future;

    public IntervalFlusher(Metrics handler, long interval, TimeUnit timeUnit) {
        super(handler);
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.future = this.executor.scheduleAtFixedRate(this::flush, interval, interval, timeUnit);
    }

    public void cancel() {
        this.future.cancel(false);
        this.executor.shutdown();
    }

}
