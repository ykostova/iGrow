#include <arduino.h>
#include "SoilMoisture.h"

SoilMoisture::SoilMoisture() {

}

float SoilMoisture::readSoilMoistureSensor()
{
 float soilMoistureValue=0.00;
 
 return soilMoistureValue; 
}

String SoilMoisture::generateSoilMoisture () 
{
  //Control for resetting SoilMoisture
  soilMoisture = 0;
  soilMoisture = readSoilMoistureSensor();

  // check if returns are valid, if they are NaN (not a number) then something went wrong!
  if (isnan(soilMoisture))
  {
    Serial.println("Failed to read from DHT");
    soilMoistureStr = "OutOfBounds";
  } else 
  {
    soilMoistureStr = floatToString(soilMoistureBuffer,soilMoisture,0,0,true); 
    Serial.print("SoilMoisture: "+soilMoistureStr+" *C"); 
  }
  
  data="";
  data+="value="+soilMoistureStr+"&submit=Submit"; // Use HTML encoding for comma's
  
  return data;
}


