package com.bizo.asperatus.tracker.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.StatisticSet;
import com.bizo.asperatus.model.Unit;

/**
 * Convert to/from Amazon data structures.
 */
public final class AggregationUtils {
  
  private AggregationUtils() { }
  
  public static List<MetricDatum> toMetricDatum(final List<Aggregation> agg) {
    final List<MetricDatum> out = new ArrayList<MetricDatum>(agg.size());
    
    for (final Aggregation a : agg) {
      out.add(toMetricDatum(a));
    }
    
    return out;
  }
  
  public static MetricDatum toMetricDatum(final Aggregation agg) {
    final MetricDatum datum = new MetricDatum();
    
    for (final com.bizo.asperatus.model.Dimension _d : agg.getDimensions()) {
      final Dimension d = new Dimension();
      d.setName(_d.getName());
      d.setValue(_d.getValue());
      datum.withDimensions(d);
    }
    
    datum.setUnit(agg.getUnit().getValue());
    datum.setMetricName(agg.getName());
    
    final StatisticSet stats = new StatisticSet();
    stats.setSum(agg.getSum());
    stats.setSampleCount(agg.getCount());
    stats.setMaximum(agg.getMax());
    stats.setMinimum(agg.getMin());
    datum.setStatisticValues(stats);
    
    return datum;
  }
  
  public static Aggregation toAggregation(final MetricDatum datum) {
    final List<com.bizo.asperatus.model.Dimension> dimensions = new ArrayList<com.bizo.asperatus.model.Dimension>(datum.getDimensions().size());
    
    for (final Dimension d : datum.getDimensions()) {
      dimensions.add(new com.bizo.asperatus.model.Dimension(d.getName(), d.getValue()));
    }
    
    final StatisticSet stats = datum.getStatisticValues();
    final Unit unit = Unit.fromValue(datum.getUnit());
    
    return new Aggregation(datum.getMetricName(), stats.getSampleCount(), stats.getSum(), stats.getMinimum(), stats.getMaximum(), unit, dimensions);
  }
  
  public static List<Aggregation> toAggregation(final Collection<MetricDatum> datum) {
    final List<Aggregation> out = new ArrayList<Aggregation>(datum.size());

    for (final MetricDatum d : datum) {
      out.add(toAggregation(d));
    }
    
    return out;
  }
}
