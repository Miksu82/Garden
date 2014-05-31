#ifndef MOISTURE_SENSOR_H
#define MOISTURE_SENSOR_H

#include "Arduino.h"

class MoistureSensor {
public:
	MoistureSensor(int pin1, int pin2, int sensorPin, int voltageDividerResistance, float probeDistance, float soilConductivity = 0.014, int moisture = 40);
	int readMoisture(long timeInMillis);
private:
	int _pin1;
	int _pin2;
	int _sensorPin;
	int _calibrationConstant;
	float _slope;
	
	void setSensorPolarity(boolean flip);
	int calculateMoisture(int sensorVal);
};

#endif