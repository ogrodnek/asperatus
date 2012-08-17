package com.bizo.asperatus.rates;

import static com.bizo.asperatus.model.Unit.CountSecond;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.logging.Level.WARNING;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.bizo.asperatus.model.Dimension;
import com.bizo.asperatus.tracker.MetricTracker;

/**
 * Helper class that will schedule flushing the rate reported by a RateTracker to an Asperatus MetricTracker using a
 * ScheduledExecutorService. By default, this class will push metrics once per minute; this can be adjusted via the
 * frequency and frequencyUnit parameters provided that the scheduler is stopped.
 * 
 * Note that this class is not thread safe. Additionally, the start/stop methods are not idempotent; they will throw
 * IllegalStateExceptions if called out of order.
 */
public final class AsperatusRateTrackerScheduler {
  private static final Logger LOGGER = Logger.getLogger(AsperatusRateTrackerScheduler.class.getName());

  private final ScheduledExecutorService executor;
  private final Runnable runnable;
  private ScheduledFuture<?> currentFuture = null;

  private int frequency = 1;
  private TimeUnit frequencyUnit = MINUTES;

  /**
   * Creates a new AsperatusRateTrackerScheduler in the stopped state.
   */
  public AsperatusRateTrackerScheduler(
      final MetricTracker metricTracker,
      final String metricName,
      final List<Dimension> dimensions,
      final RateTracker rateTracker,
      final ScheduledExecutorService executor) {
    this.executor = executor;
    runnable = new Runnable() {
      public void run() {
        try {
          final double rate = rateTracker.getRateAndResetPeriod();
          metricTracker.track(metricName, rate, CountSecond, dimensions);
        } catch (final Exception e) {
          LOGGER.log(WARNING, "Exception flushing rate to Asperatus for metric " + metricName, e);
        }
      }
    };
  }

  public void start() {
    if (currentFuture != null) {
      throw new IllegalStateException("This AsperatusRateTrackerScheduler has already been started.");
    }
    currentFuture = executor.scheduleAtFixedRate(runnable, frequency, frequency, frequencyUnit);
  }

  public void stop() {
    if (currentFuture == null) {
      throw new IllegalStateException("This AsperatusRateTrackerScheduler has already been stopped.");
    }
    currentFuture.cancel(true);
  }

  public boolean isStarted() {
    return currentFuture != null;
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(final int frequency) {
    if (isStarted()) {
      throw new IllegalStateException("The AsperatusRateTrackerScheduler must be stopped to adjust the frequency.");
    }
    this.frequency = frequency;
  }

  public TimeUnit getFrequencyUnit() {
    return frequencyUnit;
  }

  public void setFrequencyUnit(final TimeUnit frequencyUnit) {
    if (isStarted()) {
      throw new IllegalStateException("The AsperatusRateTrackerScheduler must be stopped to adjust the frequencyUnit.");
    }
    this.frequencyUnit = frequencyUnit;
  }
}
