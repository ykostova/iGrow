#include <arduino.h>
#include "SoilMoisture.h"

SoilMoisture::SoilMoisture() {
   r0 = 0;      //value of select pin at the 4051 (s0)
   r1 = 0;      //value of select pin at the 4051 (s1)
   r2 = 0;      //value of select pin at the 4051 (s2)
   count = 0;   //which y pin we are selecting
   moistureProbePin = 0; // the cell and 10K pulldown are connected to a0
}

float SoilMoisture::readSoilMoistureSensor(int SoilMoistureID)
{
   float SoilMoistureSensorValue=0.00;
   moistureProbeReading =0;
 
    r0 = bitRead(SoilMoistureID,0);    // use this with arduino 0013 (and newer versions)    
    r1 = bitRead(SoilMoistureID,1);    // use this with arduino 0013 (and newer versions)    
    r2 = bitRead(SoilMoistureID,2);    // use this with arduino 0013 (and newer versions)    
  Serial.println("moisture");
  Serial.println(SoilMoistureID);
    digitalWrite(2, r0);
    digitalWrite(3, r1);
    digitalWrite(4, r2);

    //Either read or write the multiplexed pin here

    moistureProbeReading = analogRead(moistureProbePin);
    //Serial.print("Analog reading = ");
   // Serial.println(moistureProbeReading); // the raw analog reading
    SoilMoistureSensorValue = moistureProbeReading;
 
 return SoilMoistureSensorValue; 
}

String SoilMoisture::generateSoilMoisture (int SoilMoistureID) 
{
  //Control for resetting temperature
  SoilMoistureLevel = 0;
  SoilMoistureLevel = readSoilMoistureSensor(SoilMoistureID);

  // check if returns are valid, if they are NaN (not a number) then something went wrong!
  if (isnan(SoilMoistureLevel))
  {
    Serial.println("Failed to read from Sensor");
    SoilMoistureStr = "OutOfBounds";
  } else 
  {
    SoilMoistureStr = floatToString(SoilMoistureBuffer,SoilMoistureLevel,0,0,true); 
    Serial.print("SoilMoisture : "+SoilMoistureStr+" %"); 
  }
  
  data="";
  data+="value="+SoilMoistureStr+"&submit=Submit"; // Use HTML encoding for comma's
  
  return data;
}


