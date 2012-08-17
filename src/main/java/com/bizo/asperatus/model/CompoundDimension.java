package com.bizo.asperatus.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Compound dimension key.
 * 
 * Metrics can be stored/queried under a key made up of multiple dimensions.
 * 
 * Note that the query must be the same complete list used to store the metrics,
 * sublists won't work.
 */
public final class CompoundDimension {
  private final List<Dimension> dimensions;
  
  public CompoundDimension(final List<Dimension> dimensions) {
    this.dimensions = new ArrayList<Dimension>(dimensions);
  }
  
  public CompoundDimension(final Dimension... dimensions) {
    this.dimensions = new ArrayList<Dimension>(Arrays.asList(dimensions));
  }
  
  public List<Dimension> getDimensions() {
    return Collections.unmodifiableList(dimensions);
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  @Override
  public int hashCode() {
    return dimensions.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (obj == null) {
      return false;
    }
    
    if (getClass() != obj.getClass()) {
      return false;
    }
    
    final CompoundDimension other = (CompoundDimension) obj;
    return this.dimensions.equals(other.dimensions);
  }
}
