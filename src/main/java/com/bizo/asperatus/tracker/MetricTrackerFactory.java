package com.bizo.asperatus.tracker;

import static com.bizo.asperatus.tracker.Env.envOrProperty;

import java.util.concurrent.TimeUnit;

import com.bizo.asperatus.tracker.impl.CWMetricTrackerBuilder;
import com.bizo.asperatus.tracker.impl.SystemOutTracker;

/**
 * This class returns a MetricTracker instance based on standard environment variables.
 * 
 * The following are required (will throw a RuntimeException if missing):
 * 
 *   AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, APPLICATION
 *  
 * The following are used, but have defaults:
 *   EC2_REGION (defaults to us-east-1).
 *   STAGE (defaults to dev).
 *   
 * To programatically construct an instance with non-environment settings, consider using
 * CWMetricTrackerBuilder directly.
 */
public final class MetricTrackerFactory {
  
  private MetricTrackerFactory() { }
  

  /**
   * @return if persistMetrics is true, a cloud watch tracker initialized with the AWS/APPLICATION environment variables,
   *         otherwise a System.out tracker.
   */
  public static MetricTracker forApplication(final boolean persistMetrics, final long flushDelay, final TimeUnit flushDelayUnit) {
    if (! persistMetrics) {
      return new SystemOutTracker();
    }
    
    final CWMetricTrackerBuilder tracker = new CWMetricTrackerBuilder();
    
    tracker.withApplication(envOrProperty("APPLICATION"));
    tracker.withRegion(envOrProperty("EC2_REGION", "us-east-1"));
    tracker.withStage(envOrProperty("STAGE", "dev"));
    tracker.withFlushDelay(flushDelay, flushDelayUnit);
    
    return tracker.toMetricTracker();
  }
  
  public static MetricTracker forApplication() {
    return forApplication(true, 60, TimeUnit.SECONDS);
  }
}