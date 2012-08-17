package com.bizo.asperatus.tracker;

import java.util.List;

import com.bizo.asperatus.model.CompoundDimension;
import com.bizo.asperatus.model.Dimension;
import com.bizo.asperatus.model.Unit;
import com.google.common.collect.Lists;

/**
 * Simple abstract parent to forward track calls without a unit, or with simple dimensions.
 * 
 * Subclasses must only implement:
 * 
 *   void track(String metricName, Number value, Unit unit, Collection<CompoundDimension> dimensions);
 */
public abstract class AbstractMetricTracker implements MetricTracker {
  @Override
  public void track(final String metricName, final Number value, final List<Dimension> dimensions) {
    track(metricName, value, Unit.Count, dimensions);
  }
  
  @Override
  public void track(final String metricName, final Number value, final Unit unit, final List<Dimension> dimensions) {
    for (final Dimension d : dimensions) {
      track(metricName, value, unit, Lists.newArrayList(new CompoundDimension(d)));
    }
  }
  
  @Override
  public void close() {
    
  }
}
