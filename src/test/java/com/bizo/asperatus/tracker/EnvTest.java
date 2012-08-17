package com.bizo.asperatus.tracker;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

public final class EnvTest {
  final String prop = prop();
  
  @Test(expected=RuntimeException.class)
  public void testMissingNoDefault() {
    Env.envOrProperty(prop);
  }
  
  @Test
  public void testMissingWithDefault() {
    assertEquals("a", Env.envOrProperty(prop, "a"));
  }
  
  @Test
  public void testWithProperty() {
    System.setProperty(prop, "b");
    assertEquals("b", Env.envOrProperty(prop));
  }
  
  private static String prop() {
    return String.format("%s.%s", EnvTest.class.getName(), UUID.randomUUID().toString());
  }
}
