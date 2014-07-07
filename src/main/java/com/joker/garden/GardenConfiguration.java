package com.joker.garden;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class GardenConfiguration extends Configuration {
   
	@NotEmpty
	private String awsAccessKey;
	
	@NotEmpty
	private String awsSecret;

    @JsonProperty
	public String getAwsAccessKey() {
		return awsAccessKey;
	}

    @JsonProperty
	public void setAwsAccessKey(String awsAccessKey) {
		this.awsAccessKey = awsAccessKey;
	}
    
    @JsonProperty
	public String getAwsSecret() {
		return awsSecret;
	}

    @JsonProperty
	public void setAwsSecret(String awsSecret) {
		this.awsSecret = awsSecret;
	}
}
