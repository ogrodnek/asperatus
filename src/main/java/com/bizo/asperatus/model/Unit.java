package com.bizo.asperatus.model;

import org.apache.commons.lang.StringUtils;

/**
 * Metric Units.
 */
public enum Unit {
  Count("Count"),
  Seconds("Seconds"),
  Microseconds("Microseconds"),
  Milliseconds("Milliseconds"),
  Bytes("Bytes"),
  Kilobytes("Kilobytes"),
  Megabytes("Megabytes"),
  Gigabytes("Gigabytes"),
  Terabytes("Terabytes"),
  Bits("Bits"),
  Kilobits("Kilobits"),
  Megabits("Megabits"),
  Gigabits("Gigabits"),
  Terabits("Terabits"),
  Percent("Percent"),
  BytesSecond("Bytes/Second"),
  KilobytesSecond("Kilobytes/Second"),
  MegabytesSecond("Megabytes/Second"),
  GigabytesSecond("Gigabytes/Second"),
  TerabytesSecond("Terabytes/Second"),
  BitsSecond("Bits/Second"),
  KilobitsSecond("Kilobits/Second"),
  MegabitsSecond("Megabits/Second"),
  GigabitsSecond("Gigabits/Second"),
  TerabitsSecond("Terabits/Second"),
  CountSecond("Count/Second"),
  None("None");

  private final String value;

  private Unit(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
  
  public static Unit fromValue(final String val) {
    if (StringUtils.isBlank(val)) {
      throw new IllegalArgumentException("Value cannot be null or empty!");
    }
    
    for (final Unit unit : values()) {
      if (val.equals(unit.getValue())) {
        return unit;
      }
    }
    
    throw new IllegalArgumentException("Cannot create unit enum from " + val + " value!");
  }
}
