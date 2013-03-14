#include <arduino.h>
#include "WaterLevel.h"

WaterLevel::WaterLevel() {

}

float WaterLevel::readWaterLevelSensor()
{
 float waterLvlValue=0.00;
 
 return waterLvlValue; 
}

String WaterLevel::generateWaterLevel () 
{
  //Control for resetting WaterLevel
  waterLvl = 0;
  waterLvl = readWaterLevelSensor();

  // check if returns are valid, if they are NaN (not a number) then something went wrong!
  if (isnan(waterLvl))
  {
    Serial.println("Failed to read from DHT");
    waterLvlStr = "OutOfBounds";
  } else 
  {
    waterLvlStr = floatToString(waterLvlBuffer,waterLvl,0,0,true); 
    Serial.print("WaterLevel: "+waterLvlStr+" *C"); 
  }
  
  data="";
  data+="value="+waterLvlStr+"&submit=Submit"; // Use HTML encoding for comma's
  
  return data;
}


