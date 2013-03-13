#include <arduino.h>
#include "Light.h"

Light::Light() {

}

float Light::readLightSensor()
{
 float lightSensorValue=0.00;
 
 return lightSensorValue; 
}

String Light::generateLight () 
{
  //Control for resetting temperature
  LightLevel = 0;
  LightLevel = readLightSensor();

  // check if returns are valid, if they are NaN (not a number) then something went wrong!
  if (isnan(LightLevel))
  {
    Serial.println("Failed to read from DHT");
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


