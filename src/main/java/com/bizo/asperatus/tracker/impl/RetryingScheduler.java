package com.bizo.asperatus.tracker.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Encapsulates running a task with retries.
 */
public final class RetryingScheduler {
  private static final Logger logger = Logger.getLogger(RetryingScheduler.class.getName());

  private final ScheduledExecutorService executor;

  public RetryingScheduler(final ScheduledExecutorService executor) {
    this.executor = executor;
  }

  public void schedule(final Callable<?> task, final int numRetries, final int retryDelaySeconds) {
    schedule(task, 0, numRetries, retryDelaySeconds);
  }

  /**
   * Schedule a task for execution. If the task throws an exception, it will be retried.
   * 
   * @param task
   *          task to call
   * @param initialDelaySeconds
   *          initial delay (seconds) before executing the task
   * @param numRetries
   *          number of times to retry task
   * @param retryDelaySeconds
   *          delay (seconds) before retrying a failed task
   */
  public void schedule(
      final Callable<?> task,
      final int initialDelaySeconds,
      final int numRetries,
      final int retryDelaySeconds) {
    executor.schedule(
      new RetryWrapper(task, numRetries, retryDelaySeconds, null),
      initialDelaySeconds,
      TimeUnit.SECONDS);
  }

  private final class RetryWrapper implements Runnable {
    private final Callable<?> call;
    private final int retry;
    private final int retryDelay;
    private final Exception lastThrown;

    private RetryWrapper(final Callable<?> call, final int retry, final int retryDelay, final Exception lastThrown) {
      this.call = call;
      this.retry = retry;
      this.retryDelay = retryDelay;
      this.lastThrown = lastThrown;
    }

    public void run() {
      if (retry <= 0) {
        logger.log(Level.WARNING, String.format("Failed to run task: %s", call), lastThrown);
        return;
      }

      try {
        call.call();
      } catch (final Exception e) {
        logger.log(Level.FINEST, String.format("Failed to run task: %s", call), e);
        executor.schedule(new RetryWrapper(call, retry - 1, retryDelay, e), retryDelay, TimeUnit.SECONDS);
      }
    }
  }
}
