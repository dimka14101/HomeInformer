#include <Wire.h> 
#include <LiquidCrystal_I2C.h>  //display 
#include <DHT.h>//humadity sensor
#include <Adafruit_Sensor.h> //pressure sensor
#include <Adafruit_BMP085_U.h>//pressure sensor
#include <BH1750.h>//light
#include <SPI.h>
#include <Ethernet.h>//data transfer on host
#include <avr/sleep.h>
#include <Bridge.h>
#include <HttpClient.h>
#include <ArduinoJson.h>

#define DHTPIN 5 //humadity sensor get data on pin 5
#define DHTTYPE DHT11  //humadity sensor
DHT dht(DHTPIN, DHTTYPE); ////humadity initialize

#define NOTE_C4  262//music values
#define NOTE_A3  220
#define NOTE_B3  247
#define NOTE_G3  196
#define NOTE_C4  262
int speakerPin = 6;  //speakerPin
int melody[] = { NOTE_C4, NOTE_G3, NOTE_G3, NOTE_A3, NOTE_G3, 0, NOTE_B3, NOTE_C4};
int noteDurations[] = {4, 8, 8, 4, 4, 4, 4, 4};// note durations: 4 = quarter note, 8 = eighth note, etc.:

LiquidCrystal_I2C lcd(0x27,16,2);//display initialize

Adafruit_BMP085_Unified bmp = Adafruit_BMP085_Unified(10085);//pressre sensor object

BH1750 lightMeter;//light sensor

byte degree[8] = // celsium symbol
{
  B00111,
  B00101,
  B00111,
  B00000,
  B00000,
  B00000,
  B00000,
};                                

byte mac[] = {0x14, 0xDA, 0xE9, 0xA0, 0x01, 0x8D }; // macaddress
EthernetClient client;      //client object                     
char server[] = "homeinformer2.azurewebsites.net";//domain/host/server
//char userName[]="dima417914@mail.ru";
//char token[]="bearer bfBAkeWczKxOOmsj-GfAS7ICa4uVyGLqGyJpiiEDB7zZSgMG-ikrBbfVOfQSi1-ILVWQl2n-wH6Yso4hfAcabLBlxdM_sgj1swI7x1Vtfr7DhrWVDYIrM-LZQTMQZLM4FdE7_nx87mA98NWqbhEWE5jFMkreZx0wK_xsFAG9Xgnbq16IaEaGDQ-ZknS3-wKI6zBzUaC5om_7j2bu_7swDOo7lJ1VG8qiAa8lcdR-7k2Q3AMGxAvNiqqOraMQzM2Cof5pRbl2BoHfAgRDEQND4X2kWTYwJkoQU2Iu2nJUxG-hjndKJPtZkHSk4vuqgGMWs_9RYN3Dg7WIykzu7SXK3Qcp9O6T_betKvc9fqqdA_vBtIbySnu2UjII0XtS-mPxyHDhIP__TL13JrFW5UOyZQFH-mxB6pb59gsk8_fGPuiQ8c1cL8Z2o6fGoURNcRXjc2yjhcWM8LVonAcQOgLuYb-9j-oXVCbaMxbjhO0LEgVnBbxUnkZoCf_lzYnYw8TP";
const unsigned long HTTP_TIMEOUT = 10000;
int criticalTemp=50;

void setup() {
  // put your setup code here, to run once:

  lcd.init();//lcd init
  lcd.backlight();
  lcd.createChar(1, degree); // create symbol celsium with variable 1
   
  Serial.begin(9600);

  Ethernet.begin(mac);//ethernet initialize
  
  pinMode(speakerPin, OUTPUT);//speaker pin on out
  
  
 

  lightMeter.begin();//light sensor initialize
  
  if(!bmp.begin())
  {
    //There was a problem detecting the BMP085 ... check your connections 
    Serial.print(F("Ooops, no BMP085 detected ... Check your wiring or I2C ADDR!"));
    while(1);
  }
  sensor_t sensor;//initialize pressure sensor
  bmp.getSensor(&sensor);
  delay(500);

  dht.begin();

//sleep_enable();

set_sleep_mode (SLEEP_MODE_PWR_DOWN);   
//  sleep_cpu (); 
}


