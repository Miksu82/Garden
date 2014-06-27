#include <TemperatureSensor.h>
#include <MoistureSensor.h>

MoistureSensor moistureSensor1(4, 5, A5, 200, 0.10);
MoistureSensor moistureSensor2(6, 7, A4, 200, 0.10);
MoistureSensor moistureSensor3(8, 9, A3, 200, 0.10);

TemperatureSensor temperatureSensor1(10, A1);
TemperatureSensor temperatureSensor2(12, A0);

void setup() {
  Serial.begin(9600);
  for (int i = 0; i < 13; i++) {
    	digitalWrite(i, LOW);
        //pinMode(i, OUTPUT);
  }
  
  digitalWrite(3,HIGH);
}

void loop() {
   Serial.println("debug Starting to read temperature");
   float temperature1 = temperatureSensor1.readTemperature(1000);
   Serial.print("Temperature1: ");
   Serial.println(temperature1);
   delay(1000);
   
  float temperature2 = temperatureSensor2.readTemperature(1000);
   Serial.print("Temperature2: ");
   Serial.println(temperature2);
   delay(1000);
   
   
  Serial.println("debug Starting to read moisture");
   int moisture1 = moistureSensor1.readMoisture(3000);
  // Serial.print("Moisture1: ");
   //Serial.println(moisture1);
   delay(1000);
   
   int moisture2 = moistureSensor2.readMoisture(3000);
  // Serial.print("Moisture2: ");
  // Serial.println(moisture2);
   delay(1000);

   int moisture3 = moistureSensor3.readMoisture(3000);
  // Serial.print("Moisture3: ");
  // Serial.println(moisture3);
   
   sendMeasurements(temperature1, temperature2, moisture1, moisture2, moisture3);
   delay(30*1000);
}

void sendMeasurements(float t1, float t2, int m1, int m2, int m3) {
  Serial.print("wget http://192.168.2.231:8080/garden/?hid=1");
  print("t1");
  Serial.print(t1);
  print("t2");
  Serial.print(t2);
  print("m1");
  Serial.print(m1);
  print("m2");
  Serial.print(m2);
  print("m3");
  Serial.print(m3);
  Serial.println(" -O /dev/null");
}
  
void print(String param) {
  Serial.print('\\');
  Serial.print("&");
  Serial.print(param);
  Serial.print("=");
}
