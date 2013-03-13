#ifndef Humidity_
#define Humidity_

#include <arduino.h>
#include <Stdio.h>
#include "DHT.h"

class Humidity {
  private:
   float humidity;
   String humidityStr; 
   char humidityBuffer[30];
   String data;
  public:
    Humidity();
    String generateHumidity();
};

#endif
