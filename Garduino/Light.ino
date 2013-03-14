#include <arduino.h>
#include "Light.h"

Light::Light() {
   r0 = 0;      //value of select pin at the 4051 (s0)
   r1 = 0;      //value of select pin at the 4051 (s1)
   r2 = 0;      //value of select pin at the 4051 (s2)
   count = 0;   //which y pin we are selecting
   photocellPin = 0; // the cell and 10K pulldown are connected to a0
}

float Light::readLightSensor(int lightID)
{
   float lightSensorValue=0.00;
 
    r0 = bitRead(count,0);    // use this with arduino 0013 (and newer versions)    
    r1 = bitRead(count,1);    // use this with arduino 0013 (and newer versions)    
    r2 = bitRead(count,2);    // use this with arduino 0013 (and newer versions)    

    digitalWrite(2, r0);
    digitalWrite(3, r1);
    digitalWrite(4, r2);

    //Either read or write the multiplexed pin here

    photocellReading = analogRead(photocellPin);
    Serial.print("Analog reading = ");
    Serial.println(photocellReading); // the raw analog reading
    // LED gets brighter the darker it is at the sensor
    // that means we have to -invert- the reading from 0-1023 back to 1023-0
     lightSensorValue = photocellReading;
 
 return lightSensorValue; 
}

String Light::generateLight (int lightID) 
{
  //Control for resetting temperature
  LightLevel = 0;
  LightLevel = readLightSensor(lightID);

  // check if returns are valid, if they are NaN (not a number) then something went wrong!
  if (isnan(LightLevel))
  {
    Serial.println("Failed to read from Sensor");
    LightStr = "OutOfBounds";
  } else 
  {
    LightStr = floatToString(LightBuffer,LightLevel,0,0,true); 
    Serial.print("Light : "+LightStr+" %"); 
  }
  
  data="";
  data+="value="+LightStr+"&submit=Submit"; // Use HTML encoding for comma's
  
  return data;
}


