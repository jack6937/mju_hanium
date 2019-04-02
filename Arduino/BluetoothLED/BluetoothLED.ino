/*
TXD = 아두이노 2번핀에 연결
RXD = 아두이노 3번핀에 연결
*/

#include<SoftwareSerial.h>
SoftwareSerial BTSerial(2,3); //BTSerial(2,3)이 무슨 의미일까?

int Red= 9;

void setup(){
  pinMode(Red, OUTPUT);
  Serial.begin(9600);
  BTSerial.begin(9600);
}

void loop(){
  if(BTSerial.available()){
    byte data = BTSerial.read();
    Serial.write(data);
    if(data == '0'){
      digitalWrite(Red, LOW);
      BTSerial.write("off");
      Serial.write("off");
    }
    else if(data == '1'){
      digitalWrite(Red, HIGH);
      BTSerial.write("on");
      Serial.write("on");
    }
    else{
      BTSerial.write("");
    }
  }
  if(Serial.available())
    BTSerial.write(Serial.read());
}
