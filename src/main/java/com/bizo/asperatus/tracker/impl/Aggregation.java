package com.bizo.asperatus.tracker.impl;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.bizo.asperatus.model.Dimension;
import com.bizo.asperatus.model.Unit;

final class Aggregation {
  private final String name;
  private final double count;
  private final double sum;
  private final double min;
  private final double max;
  private final Unit unit;
  private final List<Dimension> dimensions;

  public Aggregation(
      final String name,
      final double count,
      final double sum,
      final double min,
      final double max,
      final List<Dimension> dimensions) {
    this(name, count, sum, min, max, Unit.Count, dimensions);
  }

  public Aggregation(
      final String name,
      final double count,
      final double sum,
      final double min,
      final double max,
      final Unit unit,
      final List<Dimension> dimensions) {
    this.name = name;
    this.count = count;
    this.sum = sum;
    this.min = min;
    this.max = max;
    this.unit = unit;
    this.dimensions = dimensions;
  }

  public String getName() {
    return name;
  }

  public List<Dimension> getDimensions() {
    return dimensions;
  }

  public Unit getUnit() {
    return unit;
  }

  public double getCount() {
    return count;
  }

  public double getSum() {
    return sum;
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }
  
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(count);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((dimensions == null) ? 0 : dimensions.hashCode());
    temp = Double.doubleToLongBits(max);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(min);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    temp = Double.doubleToLongBits(sum);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((unit == null) ? 0 : unit.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Aggregation other = (Aggregation) obj;
    if (Double.doubleToLongBits(count) != Double.doubleToLongBits(other.count))
      return false;
    if (dimensions == null) {
      if (other.dimensions != null)
        return false;
    } else if (!dimensions.equals(other.dimensions))
      return false;
    if (Double.doubleToLongBits(max) != Double.doubleToLongBits(other.max))
      return false;
    if (Double.doubleToLongBits(min) != Double.doubleToLongBits(other.min))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (Double.doubleToLongBits(sum) != Double.doubleToLongBits(other.sum))
      return false;
    if (unit != other.unit)
      return false;
    return true;
  }
}
