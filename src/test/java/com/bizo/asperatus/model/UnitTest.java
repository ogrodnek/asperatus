package com.bizo.asperatus.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class UnitTest {
  @Test(expected=IllegalArgumentException.class)
  public void testBadUnit() {
    Unit.fromValue("bad val");
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testNullUnit() {
    Unit.fromValue(null);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBlankUnit() {
    Unit.fromValue(" ");
  }
  
  @Test
  public void testUnit() {
    assertEquals(Unit.Count, Unit.fromValue("Count"));
    assertEquals(Unit.Seconds, Unit.fromValue("Seconds"));    
  }
}
