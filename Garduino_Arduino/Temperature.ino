#include <arduino.h>
#include "Temperature.h"

Temperature::Temperature() {

}

String Temperature::generateTemp () 
{
  //Control for resetting temperature
  temp = 0;
  temp = dht.readTemperature();

  // check if returns are valid, if they are NaN (not a number) then something went wrong!
  if (isnan(temp))
  {
    Serial.println("Failed to read from DHT");
    tempStr = "OutOfBounds";
  } else 
  {
    tempStr = floatToString(tempBuffer,temp,0,0,true); 
    Serial.print("Temperature: "+tempStr+" *C"); 
  }
  
  data="";
  data+="value="+tempStr+"&submit=Submit"; // Use HTML encoding for comma's
  
  return data;
}


