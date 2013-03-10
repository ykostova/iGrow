/*
  LiquidCrystal Library - Hello World
 
 Demonstrates the use a 16x2 LCD display.  The LiquidCrystal
 library works with all LCD displays that are compatible with the 
 Hitachi HD44780 driver. There are many of them out there, and you
 can usually tell them by the 16-pin interface.
 
 This sketch prints "Hello World!" to the LCD
 and shows the time.
 
  The circuit:
 * LCD RS pin to digital pin 12
 * LCD Enable pin to digital pin 11
 * LCD D4 pin to digital pin 5
 * LCD D5 pin to digital pin 4
 * LCD D6 pin to digital pin 3
 * LCD D7 pin to digital pin 2
 * LCD R/W pin to ground
 * 10K resistor:
 * ends to +5V and ground
 * wiper to LCD VO pin (pin 3)
 
 
// include the library code:
*/

#include <LiquidCrystal.h>

// initialize the library with the numbers of the interface pins

// * LCD RS pin to digital pin 12
int RS_Pin = 12;
 //* LCD Enable pin to digital pin 11
int Enable_Pin = 11;
 //* LCD D4 pin to digital pin 5
int D4_Pin = 5;
 //* LCD D5 pin to digital pin 4
int D5_Pin = 4;
// * LCD D6 pin to digital pin 3
int D6_Pin = 3;
// * LCD D7 pin to digital pin 2
int D7_Pin = 2;


class LCD
{
    
}


