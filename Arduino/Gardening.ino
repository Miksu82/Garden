#include <TemperatureSensor.h>
#include <MoistureSensor.h>

MoistureSensor moistureSensor1(4, 5, A5, 200, 0.10);
MoistureSensor moistureSensor2(6, 7, A4, 200, 0.10);
MoistureSensor moistureSensor3(8, 9, A3, 200, 0.10);

TemperatureSensor temperatureSensor1(10, A1);
TemperatureSensor temperatureSensor2(11, A0);

const unsigned long FLAG_CHECK_INTERVAL = 10000;
const unsigned long HOUR = 86400000;
const int LED_PIN = 12;

boolean isFirst = true;
volatile boolean buttonPressed = false;

void setup() {
  Serial.begin(9600);
  for (int i = 0; i <= 13; i++) {
    	digitalWrite(i, LOW);
  }
  
  pinMode(3,OUTPUT);
  digitalWrite(3,LOW);
  pinMode(LED_PIN,OUTPUT);
  digitalWrite(LED_PIN, LOW);
  
  // Wait for the router to boot
  Serial.println("debug starting to boot the router");
  delay(60000);
  Serial.println("debug router booted");
 
  attachInterrupt(0, buttonTriggered, FALLING);
}

void loop() {
  
  digitalWrite(LED_PIN, HIGH);
  if (!isFirst) {
     Serial.println("/root/network-up");
     delay(30000);
  }
   isFirst = false;

   Serial.println("debug Starting to read temperature");
   float temperature1 = temperatureSensor1.readTemperature(2000);
   delay(500);
   
   float temperature2 = temperatureSensor2.readTemperature(2000);
   delay(500);
   
   Serial.println("debug Starting to read moisture");
   int moisture1 = moistureSensor1.readMoisture(2000);
   delay(500);
   
   int moisture2 = moistureSensor2.readMoisture(2000);
   delay(500);

   int moisture3 = moistureSensor3.readMoisture(2000);
   
   sendMeasurements(temperature1, temperature2, moisture1, moisture2, moisture3);   
  
   Serial.println("/root/network-down");
   digitalWrite(LED_PIN, LOW);

   Serial.println("debug starting to wait");
   waitLong(HOUR);
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

void waitLong(unsigned long ms) {
  digitalWrite(3, HIGH);
  // We wait so the interreupt has time to run
  delay(100);
  buttonPressed = false;
  unsigned long parts = ms / FLAG_CHECK_INTERVAL;  
  for (unsigned int i = 0; i < parts && !buttonPressed; i++) {
     delay(FLAG_CHECK_INTERVAL);
  }
  digitalWrite(3, LOW);
  delay(100);
  buttonPressed = false;  
}

void buttonTriggered() {
  buttonPressed = true; 
}
