package com.bizo.asperatus.tracker;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

public final class Env {
  private static final Logger logger = Logger.getLogger(Env.class.getName());
  
  private Env() { }
  
  
  public static String envOrProperty(final String name) {  
    return envOrProperty(name, null);
  }

  public static String envOrProperty(final String name, final String def) {
    final String val = System.getProperty(name, System.getenv(name));
    
    if (StringUtils.isBlank(val)) {
      if (StringUtils.isBlank(def)) {
        throw new RuntimeException("Missing required property: " + name);
      }
      
      logger.log(Level.WARNING, String.format("Warning, missing property %s, using default of %s", name, def));
      return def;      
    }
    
    return val;
  }
}
