#ifndef Temperature_
#define Temperature_

#include <arduino.h>
#include <Stdio.h>
#include "DHT.h"

class Temperature {
  private:
   float temp;
   String tempStr; 
   char tempBuffer[30];
   String data;
  public:
    Temperature();
    String generateTemp();
};

#endif

