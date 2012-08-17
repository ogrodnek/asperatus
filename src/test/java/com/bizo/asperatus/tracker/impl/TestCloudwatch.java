package com.bizo.asperatus.tracker.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;


public final class TestCloudwatch extends AbstractCloudwatch {
  Map<String, List<Aggregation>> aggregations = new HashMap<String, List<Aggregation>>();
  
  @Override
  public void putMetricData(final PutMetricDataRequest putMetricDataRequest) {
    
    List<Aggregation> existing = aggregations.get(putMetricDataRequest.getNamespace());
    if (existing == null) {
      existing = new ArrayList<Aggregation>();
      aggregations.put(putMetricDataRequest.getNamespace(), existing); 
    }
    
    existing.addAll(AggregationUtils.toAggregation(putMetricDataRequest.getMetricData()));
  }
}