#include <LiquidCrystal.h>
#include <Temperature.h>
#include <Display.h>

// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(12, 11, 5, 4, 3, 2);
Temperature Temp(A0);
Display Disp();

void setup() 
{
  Serial.begin(9600);
  
  // set up the LCD's number of columns and rows: 
  lcd.clear();
  lcd.begin(16, 2);
  // Print a message to the LCD.
 // lcd.print("hello, world!");
 Serial.print("Hello");
  
  Temp.initializeSensorArray();
//  Disp.setupDisplay();
  
}

void loop() {
  // set the cursor to column 0, line 1
  // (note: line 1 is the second row, since counting begins with 0):
  lcd.setCursor(0, 0);
  // print the number of seconds since reset:
  lcd.print(millis()/1000);
  
  Temp.readTemperature(); 
  //Temp.updateSensorArray();
  Temp.serialPrintTemperature();
  Temp.serialPrintAverageTemperature();
  Temp.PrintSensorArray();
  
  lcd.setCursor(0, 1);

  
  lcd.print("Temp : ");
  lcd.print(Temp.getTemperature());
  lcd.print((char)223);
  //lcd.print((char)223);
  
}