void loop() {
   //BEGIN SCREEN BLOCK 1
   client.connect(server, 80);
     if (client.connected()) {
     if (sendRequest() && skipResponseHeaders()) {
       lcd.setCursor(0, 0);
         lcd.print(F("Alarm Temperat."));
         lcd.setCursor(0, 1);  
         lcd.print(F("UPDATED  t=   \1C"));
         lcd.setCursor(11, 1);
         lcd.print(receiveDataFromServer());
         client.stop();
     }
     else{
      lcd.setCursor(0, 0);
         lcd.print(F("Alarm Temperat."));
         lcd.setCursor(0, 1);  
         lcd.print(F("DEFAULT  t=   \1C"));
         lcd.setCursor(11, 1);
         lcd.print(criticalTemp);
      }
    }
    else{
        lcd.setCursor(0, 0);
         lcd.print(F("No connection..."));
           lcd.setCursor(0, 1);  
         lcd.print(F(" Not received.. "));
      }
       delay(3000);
      lcd.clear();
      //END SCREEN BLOCK 1
    
  //BEGIN SCREEN BLOCK 2
        float humidity = dht.readHumidity();//read humidiry
        float temperature1 = dht.readTemperature();//read temperat
      lcd.setCursor(0, 0);
      lcd.print(F("HUMID:         %"));
      lcd.setCursor(9, 0);
      lcd.print(humidity);
      lcd.setCursor(0, 1);
      lcd.print(F("TEMP1:        \1C"));
      lcd.setCursor(9, 1);
      lcd.print(temperature1);
        delay(3000);
      lcd.clear();
  //END SCREEN BLOCK 2

  //BEGIN SCREEN BLOCK 3   
        sensors_event_t event;
        bmp.getEvent(&event);
        float temperature2;
        bmp.getTemperature(&temperature2);
      lcd.setCursor(0, 0);
      lcd.print(F("PRSSR:       hPa"));
      lcd.setCursor(7, 0);
      lcd.print(event.pressure);
      lcd.setCursor(0, 1);
      lcd.print(F("TEMP2:        \1C"));
      lcd.setCursor(8, 1);
      lcd.print(temperature2);
        delay(3000);
      lcd.clear();
    //END SCREEN BLOCK 3
              //CHECK TEMPERATURE AND NOTIFICATE
              if(temperature1>=criticalTemp || temperature2 >=criticalTemp)
                 {
                  for (int thisNote = 0; thisNote < 8; thisNote++)
                  {
                    int noteDuration = 1000 / noteDurations[thisNote];
                    tone(6, melody[thisNote], noteDuration);
                    int pauseBetweenNotes = noteDuration * 1.30;
                    delay(pauseBetweenNotes);        
                  }
                 }
              else
              {
                noTone(6);
              }
      //BEGIN SCREEN BLOCK 4
           float seaLevelPressure = SENSORS_PRESSURE_SEALEVELHPA;
           float altitude=bmp.pressureToAltitude(seaLevelPressure,event.pressure);
           uint16_t luminosity = lightMeter.readLightLevel();
      lcd.setCursor(0, 0);
      lcd.print(F("ALTTD:         m"));
      lcd.setCursor(8, 0);
      lcd.print(altitude);  
      lcd.setCursor(0, 1);  
      lcd.print(F("LIGHT:        lx"));
      lcd.setCursor(9, 1);
      lcd.print(luminosity);
        delay(3000);
      lcd.clear();
  //END SCREEN BLOCK 4

  
    //BEGIN SCREEN BLOCK 5
    client.connect(server, 80);
    if(client.connected()) {
         lcd.setCursor(0, 0);
         lcd.print(F("  Connected...  "));
         sendDataToServer(temperature1,temperature2,humidity,luminosity,event.pressure,altitude);
         lcd.setCursor(0, 1);  
         lcd.print(F("     SENT!!!    "));
         client.stop();
    }else {
          lcd.setCursor(0, 0);
         lcd.print(F("No connection..."));
         lcd.setCursor(0, 1);  
         lcd.print(F("   NOT SENT!!!  "));
    }
     delay(3000);
      lcd.clear();
     //END SCREEN BLOCK 53
}



