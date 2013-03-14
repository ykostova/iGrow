#include <Ethernet.h>
#include <SPI.h>
#include "DHT.h"
#include "floatToString.h"
#include "Temperature.h"
#include "Humidity.h"
#include "Light.h"

#define DHTPIN 8     // what pin we're connected to
#define DHTTYPE DHT11   // DHT 11 

DHT dht(DHTPIN, DHTTYPE);

byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED}; // max address for ethernet shield
byte ip[] = {192,168,2,17}; // ip address for ethernet shield
String serverAddress = "192.168.2.2"; // ip address to ping
byte server[] = {192,168,2,2};
int port = 8080;
String ServletKey = "vhMPGBUHX7MLVMSG";

EthernetClient client;
Temperature temp = Temperature();
Humidity humid = Humidity();
Light light = Light();

String Project="garduino";
String Servlet="SensorData";
String NodeID ="1";
int TimeDelayBeingUpdates(3500);

void setup()
{
Serial.begin(9600);
Ethernet.begin(mac,ip);
delay(1000);
Serial.println("Set Up...");
dht.begin();
Serial.println("Set Up Complete");
delay(1000);     
}

char* SensorType[]={"temp", "humidity", "light", "water_level", "soil_moisture"};

char* lightSensors[]={"1", "2", "3", "4"};
char* soilMoistureSensors[]={"1", "2", "3", "4","5", "6", "7", "8","9", "10"};
char* waterLevelSensors[]={"1", "2", "3", "4"};
char* DHT11Sensors[]={"1", "2"};

void loop(){

ConnectToServlet("temp");
ConnectToServlet("humidity");
ConnectToServlet("light");

}


void ConnectToServlet(String SensorType)
{
    if (client.connect(server,port)) 
  {
        Serial.println("connecting...");

        String thisData = "";
        
        if(SensorType=="temp")
        {
            thisData = temp.generateTemp();
            sendData(thisData,NodeID,"temp","1");
            serialDebugData(thisData,NodeID,"temp","1");
        }
        else if( SensorType== "humidity")
        {
            thisData = humid.generateHumidity();
            sendData(thisData,NodeID,"humidity","1");
             serialDebugData(thisData,NodeID,"humidity","1");
        }
        else if(SensorType=="light")
        {
          thisData = light.generateLight(0);
          sendData(thisData,NodeID,"light","1");
          serialDebugData(thisData,NodeID,"light","1");
        }
        else if (SensorType=="soilMoisture")
        {
          
        }
         
        Serial.println("");
        delay(3000);

  }
  delay(1000);
  
  if (client.connected()) 
  {
    Serial.println();
    Serial.println("disconnecting.");
    client.stop();
    delay(TimeDelayBeingUpdates);
  }
  else
  {
    Serial.println("Error Connecting");
  }
}
  

void updateTemperatureSensors()
{
  
}

void sendData(String thisData, String NodeID,String SensorType, String SensorID) {

  Serial.println("connected");
  // node / type / number / value
  client.println("POST /"+Project+"/"+Servlet+"/"+NodeID+"/"+SensorType+"/"+SensorID+"/"+ServletKey+" HTTP/1.1");
  client.println("Host: "+serverAddress);
  client.println("Content-Type: application/x-www-form-urlencoded");
  client.print("Content-Length: ");
  client.println(thisData.length());
  client.println();
  client.print(thisData);
  client.println();
}

void serialDebugData(String thisData, String NodeID,String SensorType, String SensorID) 
{
    //Prints your post request out for debugging
  Serial.println("POST /"+Project+"/"+Servlet+"/"+NodeID+"/"+SensorType+"/"+SensorID+"/"+ServletKey+" HTTP/1.1");
  Serial.println("Host: "+serverAddress);
  Serial.println("Content-Type: application/x-www-form-urlencoded");
  Serial.print("Content-Length: ");
  Serial.println(thisData.length());
  Serial.println();
  Serial.print(thisData);
  Serial.println();
}


