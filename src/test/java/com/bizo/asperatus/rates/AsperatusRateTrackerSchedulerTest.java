package com.bizo.asperatus.rates;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Test;

import com.bizo.asperatus.model.Dimension;
import com.bizo.asperatus.model.Unit;
import com.bizo.asperatus.tracker.MetricTracker;
import com.google.common.collect.ImmutableList;

public final class AsperatusRateTrackerSchedulerTest {
  private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  @AfterClass
  public static void tearDownClass() throws Exception {
    executor.shutdownNow();
  }

  private final MetricTracker metricTracker = mock(MetricTracker.class);
  private final RateTracker rateTracker = mock(RateTracker.class);

  private final String metricName = "metricName";
  private final List<Dimension> dimensions = ImmutableList.of(new Dimension("a", "1"));

  @Test
  public void test() throws Exception {
    when(rateTracker.getRateAndResetPeriod()).thenReturn(100.0);

    final AsperatusRateTrackerScheduler scheduler =
      new AsperatusRateTrackerScheduler(metricTracker, metricName, dimensions, rateTracker, executor);

    scheduler.setFrequency(100);
    scheduler.setFrequencyUnit(TimeUnit.MILLISECONDS);

    scheduler.start();

    Thread.sleep(1050);

    scheduler.stop();

    verify(rateTracker, times(10)).getRateAndResetPeriod();
    verify(metricTracker, times(10)).track(eq(metricName), anyDouble(), eq(Unit.CountSecond), eq(dimensions));
  }
}
