package com.joker.garden;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.joker.garden.entrypoint.GardenResource;
import com.joker.garden.persistance.GardenStorage;

public class GardenApplication extends Application<GardenConfiguration> {

	public static void main(String[] args) throws Exception {
        new GardenApplication().run(args);
    }
	
	@Override
    public String getName() {
        return "GardenServer";
    }

	
	@Override
	public void initialize(Bootstrap<GardenConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/assets/", "/foo/", "index.html"));	
	}

	@Override
	public void run(GardenConfiguration configuration, Environment environment) {
	    final GardenResource resource = new GardenResource(new GardenStorage(configuration.getAwsAccessKey(), configuration.getAwsSecret()));
	    final GardenHealthCheck healthCheck = new GardenHealthCheck();
	    
	    environment.jersey().register(resource);
	    environment.healthChecks().register("healthcheck", healthCheck);
	}

}
