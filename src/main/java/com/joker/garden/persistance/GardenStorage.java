package com.joker.garden.persistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.joker.garden.GardenUtils;
import com.joker.garden.model.GardenDataResponse;
import com.joker.garden.model.GardenMeasurement;

public class GardenStorage {

	private static final String HOUSEHOLD_DOMAIN = "household";
	private static final String SENSOR_DOMAIN = "sensor";
	private static final String VALUE_DOMAIN = "values";

	private AmazonSimpleDBClient client;

	public GardenStorage(String awsAccessKey, String awsSecret) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecret);
		client = new AmazonSimpleDBClient(credentials);
	}

	public GardenMeasurement putGardenMeasurement(int householdId, GardenMeasurement measurement) {	
		List<ReplaceableAttribute> values = new ArrayList<ReplaceableAttribute>();
		values.add(new ReplaceableAttribute().withName("m1").withValue(Integer.toString(measurement.getMoisture1())));
		values.add(new ReplaceableAttribute().withName("m2").withValue(Integer.toString(measurement.getMoisture2())));
		values.add(new ReplaceableAttribute().withName("m3").withValue(Integer.toString(measurement.getMoisture3())));
		values.add(new ReplaceableAttribute().withName("t1").withValue(Float.toString(measurement.getTemperature1())));
		values.add(new ReplaceableAttribute().withName("t2").withValue(Float.toString(measurement.getTemperature2())));
		values.add(new ReplaceableAttribute().withName("hid").withValue(Integer.toString(householdId)));
		values.add(new ReplaceableAttribute().withName("date").withValue(measurement.combineDateAndHour()));

		PutAttributesRequest request = new PutAttributesRequest(VALUE_DOMAIN, getItemName(householdId, measurement.getDate(), measurement.getHour()), values);
		client.putAttributes(request);

		return measurement;
	}

	public GardenDataResponse getAllData(int householdId) throws IOException {
		return getDataInRange(householdId, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	public GardenDataResponse getDataInRange(int householdId, long start, long end) throws IOException {
		SelectRequest householdRequest = new SelectRequest("select * from " + HOUSEHOLD_DOMAIN + " where hid = '" + householdId + "'");
		SelectResult householdResult = client.select(householdRequest);
		List<Item> householdItems = householdResult.getItems();
		if (householdItems.size() != 1) {
			throw new IOException("Invalid household request response of size " + householdItems.size());
		}

		String householdLabel = householdItems.get(0).getName();

		SelectRequest sensorRequest = new SelectRequest("select * from " + SENSOR_DOMAIN + " where hid = '" + householdId + "'");
		SelectResult sensorResult = client.select(sensorRequest);
		List<Item> sensorItems = sensorResult.getItems();
		if (sensorItems.size() != 5) {
			throw new IOException("Invalid sensor request response of size " + sensorItems.size());
		}

		Map<Integer, String> sensorMap = new HashMap<Integer, String>();
		for (Item sensorItem : sensorItems) {
			List<Attribute> sensorAttributes = sensorItem.getAttributes();
			Map<String, String> attributeMap = convertAttributeListToMap(sensorAttributes);
			sensorMap.put(Integer.parseInt(attributeMap.get("sid")), attributeMap.get("label"));
		}

		SelectResult dataResult = null;
		GardenDataResponse response = new GardenDataResponse();		

		response.setHouseholdId(householdId);
		response.setHouseholdLabel(householdLabel);
		response.setMoisture1Label(sensorMap.get(1));
		response.setMoisture2Label(sensorMap.get(2));
		response.setMoisture3Label(sensorMap.get(3));
		response.setTemperature1Label(sensorMap.get(4));
		response.setTemperature2Label(sensorMap.get(5));

		do {
			SelectRequest dataRequest = null;
			StringBuffer query =  new StringBuffer("select * from " + VALUE_DOMAIN + " where hid = '" + householdId + "'");
			if (start != Long.MIN_VALUE && end != Long.MAX_VALUE) {
				String startDate = GardenUtils.getDate(start);
				String startHour = GardenUtils.getHour(start);
				String endDate = GardenUtils.getDate(end);
				String endHour = GardenUtils.getHour(end);
				query.append(" and date >= '");
				query.append(startDate);
				query.append("-");
				query.append(startHour);
				query.append("' and date <= '");
				query.append(endDate);
				query.append("-");
				query.append(endHour);
				query.append("'");
			}

			dataRequest = new SelectRequest(query.toString());

			if (dataResult != null) {  
				dataRequest.setNextToken(dataResult.getNextToken());
			}

			dataResult = client.select(dataRequest);
			List<Item> dataItems = dataResult.getItems();

			for (Item dataItem : dataItems) {
				GardenMeasurement measurement = new GardenMeasurement();
				List<Attribute> attributes = dataItem.getAttributes();
				Map<String, String> attributeMap = convertAttributeListToMap(attributes);
				measurement.setMoisture1(Integer.parseInt(attributeMap.get("m1")));
				measurement.setMoisture2(Integer.parseInt(attributeMap.get("m2")));
				measurement.setMoisture3(Integer.parseInt(attributeMap.get("m3")));
				measurement.setTemperature1(Float.parseFloat(attributeMap.get("t1")));
				measurement.setTemperature2(Float.parseFloat(attributeMap.get("t2")));
				setDate(attributeMap.get("date"), measurement);
				response.addMeasurement(measurement);				
			}

		} while (dataResult.getNextToken() != null);

		return response;
	}

	private void setDate(String dateValue, GardenMeasurement measurement) {
		String[] parts = dateValue.split("-");
		measurement.setDate(dateValue.substring(0, dateValue.length() - 3));
		measurement.setHour(parts[3]);
	}

	private String getItemName(int householdId, String date, String hour) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(householdId);
		buffer.append(":");
		buffer.append(date);
		buffer.append(":");
		buffer.append(hour);
		return buffer.toString();
	}

	private Map<String, String> convertAttributeListToMap(List<Attribute> attributes) {
		Map<String, String> attributeMap = new HashMap<String, String>();
		for (Attribute attribute : attributes) {
			attributeMap.put(attribute.getName(), attribute.getValue());
		}
		return attributeMap;
	}
}
