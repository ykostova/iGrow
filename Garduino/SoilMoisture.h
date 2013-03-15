#ifndef SoilMoisture_
#define SoilMoisture_

#include <arduino.h>
#include <Stdio.h>

class SoilMoisture {
  private:
   float SoilMoistureLevel;
   String SoilMoistureStr; 
   char SoilMoistureBuffer[30];
   String data;
   int r0;      //value of select pin at the 4051 (s0)
   int r1;      //value of select pin at the 4051 (s1)
   int r2;      //value of select pin at the 4051 (s2)
   int count;   //which y pin we are selecting
   int moistureProbePin; // the cell and 10K pulldown are connected to a0
   int moistureProbeReading; // the analog reading from the sensor divider
  public:
    SoilMoisture();
    String generateSoilMoisture(int);
    float readSoilMoistureSensor(int);
};

#endif
