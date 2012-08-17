package com.bizo.asperatus.tracker.impl.buffer;

import java.util.Map;

import com.bizo.asperatus.tracker.MetricTracker;
import com.bizo.asperatus.tracker.impl.MetricKey;
import com.bizo.asperatus.tracker.impl.MetricStatistics;

public interface MetricBuffer extends MetricTracker {
  Map<MetricKey, MetricStatistics> reset();
}
