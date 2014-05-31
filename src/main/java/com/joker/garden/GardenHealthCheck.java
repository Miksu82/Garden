package com.joker.garden;

import com.codahale.metrics.health.HealthCheck;

public class GardenHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		return Result.healthy("All good");
	}

}
