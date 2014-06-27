#ifndef TEMPERATURE_SENSOR_H
#define TEMPERATURE_SENSOR_H

#include "Arduino.h"

class TemperatureSensor {
public:
	TemperatureSensor(int pin1, int sensorPin);
	float readTemperature(long timeInMillis);
private:
	int _pin1;
	int _sensorPin;
};

#endif