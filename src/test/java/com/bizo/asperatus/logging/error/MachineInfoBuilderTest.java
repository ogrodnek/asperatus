package com.bizo.asperatus.logging.error;

import static org.junit.Assert.assertEquals;
import static com.google.common.collect.Lists.newArrayList;
import java.util.List;

import org.junit.Test;

import com.bizo.asperatus.logging.error.MachineInfo.MachineInfoBuilder;
import com.bizo.asperatus.model.Dimension;

public final class MachineInfoBuilderTest {
  @Test
  public void test() {
    final List<Dimension> dims = newArrayList(new Dimension("a", "1"), new Dimension("b", "2"));
    
    assertEquals(dims, new MachineInfoBuilder(dims).with("a", "b").toDimensions());
    assertEquals(newArrayList(new Dimension("a", "1")), new MachineInfoBuilder(dims).with("a").toDimensions());    
    assertEquals(newArrayList(new Dimension("a", "1")), new MachineInfoBuilder(dims).with("a", "b").without("b").toDimensions());        
    assertEquals(newArrayList(new Dimension("b", "2")), new MachineInfoBuilder(dims).with("b").toDimensions());            
  }
}
