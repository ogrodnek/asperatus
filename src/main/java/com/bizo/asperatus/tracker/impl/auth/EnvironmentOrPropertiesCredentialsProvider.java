package com.bizo.asperatus.tracker.impl.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.auth.AWSCredentialsProvider;
import static com.bizo.asperatus.tracker.Env.envOrProperty;

public final class EnvironmentOrPropertiesCredentialsProvider implements AWSCredentialsProvider {
  private final String accessKeyIdentifer;
  private final String secretKeyIdentifier;
  
  public EnvironmentOrPropertiesCredentialsProvider(final String accessKeyIdentifier, final String secretKeyIdentifier) {
    this.accessKeyIdentifer = accessKeyIdentifier;
    this.secretKeyIdentifier = secretKeyIdentifier;
  }
  
  @Override
  public AWSCredentials getCredentials() {
    return new BasicAWSCredentials(envOrProperty(accessKeyIdentifer), envOrProperty(secretKeyIdentifier));
  }

  @Override
  public void refresh() {
  }
}
