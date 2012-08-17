package com.bizo.asperatus.tracker.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreadFactoryUtils {
  private ThreadFactoryUtils() { }
  
  /**
   * Construct a new ThreadFactory where threads will be marked as daemon
   * and name will be <name>-<pool number>-<thread>-<thread number>
   * e.g. asperatus-metrics-1-thread-1
   */
  public static ThreadFactory namedDaemonThreadFactory(final String name) {
    return new NamedThreadFactory(name, true);
  }
  
  private static final class NamedThreadFactory implements ThreadFactory {
    
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    
    private final ThreadGroup group;
    private final boolean isDaemon;
    private final String namePrefix;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    
    private NamedThreadFactory(final String name, final boolean isDaemon) {
      final SecurityManager s = System.getSecurityManager();
      group = (s != null)? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
      namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
      this.isDaemon = isDaemon;
    }
    
    @Override
    public Thread newThread(final Runnable r) {
      final Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
      t.setDaemon(isDaemon);
      return t;
    }    
  }
}
