#include <Ethernet.h>
#include <SPI.h>
#include "DHT.h"
#include "floatToString.h"
#include "Temperature.h"
#include "Humidity.h"
#include "Light.h"
#include "SoilMoisture.h"

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
SoilMoisture soilMoisture = SoilMoisture();

String Project="garduino";
String Servlet="SensorData";
String NodeID ="1";
int TimeDelayBeingUpdates(5000);
int TimeDelayBeingCycles (30000);

void setup()
{
Serial.begin(9600);
Ethernet.begin(mac,ip);
delay(1000);
Serial.println("");
Serial.println("Set Up...");
dht.begin();
Serial.println("Set Up Complete");
delay(1000);     
pinMode(9, OUTPUT);  
}

char* SensorType[]={"temp", "humidity", "light", "water-level", "soil-moisture"};

String lightSensors[]={"0", "1", "2", "3"};
char* soilMoistureSensors[]={"4","5", "6", "7"};

char* waterLevelSensors[]={"1", "2", "3", "4"};
char* DHT11Sensors[]={"1", "2"};

void loop(){


activateWaterPump();

ConnectToServlet("temp","1");
ConnectToServlet("humidity","1");

ConnectToServlet("light","0");
ConnectToServlet("light","1");


/*
ConnectToServlet("light","2");
ConnectToServlet("light","3");
*/
ConnectToServlet("soil-moisture","4");

/*
ConnectToServlet("soil-moisture","5");
ConnectToServlet("soil-moisture","6");
ConnectToServlet("soil-moisture","7");

*/

delay(TimeDelayBeingCycles);
}

void debugProbe()
{
     // read the analog in value:
    int sensorValue = analogRead(1);            
    // map it to the range of the analog out:
    int outputValue = map(sensorValue, 0, 1023, 0, 255);  

    // print the results to the serial monitor:
    Serial.print("sensor = " );                      
    Serial.print(sensorValue);      
    Serial.print("\t output = ");      
    Serial.println(outputValue);  
}

void ConnectToServlet(String SensorType,String SensorID)
{
 
    if (client.connect(server,port)) 
  {
        Serial.println("");
        Serial.println("Connecting to "+ SensorType+" Sensor ID "+SensorID);

        String thisData = "";
        
        if(SensorType=="temp")
        {
            thisData = temp.generateTemp();
        }
        else if( SensorType== "humidity")
        {
            thisData = humid.generateHumidity();
        }
        else if(SensorType=="light")
        {
          int LightSensorID =0;
          
          if(SensorID=="0")
          {
            LightSensorID =0;
          } else  if(SensorID=="1")
          {
            LightSensorID =1;
          } else  if(SensorID=="2")
          {
            LightSensorID =2;
          } else  if(SensorID=="3")
          {
            LightSensorID =3;
          } 

          thisData = light.generateLight(LightSensorID);
        }
        else if (SensorType=="soil-moisture")
        {
          int SoilSensorID =4;
          
          if(SensorID=="4")
          {
            SoilSensorID =4;
          } else  if(SensorID=="5")
          {
            SoilSensorID =5;
          } else  if(SensorID=="6")
          {
            SoilSensorID =6;
          } else  if(SensorID=="7")
          {
            SoilSensorID =7;
          } 
          
          thisData = soilMoisture.generateSoilMoisture(SoilSensorID);
        }
        
        sendData(thisData,NodeID,SensorType,SensorID);
       // serialDebugData(thisData,NodeID,SensorType,SensorID);
         
        Serial.println("");
  }
  
  if (client.connected()) 
  {
        Serial.println("Disconnecting from "+ SensorType+" Sensor ID "+SensorID);
    client.stop();
    delay(TimeDelayBeingUpdates);
  }
  else
  {
    Serial.println("Error Connecting");
  }
}
  

void activateWaterPump()
{
  Serial.println("Pumping Water");
  digitalWrite(9, HIGH);   // set the LED on
  delay(10000);                  // wait for a second
  digitalWrite(9, LOW);    // set the LED off
  delay(1000);                  // wait for a second
  Serial.println("Stopped Pumping Water");
}

void sendData(String thisData, String NodeID,String SensorType, String SensorID) {

  //Serial.println("connected");
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
}


