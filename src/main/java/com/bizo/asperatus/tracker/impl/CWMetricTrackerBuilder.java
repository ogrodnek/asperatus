package com.bizo.asperatus.tracker.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.bizo.asperatus.tracker.Env;
import com.bizo.asperatus.tracker.MetricTracker;
import com.bizo.asperatus.tracker.impl.auth.AsperatusDefaultCredentialsProvider;

public final class CWMetricTrackerBuilder {
  // this is required - must be set
  private String application;

  // these have defaults
  private String stage = Env.envOrProperty("STAGE", "dev");
  private String region = Env.envOrProperty("EC2_REGION", "us-east-1");
  
  private ScheduledExecutorService executor;
  private long flushDelay = 60;
  private TimeUnit flushUnit = TimeUnit.SECONDS;

  private AWSCredentialsProvider credentialsProvider = new AsperatusDefaultCredentialsProvider();

  public MetricTracker toMetricTracker() {
    if (application == null) {
      throw new IllegalStateException("Must set application before constructing.");
    }

    final String namespace = String.format("%s-%s", application, stage);
    final String endpoint = String.format("monitoring.%s.amazonaws.com", region);
    
    if (executor == null) {
      executor = Executors.newScheduledThreadPool(5,
        ThreadFactoryUtils.namedDaemonThreadFactory("asperatus-metrics"));
    }
    
    final AmazonCloudWatch cloudwatch = new AmazonCloudWatchClient(credentialsProvider);
    cloudwatch.setEndpoint(endpoint);
    
    return new CWMetricTracker(cloudwatch, namespace, executor, flushDelay, flushUnit);
  }

  public AWSCredentialsProvider getCredentialsProvider() {
    return credentialsProvider;
  }

  public CWMetricTrackerBuilder withCredentialsProvider(final AWSCredentialsProvider credentialsProvider) {
    this.credentialsProvider = credentialsProvider;
    return this;
  }

  public String getApplication() {
    return application;
  }

  public CWMetricTrackerBuilder withApplication(final String application) {
    this.application = application;
    return this;
  }

  public String getStage() {
    return stage;
  }

  public CWMetricTrackerBuilder withStage(final String stage) {
    this.stage = stage;
    return this;
  }

  public ScheduledExecutorService getExecutor() {
    return executor;
  }

  public CWMetricTrackerBuilder withExecutor(final ScheduledExecutorService executor) {
    this.executor = executor;
    return this;
  }

  public long getFlushDelay() {
    return flushDelay;
  }
  
  public TimeUnit getFlushUnit() {
    return flushUnit;
  }  

  public CWMetricTrackerBuilder withFlushDelay(final long flushDelay, final TimeUnit flushUnit) {
    this.flushDelay = flushDelay;
    this.flushUnit = flushUnit;
    return this;
  }

  public String getRegion() {
    return region;
  }

  public CWMetricTrackerBuilder withRegion(final String region) {
    this.region = region;
    return this;
  }
}
