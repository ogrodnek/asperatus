/**
 * This package contains classes for calculating the frequency of an event.
 * 
 * For example, to track the request rate (req/sec), an application call track on a RateTracker for each request.
 * Periodically, the application can then retrieve from the RateTracker the request rate (in req/sec) since the last
 * time the rate was retrieved.  This number can then be passed to a standard Asperatus MetricTracker for persistence.
 * 
 * The AsperatusRateTrackerScheduler is a convenience class designed to schedule this periodic data delivery to the 
 * MetricTracker.  Thus, an application can set up rate tracking by simply ensuring that RateTracker.track is called
 * once per request and configuring an AsperatusRateTrackerScheduler with the target Asperatus metric tracker and 
 * information (metric name and dimensions).
 * 
 * Sample code:
 * <code>
 * public class MyRequestHandler {
 *   
 *   private static final RateTracker rateTracker = new RateTracker();
 *   
 *   public Response handleRequest(Request req) {
 *     rateTracker.track();
 *     // handle the request...
 *   }
 *   
 *   public static void main(String[] args) {
 *     RateTracker rateTracker = new RateTracker();
 *     MetricTracker metricTracker = new MetricTracker();
 *
 *     AsperatusRateTrackerScheduler scheduler = 
 *       new AsperatusRateTrackerScheduler(metricTracker, "metricName", MachineInfo.defaultDimensions, rateTracker);
 *     scheduler.start();
 *     
 *     // continue to start the rest of the application...
 *   }
 *  
 * }
 * </code>
 */
package com.bizo.asperatus.rates;

