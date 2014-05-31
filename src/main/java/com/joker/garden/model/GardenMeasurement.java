package com.joker.garden.model;

import javax.ws.rs.QueryParam;

public class GardenMeasurement {

	@QueryParam("m1")
	int moisture1;
	
	@QueryParam("m2")
	int moisture2;
	
	@QueryParam("m3")
	int moisture3;
	
	@QueryParam("t1")
	float temperature1;
	
	@QueryParam("t2")
	float temperature2;
	
	String date;
	String hour;
	
	public int getMoisture1() {
		return moisture1;
	}
	public void setMoisture1(int moisture1) {
		this.moisture1 = moisture1;
	}
	public int getMoisture2() {
		return moisture2;
	}
	public void setMoisture2(int moisture2) {
		this.moisture2 = moisture2;
	}
	public int getMoisture3() {
		return moisture3;
	}
	public void setMoisture3(int moisture3) {
		this.moisture3 = moisture3;
	}
	public float getTemperature1() {
		return temperature1;
	}
	public void setTemperature1(float temperature1) {
		this.temperature1 = temperature1;
	}
	public float getTemperature2() {
		return temperature2;
	}
	public void setTemperature2(float temperature2) {
		this.temperature2 = temperature2;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	
	public String combineDateAndHour() {
		return String.format("%s-%s", date, hour);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((hour == null) ? 0 : hour.hashCode());
		result = prime * result + moisture1;
		result = prime * result + moisture2;
		result = prime * result + moisture3;
		result = prime * result + Float.floatToIntBits(temperature1);
		result = prime * result + Float.floatToIntBits(temperature2);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GardenMeasurement other = (GardenMeasurement) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (hour == null) {
			if (other.hour != null)
				return false;
		} else if (!hour.equals(other.hour))
			return false;
		if (moisture1 != other.moisture1)
			return false;
		if (moisture2 != other.moisture2)
			return false;
		if (moisture3 != other.moisture3)
			return false;
		if (Float.floatToIntBits(temperature1) != Float
				.floatToIntBits(other.temperature1))
			return false;
		if (Float.floatToIntBits(temperature2) != Float
				.floatToIntBits(other.temperature2))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "GardenMeasurement [moisture1=" + moisture1 + ", moisture2="
				+ moisture2 + ", moisture3=" + moisture3 + ", temperature1="
				+ temperature1 + ", temperature2=" + temperature2 + ", date="
				+ date + ", hour=" + hour + "]";
	}
}
