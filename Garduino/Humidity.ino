#include <arduino.h>
#include "Humidity.h"

Humidity::Humidity() {

}

String Humidity::generateHumidity () 
{
  //Control for resetting temperature
  humidity = 0;
  humidity = dht.readHumidity();

  // check if returns are valid, if they are NaN (not a number) then something went wrong!
  if (isnan(humidity))
  {
    Serial.println("Failed to read from DHT");
    humidityStr="OutOfBounds";
  } else 
  {
    humidityStr = floatToString(humidityBuffer,humidity,0,0,true); 
    Serial.print("Humidity: "+humidityStr+" %"); 
  }
  
  data="";
  data+="value="+humidityStr+"&submit=Submit"; // Use HTML encoding for comma's
  
  return data;
}


