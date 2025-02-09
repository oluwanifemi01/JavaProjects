package org.example;
import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.Pin; // Firmata
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.MonochromeCanvas;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    static final int A0 = 14; // Potentiometer
    static final int A2 = 16; // Sound
    static final int D4 = 6; // Button
    static final int D6 = 4; // LED
    static final byte I2C0 = 0x3C; // OLED Display
    static final int A1 = 15; //soil mositure sensor
    static final int D7 = 7;
    public static void main(String[] args) throws IOException, InterruptedIOException, InterruptedException {
       var myUsbPort = "/dev/cu.usbserial-1140";
       IODevice myDevice = new FirmataDevice(myUsbPort);

       try{
           // initialization of board
           myDevice.start();
           myDevice.ensureInitializationIsDone();
       }catch(Exception ex){
           // error message
           System.out.println("Connection to board failed");
       }finally {

           // initialization of pins
           Pin button = myDevice.getPin(D4);
           button.setMode(Pin.Mode.INPUT);

           Pin pump = myDevice.getPin(D7);
           pump.setMode(Pin.Mode.OUTPUT);

           Pin sensor = myDevice.getPin(15);
           sensor.setMode(Pin.Mode.ANALOG);


           // initialization of OLED
           I2CDevice i2c = myDevice.getI2CDevice((byte) 0x3C);
           SSD1306 display = new SSD1306(i2c, SSD1306.Size.SSD1306_128_64);
           display.init();

           // creation of graph
           StdDraw.setXscale(0,30);
           StdDraw.setYscale(-1,6);

           StdDraw.setPenRadius(0.005);
           StdDraw.setPenColor(StdDraw.BLACK);

           StdDraw.line(0,0,0,5);
           StdDraw.line(0,0,30,0);

           StdDraw.text(15,-0.5,"Time [s]");
           StdDraw.text(-1,2.5,"[V]");
           StdDraw.text(15,6,"Moisture level vs Time");

           //task object
           Timer timer = new Timer();
           var task =  new MoistureTestCheck(sensor,pump,display,button);
           timer.schedule(task, 0, 1000);

       }
    }
}