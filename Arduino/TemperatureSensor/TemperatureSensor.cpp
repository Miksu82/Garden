#include "TemperatureSensor.h"
	
TemperatureSensor::TemperatureSensor(int pin1, int sensorPin) {
	_pin1 = pin1;
	_sensorPin = sensorPin;
	
	pinMode(_pin1, OUTPUT);	
	digitalWrite(_pin1, LOW);
}

float TemperatureSensor::readTemperature(long timeInMillis) {
	digitalWrite(_pin1, HIGH);	
	delay(1000);
	long now = millis();
	float value = 0.0;
	int count = 0;
	while ((millis() - now) < timeInMillis) {
		int sensorVal = analogRead(_sensorPin);
		//Serial.print("temp sensor val: ");
		//Serial.println(sensorVal);
		
		float voltage = (sensorVal / 1024.0) * 5.0;
		
		//Serial.print("voltage: ");
		//Serial.println(voltage);
		float tempValue = (voltage - 0.5) * 100;
		if (count == 0) {
			value = tempValue;
		} else {
			value = 0.95*tempValue + 0.05*value;
		}
		//value += (voltage - 0.5) * 100;
		count++;
		delay(100);
	}
	
	digitalWrite(_pin1, LOW);	
	return value;
}