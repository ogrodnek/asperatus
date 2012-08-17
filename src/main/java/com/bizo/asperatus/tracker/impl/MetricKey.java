package com.bizo.asperatus.tracker.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.bizo.asperatus.model.CompoundDimension;
import com.bizo.asperatus.model.Dimension;
import com.google.common.base.Preconditions;

public final class MetricKey {
  private final String metricName;
  private final CompoundDimension dimension;

  public MetricKey(final String metricName, final CompoundDimension dimension) {
    this.metricName = Preconditions.checkNotNull(metricName);
    this.dimension = Preconditions.checkNotNull(dimension);
  }
  
  public MetricKey(final String metricName, final Dimension dimension) {
    this(metricName, new CompoundDimension(dimension));
  }

  public String getMetricName() {
    return metricName;
  }

  public CompoundDimension getDimension() {
    return dimension;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + dimension.hashCode();
    result = prime * result + metricName.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MetricKey other = (MetricKey) obj;
    
    return metricName.equals(other.metricName) && dimension.equals(other.dimension);
  }
}