int  receiveDataFromServer(){
  
  DynamicJsonBuffer jsonBuffer(200);
  JsonObject& root = jsonBuffer.parseObject(client);
  if (!root.success()) {
    Serial.println(F("JSON parsing failed!"));
    return false;
  }
  criticalTemp= root["value"];
  return criticalTemp;
}

bool sendRequest() {
    client.print(F("GET "));
  client.print(F("/api/getsetting?description=criticalTemperature"));
  //client.println(userName);
  client.println(F(" HTTP/1.0"));
  client.print(F("Host: "));
  client.println(server);
    client.println(F("Authorization: bearer Ij54_6H9ZCVHVg3_tCRNEabCOsOiE1Vrwunxf3jtAbC6uTlNtvtJiQ3whXRmOMC3RNHR6J-KyrRgDdkyF_5glxCwWKe9xAtBIhd_0f_Q5tdQj7bTjlG-yMrRpQnlqkmTZUp6Sb0nQv3dYP7DTWK_h-lZgFyM3363aYHo_88lhbBNbUPWAFODWNNkk98bwQRICvTlFKOJkzUVyqNgS_Wx4siT8tEdRWsxp-tDvL9mtUImcYTilX9hmAGu6PyriVO0bofCth__S5EzkWmR0f0jRJiDS-9AVJlFWn-KJRqHb2rHH96AVzKtmJLOhFcwJWWYtEk2F3rmWd-3U7p54dRNakR4NZVgyVYgsvcnDwoVx8Ny-kaaTxmDqlIhU98XY5UExu3K4MF7ENvex-VoomB5O4eN7EiqBAhJUX1GqZG6tLBwH2np8eMnL8h2aV_E8OZF8fu9SnFiM1b9f29NemtPSYpHMHTB81-dRpNp10wwSjVYy-_0hiIC0J68_W3b1358"));
  //client.println(token);
  client.println(F("Connection: close"));
  client.println();
  return true;
}

bool skipResponseHeaders() {
  char endOfHeaders[] = "\r\n\r\n";
  client.setTimeout(HTTP_TIMEOUT);
  bool ok = client.find(endOfHeaders);
  return ok;
}

void sendDataToServer(float temperature1,float temperature2, float humidity,uint16_t luminosity,float pressure,float altitude) {
  client.print(F("GET /api/insert?"));
  client.print(F("T1="));  
  client.print(temperature1);
  client.print(F("&T2="));
  client.print(temperature2);
  client.print(F("&HMDT="));
  client.print(humidity);
  client.print(F("&LMNST="));
  client.print(luminosity);
  client.print(F("&PRSR="));
  client.print(pressure);
  client.print(F("&ALTTD="));
  client.print(altitude);
  client.println(F(" HTTP/1.1"));
  client.print( F("Host: ") );
  client.println(server);
   client.println(F("Authorization: bearer Ij54_6H9ZCVHVg3_tCRNEabCOsOiE1Vrwunxf3jtAbC6uTlNtvtJiQ3whXRmOMC3RNHR6J-KyrRgDdkyF_5glxCwWKe9xAtBIhd_0f_Q5tdQj7bTjlG-yMrRpQnlqkmTZUp6Sb0nQv3dYP7DTWK_h-lZgFyM3363aYHo_88lhbBNbUPWAFODWNNkk98bwQRICvTlFKOJkzUVyqNgS_Wx4siT8tEdRWsxp-tDvL9mtUImcYTilX9hmAGu6PyriVO0bofCth__S5EzkWmR0f0jRJiDS-9AVJlFWn-KJRqHb2rHH96AVzKtmJLOhFcwJWWYtEk2F3rmWd-3U7p54dRNakR4NZVgyVYgsvcnDwoVx8Ny-kaaTxmDqlIhU98XY5UExu3K4MF7ENvex-VoomB5O4eN7EiqBAhJUX1GqZG6tLBwH2np8eMnL8h2aV_E8OZF8fu9SnFiM1b9f29NemtPSYpHMHTB81-dRpNp10wwSjVYy-_0hiIC0J68_W3b13584чуа"));
  client.println( F("Connection: close") );
  client.println();
  client.println();
  client.stop();
  client.flush();
}

