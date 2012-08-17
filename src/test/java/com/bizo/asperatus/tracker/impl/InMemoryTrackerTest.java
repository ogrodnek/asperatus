package com.bizo.asperatus.tracker.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.bizo.asperatus.model.CompoundDimension;
import com.bizo.asperatus.model.Dimension;
import com.bizo.asperatus.model.Unit;
import com.bizo.asperatus.tracker.impl.buffer.InMemoryTracker;
import com.google.common.collect.Lists;

public final class InMemoryTrackerTest {

  private final InMemoryTracker tracker = new InMemoryTracker();

  @Test
  public void test() {
    List<CompoundDimension> dims = Lists.newArrayList(new CompoundDimension(new Dimension("d1", "x"), new Dimension("d2", "z")));

    tracker.track("a", 1, Unit.Count, dims);
    tracker.track("a", 2, Unit.Count, dims);
    
    dims.clear();
    dims.add(new CompoundDimension(new Dimension("d1", "x"), new Dimension("d1", "v"), new Dimension("d3", "z")));
    
    tracker.track("a", 1, Unit.Count, dims);
    tracker.track("a", 2, Unit.Count, dims);
    tracker.track("b", 1, Unit.Seconds, dims);
    tracker.track("b", 7, Unit.Seconds, dims);

    final Map<MetricKey, MetricStatistics> data = tracker.reset();
    
    assertEquals(3, data.size());

    // verify it's clear
    assertEquals(0, tracker.reset().size());

    // check records
    assertStats(data, "a", "d1=x,d2=z", Unit.Count, 2, 3, 1, 2);
    assertStats(data, "a", "d1=x,d1=v,d3=z", Unit.Count, 2, 3, 1, 2);    
    assertStats(data, "b", "d1=x,d1=v,d3=z", Unit.Seconds, 2, 8, 1, 7);
  }
  
  private void assertStats(
      final Map<MetricKey, MetricStatistics> data,
      final String name,
      final String dims,
      final Unit unit,
      final long count,
      final float sum,
      final float min,
      final float max) {
    final MetricStatistics stats = data.get(new MetricKey(name, toDims(dims)));

    assertEquals(count, stats.getSamples());

    assertEquals(sum, stats.getSum(), 0.0000001f);
    assertEquals(min, stats.getMin(), 0.0000001f);
    assertEquals(max, stats.getMax(), 0.0000001f);
    assertEquals(unit, stats.getUnit());
  }
  
  private CompoundDimension toDims(final String s) {
    final List<Dimension> ret = new ArrayList<Dimension>();
    
    final String[] dims = s.split(",");
    for (final String d : dims) {
      final String[] p = d.split("=");
      ret.add(new Dimension(p[0], p[1]));
    }
    
    return new CompoundDimension(ret);
  }
}
