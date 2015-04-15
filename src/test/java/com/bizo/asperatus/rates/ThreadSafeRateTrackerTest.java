package com.bizo.asperatus.rates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;

public final class ThreadSafeRateTrackerTest {
  private static final double TOLERANCE = 0.0000001;

  private final SetTimeTicker ticker = new SetTimeTicker();
  private final ThreadSafeRateTracker rateTracker = new ThreadSafeRateTracker(Stopwatch.createUnstarted(ticker));

  @Test
  public void testOneSecondThenTwoSecondsWithConstantRate() {
    final int requestCount = 100;
    for (int i = 0; i < requestCount; i++) {
      rateTracker.track();
    }
    ticker.tick();
    assertEquals(100.0, rateTracker.getRateAndResetPeriod(), TOLERANCE);

    for (int i = 0; i < 2 * requestCount; i++) {
      rateTracker.track();
    }
    ticker.tick();
    ticker.tick();
    assertEquals(100.0, rateTracker.getRateAndResetPeriod(), TOLERANCE);
  }

  /** Ticker implementation where the current time is a settable constant. */
  private static class SetTimeTicker extends Ticker {
    public long timeNanos = 0L;

    @Override
    public long read() {
      return timeNanos;
    }

    // one second later...
    public void tick() {
      timeNanos += 1000000000L;
    }
  }
}
