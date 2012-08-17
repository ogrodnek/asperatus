package com.bizo.asperatus.tracker.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.bizo.asperatus.model.CompoundDimension;
import com.bizo.asperatus.model.Unit;
import com.bizo.asperatus.tracker.AbstractMetricTracker;
import com.bizo.asperatus.tracker.MetricTracker;
import com.bizo.asperatus.tracker.impl.buffer.InMemoryTracker;
import com.bizo.asperatus.tracker.impl.buffer.MetricBuffer;

/**
 * MetricTracker implementation that performs local aggregation, then pushes the results to Cloudwatch at a fixed
 * interval.
 * 
 * @author larry
 */
public final class CWMetricTracker extends AbstractMetricTracker implements MetricTracker {
  private final MetricBuffer buffer = new InMemoryTracker();
  private final ScheduledExecutorService executor;

  public CWMetricTracker(
      final AmazonCloudWatch cloudwatch,
      final String namespace,
      final ScheduledExecutorService executor,
      final long flushDelay,
      final TimeUnit flushDelayUnit
      ) {
    this.executor = executor;
    executor.scheduleAtFixedRate(
      new PushStatsTask(cloudwatch, namespace, buffer, executor),
      flushDelay,
      flushDelay,
      flushDelayUnit);
  }

  @Override
  public void track(final String metricName, final Number value, final Unit unit, final Collection<CompoundDimension> dimensions) {
    buffer.track(metricName, value, unit, dimensions);
  }

  @Override
  public void close() {
    try {
      // TODO: this should really just pull out any pending tasks and execute them....
      executor.awaitTermination(5, TimeUnit.SECONDS);
    } catch (final InterruptedException ie) {
      Thread.currentThread().interrupt();
      // fall through
    }
    executor.shutdownNow();
  }

  private static final class PushStatsTask implements Runnable {
    private static final int MAX_PER_REQUEST = 10;
    private static final int NUM_RETRIES = 5;
    private static final int RETRY_DELAY_SEC = 30;
    private final AmazonCloudWatch cloudwatch;
    private final MetricBuffer buffer;
    private final String namespace;
    private final RetryingScheduler scheduler;

    private PushStatsTask(
        final AmazonCloudWatch cloudwatch,
        final String namespace,
        final MetricBuffer buffer,
        final ScheduledExecutorService executor) {
      this.cloudwatch = cloudwatch;
      this.namespace = namespace;
      this.buffer = buffer;
      scheduler = new RetryingScheduler(executor);
    }

    public void run() {
      final Map<MetricKey, MetricStatistics> stats = buffer.reset();
      if (stats.isEmpty()) {
        return;
      }

      final List<Aggregation> agg = new ArrayList<Aggregation>();

      // batch up cloudwatch requests
      for (final Map.Entry<MetricKey, MetricStatistics> me : stats.entrySet()) {
        final Aggregation a = toAggregation(me.getKey(), me.getValue());

        agg.add(a);

        if (agg.size() >= MAX_PER_REQUEST) {
          submit(agg);
          agg.clear();
        }
      }

      submit(agg);
    }

    private void submit(final List<Aggregation> agg) {
      if (agg.size() > 0) {
        final CloudwatchSubmitTask task =
          new CloudwatchSubmitTask(cloudwatch, namespace, new ArrayList<Aggregation>(agg));
        scheduler.schedule(task, NUM_RETRIES, RETRY_DELAY_SEC);
      }
    }

    private static final Aggregation toAggregation(final MetricKey key, final MetricStatistics val) {
      return new Aggregation(
        key.getMetricName(),
        val.getSamples(),
        val.getSum(),
        val.getMin(),
        val.getMax(),
        val.getUnit(),
        key.getDimension().getDimensions());
    }
  }

  private static final class CloudwatchSubmitTask implements Callable<Boolean> {
    private final AmazonCloudWatch cloudwatch;
    private final String namespace;
    private final List<Aggregation> agg;

    public CloudwatchSubmitTask(final AmazonCloudWatch cloudwatch, final String namespace, final List<Aggregation> agg) {
      this.cloudwatch = cloudwatch;
      this.namespace = namespace;
      this.agg = agg;
    }

    public Boolean call() throws Exception {
      final PutMetricDataRequest request = new PutMetricDataRequest();
      request.setNamespace(namespace);
      request.setMetricData(AggregationUtils.toMetricDatum(agg));
      
      cloudwatch.putMetricData(request);

      return Boolean.TRUE;
    }

    public String toString() {
      return String.format("CloudwatchSubmitTask[ns=%s,agg=%s]", agg, namespace);
    }
  }
}
