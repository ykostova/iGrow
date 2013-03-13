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
  public:
    Light();
    String generateLight();
    float readLightSensor();
};

#endif
