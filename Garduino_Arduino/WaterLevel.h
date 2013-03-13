#ifndef WaterLevel_
#define WaterLevel_

#include <arduino.h>
#include <Stdio.h>

class WaterLevel {
  private:
   float waterLvl;
   String waterLvlStr; 
   char waterLvlBuffer[30];
   String data;
  public:
    WaterLevel();
    String generateWaterLevel();
    float readWaterLevelSensor();
};

#endif
