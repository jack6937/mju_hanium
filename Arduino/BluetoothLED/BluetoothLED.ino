/*
TXD = 아두이노 2번핀에 연결
RXD = 아두이노 3번핀에 연결
*/

#include<SoftwareSerial.h>
SoftwareSerial BTSerial(2,3); //BTSerial(2,3)이 무슨 의미일까?

int Red = 9;
int Green = 7;

void setup(){
  pinMode(Red, OUTPUT);
  pinMode(Green, OUTPUT);
  Serial.begin(9600);
  BTSerial.begin(9600);
}

void loop(){
  if(BTSerial.available()){
    byte data = BTSerial.read();
    Serial.write(data);
    if(data == '0'){
      digitalWrite(Green, LOW);
      digitalWrite(Red, HIGH);
      BTSerial.write("off");
      Serial.write("off");
    }
    else if(data == '1'){
      digitalWrite(Green, HIGH);
      digitalWrite(Red, LOW);
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
