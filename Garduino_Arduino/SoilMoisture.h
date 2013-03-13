#ifndef SoilMoisture_
#define SoilMoisture_

#include <arduino.h>
#include <Stdio.h>

class SoilMoisture {
  private:
   float soilMoisture;
   String soilMoistureStr; 
   char soilMoistureBuffer[30];
   String data;
  public:
    SoilMoisture();
    String generateSoilMoisture();
    float readSoilMoistureSensor();
};

#endif
