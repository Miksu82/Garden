#include "MoistureSensor.h"

#define DELAY 100
//#define DEBUG 0

MoistureSensor::MoistureSensor(int pin1, int pin2, int sensorPin, int calibrationConstant, float probeDistance, float soilConductivity, int moisture) {
	_pin1 = pin1;
	_pin2 = pin2;
	_sensorPin = sensorPin;
	_calibrationConstant = calibrationConstant;
	
	pinMode(_pin1, OUTPUT);
	pinMode(_pin2, OUTPUT);
	pinMode(_sensorPin, INPUT);
	
	digitalWrite(_pin1, LOW);
	digitalWrite(_pin2, LOW);
	
	float soilResistanceBetweenProbes = 1.0 / soilConductivity * probeDistance;
	_slope = (float)moisture / soilResistanceBetweenProbes;
}

int MoistureSensor::readMoisture(long timeInMillis) {
	long now = millis();
	int value = 0;
	int count = 0;
	while ((millis() - now) < timeInMillis) {
		setSensorPolarity(true);
		delay(DELAY);
		int val1 = calculateMoisture(analogRead(_sensorPin));
		setSensorPolarity(false);
		delay(DELAY);
		// invert the reading
		int val2 = calculateMoisture(1023 - analogRead(_sensorPin));
		value += (val1 + val2)/2;
		count++;
		
#ifdef DEBUG
		int avg = (val1 + val2)/2;
	    String msg = ", avg: ";
	    msg += avg;
	     Serial.print("val1: ");
	     Serial.print(val1);
	     Serial.print(", val2: ");
	     Serial.print(val2);
	     Serial.println(msg);
#endif
	}
	
	digitalWrite(_pin1, LOW);
	digitalWrite(_pin2, LOW);
	
	if (count == 0) {
		return 0;
	} else {
		return value/count;
	}
}

int MoistureSensor::calculateMoisture(int sensorVal) {
	float voltage =  sensorVal / 1023.0 * 5.0;
	float resistance = (voltage * _calibrationConstant) / (5.0 - voltage);
	int moisture = _slope * resistance;
	if (moisture > 100) {
		return 100;
	}
	
	return moisture;
}

void MoistureSensor::setSensorPolarity(boolean flip) {
	if (flip) {
		digitalWrite(_pin1, HIGH);
		digitalWrite(_pin2, LOW);
	} else {
		digitalWrite(_pin1, LOW);
		digitalWrite(_pin2, HIGH);
	}
}