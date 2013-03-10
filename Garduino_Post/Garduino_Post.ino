#include <Ethernet.h>
#include <SPI.h>
#include "DHT.h"
#include "floatToString.h"

#define DHTPIN 2     // what pin we're connected to

#define DHTTYPE DHT11   // DHT 11 

byte mac[] = { 0x00, 0xAA, 0xBB, 0xCC, 0xDE, 0x02};  //Replace with your Ethernet shield MAC
EthernetClient client;

// Connect pin 1 (on the left) of the sensor to +5V
// Connect pin 2 of the sensor to whatever your DHTPIN is
// Connect pin 4 (on the right) of the sensor to GROUND
// Connect a 10K resistor from pin 2 (data) to pin 1 (power) of the sensor
 
DHT dht(DHTPIN, DHTTYPE);
 
void setup()
{
Serial.begin(9600);
Ethernet.begin(mac);
delay(1000);
Serial.println("connecting...");

dht.begin();
}
 
void loop(){
String data;
data+="";

 
if (client.connect("192.168.2.2",8080)) {
  
  
  // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  pinMode(0, OUTPUT); 
  String temp; 
  String humidity;
  
  // check if returns are valid, if they are NaN (not a number) then something went wrong!
  if (isnan(t) || isnan(h)) {
    Serial.println("Failed to read from DHT");
  } else {
    Serial.print("Humidity: "); 
    Serial.print(h);
    Serial.print(" %\t");
    Serial.print("Temperature: "); 
    Serial.print(t);
    Serial.println(" *C");
    
    
  }
  
data+="&submit=Submit"; // Submitting data
  
  
Serial.println("connected");
client.println("POST /Garduino/Node HTTP/1.1");
client.println("Host: 192.168.2.2");
client.println("Content-Type: application/x-www-form-urlencoded");

client.print("Content-Length: ");
client.println(data.length());
client.println();
client.print(data);
client.println();
 
//Prints your post request out for debugging
Serial.println("POST /Node HTTP/1.1");
Serial.println("Host: www.your-website.com");
Serial.println("Content-Type: application/x-www-form-urlencoded");
Serial.println("Connection: close");
Serial.print("Content-Length: ");
Serial.println(data.length());
Serial.println();
Serial.print(data);
Serial.println();
}
delay(2000);
 
if (client.connected()) {
Serial.println();
Serial.println("disconnecting.");
client.stop();
}
 
}
