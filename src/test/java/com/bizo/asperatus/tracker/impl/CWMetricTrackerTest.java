package com.bizo.asperatus.tracker.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.bizo.asperatus.model.CompoundDimension;
import com.bizo.asperatus.model.Dimension;
import com.bizo.asperatus.model.Unit;
import com.google.common.collect.Lists;

public final class CWMetricTrackerTest {
  private final TestCloudwatch cloudwatch = new TestCloudwatch();
  private final CWMetricTracker tracker = new CWMetricTracker(
    cloudwatch,
    "TestNamespace",
    Executors.newScheduledThreadPool(3),
    50, TimeUnit.MILLISECONDS);

  @Test
  public void testSmall() throws Exception {

    final List<Dimension> dims = new ArrayList<Dimension>();
    dims.add(new Dimension("d1", "a"));

    tracker.track("m1", 5, dims);

    Thread.sleep(100);

    assertTrue(hasAggregation("TestNamespace", aggregation("m1", "d1", "a", 1, 5, 5, 5)));
  }
  
  @Test
  public void testMultiDimension() throws Exception {
    final List<Dimension> dims = new ArrayList<Dimension>();
    dims.add(new Dimension("d1", "a"));
    dims.add(new Dimension("d2", "a"));
    
    tracker.track("m1", 5, Unit.Count, dims);
    Thread.sleep(100);
    
    assertTrue(hasAggregation("TestNamespace", aggregation("m1", "d1", "a", 1, 5, 5, 5)));    
    assertTrue(hasAggregation("TestNamespace", aggregation("m1", "d2", "a", 1, 5, 5, 5)));
  }
  
  @Test
  public void testCompoundDimension() throws Exception {
    final List<Dimension> dims = new ArrayList<Dimension>();
    dims.add(new Dimension("d1", "a"));
    dims.add(new Dimension("d2", "a"));
    
    tracker.track("m1", 5, Unit.Count, Collections.singleton(new CompoundDimension(dims)));
    Thread.sleep(100);
    
    assertTrue(hasAggregation("TestNamespace", aggregation("m1", dims, 1, 5, 5, 5)));
  }

  @Test
  public void testMany() throws Exception {

    final List<Dimension> dims = new ArrayList<Dimension>();
    dims.add(new Dimension("d1", "a"));

    for (int i = 0; i < 1000; i++) {
      tracker.track(String.valueOf(i), 5, dims);
    }

    Thread.sleep(400);

    for (int i = 0; i < 1000; i++) {
      assertTrue(hasAggregation("TestNamespace", aggregation(String.valueOf(i), "d1", "a", 1, 5, 5, 5)));
    }
  }

  private boolean hasAggregation(final String namespace, final Aggregation agg) {
    final List<Aggregation> aggs = cloudwatch.aggregations.get(namespace);
    
    return (aggs != null && aggs.contains(agg));
  }
  
  private static Aggregation aggregation(
      final String metric,
      final String dimKey,
      final String dimVal,
      final long samples,
      final float sum,
      final float min,
      final float max) {
    return aggregation(metric, Lists.newArrayList(new Dimension(dimKey, dimVal)), samples, sum, min, max);
  }

  private static Aggregation aggregation(
      final String metric,
      List<Dimension> dims,
      final long samples,
      final float sum,
      final float min,
      final float max) {

    return new Aggregation(metric, samples, sum, min, max, Unit.Count, dims);
  }
}
