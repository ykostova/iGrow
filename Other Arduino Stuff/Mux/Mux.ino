    /*
     * codeexample for useing a 4051 * analog multiplexer / demultiplexer
     * by david c. and tomek n.* for k3 / malm� h�gskola
     *
     * edited by Ross R.
     */  
     
    int sensorValues [5]; 
     
    int r0 = 0;      //value of select pin at the 4051 (s0)
    int r1 = 0;      //value of select pin at the 4051 (s1)
    int r2 = 0;      //value of select pin at the 4051 (s2)
    int count = 0;   //which y pin we are selecting
     
    void setup(){
     
      Serial.begin(9600);
      pinMode(2, OUTPUT);    // s0
      pinMode(3, OUTPUT);    // s1
      pinMode(4, OUTPUT);    // s2
    }
     
    void loop () {
      for (count=0; count<7; count++) {
     
        sensorValues [count] = 0.0;
        
        // select the bit  
        r0 = bitRead(count,0);    // use this with arduino 0013 (and newer versions)    
        r1 = bitRead(count,1);    // use this with arduino 0013 (and newer versions)    
        r2 = bitRead(count,2);    // use this with arduino 0013 (and newer versions)    
        
        digitalWrite(2, r0);
        digitalWrite(3, r1);
        digitalWrite(4, r2);
        
        sensorValues[count] = analogRead(A0);   
        //value = (value - .5) *100;
        Serial.print("Val = ");
        Serial.print(sensorValues[count]);
        Serial.print(" #");
        Serial.println(count);
        
        
        delay(1000);
     
        //Either read or write the multiplexed pin here

      }  
    }

