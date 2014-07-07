package com.joker.garden.model;

import java.util.ArrayList;
import java.util.List;

public class GardenDataResponse {
	private int householdId;
	private String householdLabel;
	
	private String moisture1Label;
	private String moisture2Label;
	private String moisture3Label;
	
	private String temperature1Label;
	private String temperature2Label;
	
	List<GardenMeasurement> measurements;

	public int getHouseholdId() {
		return householdId;
	}

	public void setHouseholdId(int householdId) {
		this.householdId = householdId;
	}

	public String getHouseholdLabel() {
		return householdLabel;
	}

	public void setHouseholdLabel(String householdLabel) {
		this.householdLabel = householdLabel;
	}

	public String getMoisture1Label() {
		return moisture1Label;
	}

	public void setMoisture1Label(String moisture1Label) {
		this.moisture1Label = moisture1Label;
	}

	public String getMoisture2Label() {
		return moisture2Label;
	}

	public void setMoisture2Label(String moisture2Label) {
		this.moisture2Label = moisture2Label;
	}

	public String getMoisture3Label() {
		return moisture3Label;
	}

	public void setMoisture3Label(String moisture3Label) {
		this.moisture3Label = moisture3Label;
	}

	public String getTemperature1Label() {
		return temperature1Label;
	}

	public void setTemperature1Label(String temperature1Label) {
		this.temperature1Label = temperature1Label;
	}

	public String getTemperature2Label() {
		return temperature2Label;
	}

	public void setTemperature2Label(String temperature2Label) {
		this.temperature2Label = temperature2Label;
	}

	public List<GardenMeasurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<GardenMeasurement> measurements) {
		this.measurements = measurements;
	}
	
	public void addMeasurement(GardenMeasurement measurement) {
		if (measurements == null) {
			measurements = new ArrayList<GardenMeasurement>();
		}
		
		measurements.add(measurement);
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + householdId;
		result = prime * result
				+ ((householdLabel == null) ? 0 : householdLabel.hashCode());
		result = prime * result
				+ ((measurements == null) ? 0 : measurements.hashCode());
		result = prime * result
				+ ((moisture1Label == null) ? 0 : moisture1Label.hashCode());
		result = prime * result
				+ ((moisture2Label == null) ? 0 : moisture2Label.hashCode());
		result = prime * result
				+ ((moisture3Label == null) ? 0 : moisture3Label.hashCode());
		result = prime
				* result
				+ ((temperature1Label == null) ? 0 : temperature1Label
						.hashCode());
		result = prime
				* result
				+ ((temperature2Label == null) ? 0 : temperature2Label
						.hashCode());
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
		GardenDataResponse other = (GardenDataResponse) obj;
		if (householdId != other.householdId)
			return false;
		if (householdLabel == null) {
			if (other.householdLabel != null)
				return false;
		} else if (!householdLabel.equals(other.householdLabel))
			return false;
		if (measurements == null) {
			if (other.measurements != null)
				return false;
		} else if (!measurements.equals(other.measurements))
			return false;
		if (moisture1Label == null) {
			if (other.moisture1Label != null)
				return false;
		} else if (!moisture1Label.equals(other.moisture1Label))
			return false;
		if (moisture2Label == null) {
			if (other.moisture2Label != null)
				return false;
		} else if (!moisture2Label.equals(other.moisture2Label))
			return false;
		if (moisture3Label == null) {
			if (other.moisture3Label != null)
				return false;
		} else if (!moisture3Label.equals(other.moisture3Label))
			return false;
		if (temperature1Label == null) {
			if (other.temperature1Label != null)
				return false;
		} else if (!temperature1Label.equals(other.temperature1Label))
			return false;
		if (temperature2Label == null) {
			if (other.temperature2Label != null)
				return false;
		} else if (!temperature2Label.equals(other.temperature2Label))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GardenDataResponse2 [householdId=" + householdId
				+ ", householdLabel=" + householdLabel + ", moisture1Label="
				+ moisture1Label + ", moisture2Label=" + moisture2Label
				+ ", moisture3Label=" + moisture3Label + ", temperature1Label="
				+ temperature1Label + ", temperature2Label="
				+ temperature2Label + ", measurements=" + measurements + "]";
	}
	
	
}
