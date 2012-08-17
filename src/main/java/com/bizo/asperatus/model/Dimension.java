package com.bizo.asperatus.model;

import com.google.common.base.Preconditions;

public final class Dimension {
  private final String name;
  private final String value;

  public Dimension(final String name, final String value) {
    this.name = Preconditions.checkNotNull(name);
    this.value = Preconditions.checkNotNull(value);
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public String toString() {
    return String.format("Dimension[%s=%s]", name, value);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + name.hashCode();
    result = prime * result + value.hashCode();
    return result;
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
    final Dimension other = (Dimension) obj;
    
    return this.name.equals(other.name) && this.value.equals(other.value);
  }
}
