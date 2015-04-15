package com.bizo.asperatus.rates;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.base.Stopwatch;

/**
 * This class is a basic thread-safe implementation of RateTracker.
 * 
 * The recommended use pattern is to use a scheduled executor service to periodically call "getRateAndResetPeriod" and
 * pass the result to a metric tracking service (ie, Asperatus/CloudWatch).
 */
public final class ThreadSafeRateTracker implements RateTracker {
  /*
   * This read/write lock is used to control flushes to the underlying metric tracker. The "read" trackLock should be
   * acquired prior to updating the counter. The "write" flushLock should be acquired prior to resetting the counter and
   * stopwatch.
   */
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock trackLock = lock.readLock();
  private final Lock flushLock = lock.writeLock();

  /** Number of events since last flush. Acquire trackLock before incrementing and flushLock before resetting. */
  private final AtomicLong counter = new AtomicLong(0L);

  /** Measures the length of the current period. Acquire flushLock before modifying. */
  private final Stopwatch stopwatch;

  /** Constructor that uses the system time to track the duration of each reporting period. */
  public ThreadSafeRateTracker() {
    this(Stopwatch.createUnstarted());
  }

  /** Constructor that allows passing a specific Stopwatch implementation. Intended for testing. */
  public ThreadSafeRateTracker(final Stopwatch stopwatch) {
    checkNotNull(stopwatch);
    this.stopwatch = stopwatch;
    this.stopwatch.start();
  }

  @Override
  public void track() {
    trackLock.lock();
    try {
      counter.incrementAndGet();
    } finally {
      trackLock.unlock();
    }
  }

  @Override
  public double getRateAndResetPeriod() {
    final double count;
    final double periodLengthSeconds;

    flushLock.lock();
    try {
      stopwatch.stop();

      periodLengthSeconds = stopwatch.elapsed(SECONDS);

      count = counter.getAndSet(0L);

      stopwatch.reset();
      stopwatch.start();
    } finally {
      flushLock.unlock();
    }

    return count / periodLengthSeconds;
  }

}
