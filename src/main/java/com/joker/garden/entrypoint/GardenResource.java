package com.joker.garden.entrypoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.joker.garden.GardenUtils;
import com.joker.garden.model.GardenDataResponse;
import com.joker.garden.model.GardenMeasurement;
import com.joker.garden.persistance.GardenStorage;
import com.sun.jersey.api.core.HttpContext;

@Path("garden")
public class GardenResource {

	private GardenStorage mStorage;
	
	public GardenResource(GardenStorage storage) {
		mStorage = storage;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public GardenMeasurement postMeasuremnt(@Context HttpContext context, @QueryParam("hid") int householdId) {
		try {
			GardenMeasurement measurement = GardenUtils.populateBean(context.getUriInfo().getQueryParameters(), GardenMeasurement.class);
			long now = System.currentTimeMillis();
			measurement.setDate(GardenUtils.getDate(now));
			measurement.setHour(GardenUtils.getHour(now));
			return mStorage.putGardenMeasurement(householdId, measurement);		
		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println("error " + t.getMessage());
		}
		return null;
	}
	
	@GET
	@Path("query")
	@Produces({MediaType.APPLICATION_JSON})
	public GardenDataResponse getDataInRange(@QueryParam("hid") Integer id, @QueryParam("start") Long start, @QueryParam("end") Long end) {
		try {
			if (start == null || end == null) {
				return mStorage.getAllData(id);
			} else {
				return mStorage.getDataInRange(id, start, end);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println("error " + t.getMessage());
		}
		
		return null;
	}
}
