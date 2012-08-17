package com.bizo.asperatus.rates;

/**
 * Describes an object that tracks occurrences of a single event to compute the average rate over periods of time.
 * 
 * The rate of occurrences is simply the number of times that the track method has been called since the last time the
 * period was reset (either as part of the call to getRateAndResetPeriod, object instantiation, or some other
 * implementation-specific way of resetting the period) divided by the amount time since that reset.
 */
public interface RateTracker {

  /** Record that the tracked even occurred. */
  void track();

  /**
   * Get the rate of events per second based on the data since the last call to this method (or object instantiation,
   * for the first call).
   */
  double getRateAndResetPeriod();
}
