package com.bizo.asperatus.logging.error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bizo.asperatus.model.Dimension;

public final class MachineInfo {

  private MachineInfo() { }

  /** Map of Application -> $APPLICATION, etc. */
  public static final Map<String, String> info = Collections.unmodifiableMap(getMachineInfo());

  /** List of all machine info: application, stage, instance id, region, and version. */
  private static final List<Dimension> allDimensions = Collections.unmodifiableList(toDimensions(info));
  
  /** List of default dimensions for tracking: Application, Version */
  public static final List<Dimension> defaultDimensions =
    Collections.unmodifiableList(with("Application", "Version").toDimensions());
  
  public static MachineInfoBuilder with(final String... includes) {
    return new MachineInfoBuilder().with(includes);
  }
  
  public static MachineInfoBuilder with(final Collection<String> includes) {
    return new MachineInfoBuilder().with(includes);
  }
  
  public static MachineInfoBuilder without(final String... excludes) {
    return new MachineInfoBuilder().without(excludes);
  }
  
  public static MachineInfoBuilder without(final Collection<String> excludes) {
    return new MachineInfoBuilder().without(excludes);
  }

  @SuppressWarnings("serial")
  private static final Map<String, String> ENV_MAPPING() {
    return new HashMap<String, String>() {
      {
        put("APPLICATION", "Application");
        put("STAGE", "Stage");
        put("EC2_INSTANCE_ID", "InstanceId");
        put("EC2_REGION", "Region");
        put("VERSION", "Version");
      }
    };
  }
  
  private static final Map<String, String> getMachineInfo() {
    final Map<String, String> info = new HashMap<String, String>();
    for (final Map.Entry<String, String> env : ENV_MAPPING().entrySet()) {
      final String val = System.getProperty(env.getKey(), System.getenv(env.getKey()));
      if (val != null) {
        info.put(env.getValue(), val);
      }
    }
    return info;
  }
  
  private static final List<Dimension> toDimensions(final Map<String, String> info) {
    final List<Dimension> dims = new ArrayList<Dimension>(info.size());
    for (final Map.Entry<String, String> env : info.entrySet()) {
      dims.add(new Dimension(env.getKey(), env.getValue()));
    }
    return dims;
  }

  public static final class MachineInfoBuilder {
    private final Collection<Dimension> defaults;
    
    private Set<String> includes = new HashSet<String>();
    private Set<String> excludes = new HashSet<String>();
    
    MachineInfoBuilder(final Collection<Dimension> defaults) {
      this.defaults = defaults;
    }
    
    MachineInfoBuilder() {
      this(MachineInfo.allDimensions);
    }
    
    public MachineInfoBuilder with(final Collection<String> includes) {
      this.includes.addAll(includes);
      return this;
    }
    
    public MachineInfoBuilder without(final Collection<String> excludes) {
      this.excludes.addAll(excludes);
      return this;
    }
    
    public MachineInfoBuilder with(final String... includes) {
      this.includes.addAll(Arrays.asList(includes));
      return this;
    }
    
    public MachineInfoBuilder without(final String... excludes) {
      this.excludes.addAll(Arrays.asList(excludes));
      return this;
    }    
    
    public List<Dimension> toDimensions() {
      final List<Dimension> ret = new ArrayList<Dimension>(includes.size());
      
      for (final Dimension d : defaults) {
        if (includes.contains(d.getName()) && (! excludes.contains(d.getName()))) {
          ret.add(d);
        }
      }
      
      return ret;
    }
  }
}
