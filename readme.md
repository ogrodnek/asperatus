asperatus
=========

simple [CloudWatch](http://aws.amazon.com/cloudwatch/) metrics API with local aggregation and batching


why
----

Asperatus provides:

* Simple API
* Local aggregation
* Request batching

### Simple API
     tracker.track("MyMetric", 1, myDimensions);
     
### Local Aggregation

  Asperatus performs local metric aggregation over a configurable timeframe (defaults to 60 seconds) before pushing to CloudWatch.  I.e. if you call .track a million times in the same minute for the same metric, it will only result in a single call to CloudWatch.  Since you pay per API call, and CloudWatch only supports granularity of 1 minute, this is a big win.  Note that this doesn't affect your number of samples, min, max, etc. as CloudWatch supports accepting locally aggregated data.

### Request batching

  In addition to local aggregation, CloudWatch support batch metric update calls.  That is, multiple metric updates can happen in the same call, reducing the number of API calls to Amazon.  Asperatus handles this automatically.


using
-----

     tracker.track("MyMetric", 1, myDimensions);
     
_TODO, talk more about CW namespace config, etc._

history
-------

When we first starting using CloudWatch for custom metrics, there was no client library, so we put together a really simple one.

After CloudWatch support was added to the official SDK, we switched out our REST client, but decided to keep our API for the simplicity, local aggregation and request batching, and the standardization on how we set up CloudWatch namespaces across application/stage/region.

You can read more about how we're using CloudWatch on our development blog:

* [Cloudwatch metrics revisited](http://dev.bizo.com/2011/08/cloudwatch-metrics-revisited.html)
* [Cloudwatch custom metrics @ Bizo](http://dev.bizo.com/2011/05/cloudwatch-custom-metrics-bizo.html)



license
-------
Copyright (c) 2012 Larry Ogrodnek, Bizo.com
Published under the Apache Software License 2.0, see LICENSE