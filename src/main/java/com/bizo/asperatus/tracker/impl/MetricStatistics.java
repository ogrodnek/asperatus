package com.bizo.asperatus.tracker.impl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.bizo.asperatus.model.Unit;

public final class MetricStatistics {

  private final Unit unit;
  private long samples = 0;
  private float sum = 0;
  private float min = Float.MAX_VALUE;
  private float max = Float.MIN_VALUE;

  public MetricStatistics(final Unit unit) {
    this.unit = unit;
  }

  public void add(final Number n) {
    final float f = n.floatValue();
    min = Math.min(min, f);
    max = Math.max(max, f);
    sum += f;
    samples += 1;
  }

  public long getSamples() {
    return samples;
  }

  public float getSum() {
    return sum;
  }

  public float getMin() {
    return min;
  }

  public float getMax() {
    return max;
  }
  
  public Unit getUnit() {
    return unit;
  }

  public String toString() {
    return String.format("MStats[unit:%s,samples=%d,sum=%2.2f,min=%2.2f,max=%2.2f]", unit, samples, sum, min, max);
  }
  
  @Override
  public boolean equals(final Object that) {
    return EqualsBuilder.reflectionEquals(this, that);
  }
  
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}