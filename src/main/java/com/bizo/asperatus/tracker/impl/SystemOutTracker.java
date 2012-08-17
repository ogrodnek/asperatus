package com.bizo.asperatus.tracker.impl;

import java.util.Collection;

import com.bizo.asperatus.model.CompoundDimension;
import com.bizo.asperatus.model.Unit;
import com.bizo.asperatus.tracker.AbstractMetricTracker;
import com.bizo.asperatus.tracker.MetricTracker;

/** Dumps metrics to {@link System#out} for local/dev testing. */
public class SystemOutTracker extends AbstractMetricTracker implements MetricTracker {
  @Override
  public void track(final String metricName, final Number value, final Unit unit, final Collection<CompoundDimension> dimensions) {
    System.out.format("%s = %s (%s)\n", metricName, value, unit);
  }
}
