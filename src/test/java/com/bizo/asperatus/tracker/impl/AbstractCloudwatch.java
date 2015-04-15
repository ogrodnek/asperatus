package com.bizo.asperatus.tracker.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.DeleteAlarmsRequest;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmHistoryRequest;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmHistoryResult;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsForMetricRequest;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsForMetricResult;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsRequest;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsResult;
import com.amazonaws.services.cloudwatch.model.DisableAlarmActionsRequest;
import com.amazonaws.services.cloudwatch.model.EnableAlarmActionsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.PutMetricAlarmRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.SetAlarmStateRequest;

/**
 * Throws UnsupportedOperationException for everything.
 */
public class AbstractCloudwatch implements AmazonCloudWatch {
  @Override
  public void setEndpoint(String endpoint) throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void putMetricAlarm(PutMetricAlarmRequest putMetricAlarmRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void putMetricData(PutMetricDataRequest putMetricDataRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListMetricsResult listMetrics(ListMetricsRequest listMetricsRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetMetricStatisticsResult getMetricStatistics(GetMetricStatisticsRequest getMetricStatisticsRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void disableAlarmActions(DisableAlarmActionsRequest disableAlarmActionsRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public DescribeAlarmsResult describeAlarms(DescribeAlarmsRequest describeAlarmsRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public DescribeAlarmsForMetricResult describeAlarmsForMetric(
      DescribeAlarmsForMetricRequest describeAlarmsForMetricRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public DescribeAlarmHistoryResult describeAlarmHistory(DescribeAlarmHistoryRequest describeAlarmHistoryRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void enableAlarmActions(EnableAlarmActionsRequest enableAlarmActionsRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteAlarms(DeleteAlarmsRequest deleteAlarmsRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAlarmState(SetAlarmStateRequest setAlarmStateRequest)
      throws AmazonServiceException,
      AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListMetricsResult listMetrics() throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public DescribeAlarmsResult describeAlarms() throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public DescribeAlarmHistoryResult describeAlarmHistory() throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void shutdown() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setRegion(Region region) throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }
}
