#ifndef Light_
#define Light_

#include <arduino.h>
#include <Stdio.h>

class Light {
  private:
   float LightLevel;
   String LightStr; 
   char LightBuffer[30];
   String data;
   int r0;      //value of select pin at the 4051 (s0)
   int r1;      //value of select pin at the 4051 (s1)
   int r2;      //value of select pin at the 4051 (s2)
   int count;   //which y pin we are selecting
   int photocellPin; // the cell and 10K pulldown are connected to a0
   int photocellReading; // the analog reading from the sensor divider
  public:
    Light();
    String generateLight(int);
    float readLightSensor(int);
};

#endif
