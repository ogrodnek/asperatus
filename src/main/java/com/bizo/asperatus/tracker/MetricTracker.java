package com.bizo.asperatus.tracker;

import java.util.Collection;
import java.util.List;

import com.bizo.asperatus.model.CompoundDimension;
import com.bizo.asperatus.model.Dimension;
import com.bizo.asperatus.model.Unit;

public interface MetricTracker {
  /**
   * Track metrics.
   * 
   * Equivalent to track(metricName, value, Unit.Count, dimensions);
   * 
   * @param metricName
   *          metric name
   * @param value
   *          value
   * @param dimensions
   *          dimensions to track against (ordering unimportant)
   */
  void track(String metricName, Number value, List<Dimension> dimensions);
  
  
  /**
   * Track metrics.
   * 
   * Metrics will be tracked individually against each dimension passed.
   * 
   * I.e. track("a", 1, List((d1,v1), (d2,v2), (d3,v3))) is equivalent to calling track("a", 1, List((d1, v1)));
   * track("a", 1, List((d2,v2)));...
   * 
   * @param metricName
   *          metric name
   * @param value
   *          value
   * @param unit
   *          unit
   * @param dimensions
   *          dimensions to track against (ordering unimportant)
   */  
  void track(String metricName, Number value, Unit unit, List<Dimension> dimensions);
  
  
  /**
   * Track metrics with compound dimensions.
   */
  void track(String metricName, Number value, Unit unit, Collection<CompoundDimension> dimensions);

  /**
   * Close tracker, perform any flushing, cleanup, etc.
   */
  void close();
}
