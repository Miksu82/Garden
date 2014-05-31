package com.joker.garden.entrypoint.test;

import static org.mockito.Mockito.*;
import static org.fest.assertions.api.Assertions.assertThat;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.io.IOException;
import java.util.Calendar;

import javax.ws.rs.WebApplicationException;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.joker.garden.GardenUtils;
import com.joker.garden.entrypoint.GardenResource;
import com.joker.garden.model.GardenDataResponse;
import com.joker.garden.model.GardenMeasurement;
import com.joker.garden.persistance.GardenStorage;

public class GardenResourceTest {

	private static final GardenStorage mockStorage = mock(GardenStorage.class);

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
	.addResource(new GardenResource(mockStorage))
	.build();

	private final GardenMeasurement mMes;
	public GardenResourceTest() {
		mMes = new GardenMeasurement();
		mMes.setMoisture1(1);
		mMes.setMoisture2(2);
		mMes.setMoisture3(3);
		mMes.setTemperature1(4.2f);
		mMes.setTemperature2(5.7f);
		mMes.setDate(GardenUtils.getDate(System.currentTimeMillis()));
		mMes.setHour(GardenUtils.getHour(System.currentTimeMillis()));
	}

	@Before
	public void setup() throws IOException {
		when(mockStorage.putGardenMeasurement(1,mMes)).thenReturn(mMes);
		when(mockStorage.getAllData(1)).thenReturn(new GardenDataResponse());
		when(mockStorage.getDataInRange(1, 2, 34)).thenReturn(new GardenDataResponse());
	}

	@Test
	public void testPutMeasurement() {
		assertThat(resources.client().resource("/garden/?hid=1&m1=1&m2=2&m3=3&t1=4.2&t2=5.7").
				get(GardenMeasurement.class)).isEqualTo(mMes);

		verify(mockStorage).putGardenMeasurement(1, mMes);
	}
	
	@Test
	public void testGetAllData() throws IOException {
		resources.client().resource("/garden/query?hid=1").get(GardenDataResponse.class);
		verify(mockStorage).getAllData(1);
	}
	
	@Test 
	public void testGetDataInRange() throws IOException {
		resources.client().resource("/garden/query?hid=1&start=2&end=34").get(GardenDataResponse.class);
		verify(mockStorage).getDataInRange(1, 2, 34);
	}
	
	
	/*@Test
	public void testPutData() {
		GardenStorage storage = new GardenStorage();
		for (int i = -100*48; i < 100*48; i++) {
			long now = System.currentTimeMillis();
			long time = now + (long)i*60L*60L*1000L;
			GardenMeasurement mes = new GardenMeasurement();
			mes.setMoisture1(i);
			mes.setMoisture2(i+1);
			mes.setMoisture3(i+3);
			mes.setTemperature1(34.6f);
			mes.setTemperature2(23.0f);
			mes.setDate(GardenUtils.getDate(time));
			mes.setHour(GardenUtils.getHour(time));
			storage.putGardenMeasurement(1, mes);
			//System.out.println(mes);
		}
	}

	@Test 
	public void testGetData() throws IOException {
		GardenStorage storage = new GardenStorage();
		GardenDataResponse response = storage.getAllData(1);
		System.out.println(response);

	}*/

	/*@Test 
	public void testGetDataInRange() throws IOException {
		GardenStorage storage = new GardenStorage();
		GardenDataResponse2 response = storage.getDataInRange(1, System.currentTimeMillis()-21*60*60*1000, System.currentTimeMillis());
		System.out.println(response);
	}*/
}

