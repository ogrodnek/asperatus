package com.bizo.asperatus.tracker.impl.auth;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

/**
 * Basically a clone of Amazon's DefaultAWSCredentialsProvider, but unfortunately we use slightly
 * different ENV names at Bizo for specifying credentials.
 * 
 * First it tries: "AWS_ACCESS_KEY_ID", "AWS_SECRET_ACCESS_KEY", then will attempt to hit the default chain.  
 * 
 * https://github.com/amazonwebservices/aws-sdk-for-java/blob/master/src/main/java/com/amazonaws/auth/DefaultAWSCredentialsProviderChain.java
 */
public final class AsperatusDefaultCredentialsProvider extends AWSCredentialsProviderChain {
  public AsperatusDefaultCredentialsProvider() {
    super(new EnvironmentOrPropertiesCredentialsProvider("AWS_ACCESS_KEY_ID", "AWS_SECRET_ACCESS_KEY"),
      new DefaultAWSCredentialsProviderChain());
  }
}