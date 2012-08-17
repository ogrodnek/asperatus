package com.bizo.asperatus.tracker.impl;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;

import org.junit.Test;

import com.bizo.concurrent.StubScheduledExecutor;

public final class RetryingSchedulerTest {

  private final StubScheduledExecutor executor = new StubScheduledExecutor();
  final TestCall call = new TestCall();
  final RetryingScheduler scheduler = new RetryingScheduler(executor);

  @Test
  public void testNoFailure() {
    scheduler.schedule(call, 5, 10);

    assertEquals(1, executor.getCount());
    assertEquals(0, executor.getLastDelay());

    // command executes, not rescheduled
    executor.getCommand().run();
    assertEquals(1, executor.getCount());
  }

  @Test
  public void testRetry() {
    scheduler.schedule(call, 5, 10);

    assertEquals(1, executor.getCount());
    assertEquals(0, executor.getLastDelay());

    call.shouldThrow = true;

    // command should fail and be re-scheduled
    for (int i = 0; i < 5; i++) {
      executor.getCommand().run();
      assertEquals(i + 2, executor.getCount());
      assertEquals(10, executor.getLastDelay());
    }

    // command should be dropped and no longer rescheduled
    for (int i = 0; i < 10; i++) {
      executor.getCommand().run();
      assertEquals(6, executor.getCount());
    }
  }

  private static final class TestCall implements Callable<Boolean> {
    private boolean shouldThrow = false;

    public Boolean call() throws Exception {
      if (shouldThrow) {
        throw new RuntimeException();
      }

      return Boolean.TRUE;
    }
  }
}
