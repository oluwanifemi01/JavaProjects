package org.example;
import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.IODeviceEventListener;
import org.firmata4j.Pin;
import org.firmata4j.firmata.parser.PinStateParsingState;
import org.firmata4j.ssd1306.MonochromeCanvas;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MoistureTestCheck extends TimerTask{
    private final Pin sensor; //soil moisture sensor pin
    private final Pin pump; //Pump pin
    static final double Dry_Value = 3.40; //Value for when plant is wet
    static final double Damp_Value =  3.04; //Value for wehn plant is damp
    static final double Wet_Value = 2.45; //Value for when plant is wet
    private final SSD1306 display; //oLed display
    private final Pin button; //button

     MoistureTestCheck(Pin sensor, Pin pump, SSD1306 display,Pin button){
        this.sensor = sensor;
        this.pump = pump;
        this.display = display;
        this.button = button;
    }

    boolean stop =false;
    int time = 1;
    @Override
    public void run(){
            int stop = (int)button.getValue();

            // get seoil mositure level
            var moisture = (double)sensor.getValue()*5/1023;

        // clear the graph and create it again if it is filled up
        if(time>=30){
            StdDraw.clear();
            StdDraw.line(0,0,0,5);
            StdDraw.line(0,0,30,0);
            StdDraw.text(15,-0.5,"Time [s]");
            StdDraw.text(-1,2.5,"[V]");
            StdDraw.text(15,6,"Moisture level vs Time");
            time = 1;
        }

        // draw the points from the data from the sensor
        StdDraw.text((double) time,(double)moisture,"*"); //plot points on the graph
        time++; //increment the time/x-point
        try {
            Thread.sleep(500); //pause for 0.5s
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
            //while(stop != 0){

                // conditional for dry soil
                if(moisture >= Dry_Value){
                    System.out.println("Pump On, Moisture Level: "+moisture+"V");
                    display.getCanvas().clear();
                    display.getCanvas().write("Pump ON"); // display on OLED display
                    display.getCanvas().setTextsize(2); //change text size
                    display.getCanvas().write((int)moisture+"V"); // display on display
                    display.display(); // send data to OLED display
                    try {
                        pump.setValue(1);// turn on water pump
                        Thread.sleep(1000); // wait for 1s
                        pump.setValue(0); // turn off water pump
                    }catch(Exception e){
                        e.printStackTrace();

                    }
                }

                // conditional for damp soil
                else if((moisture>Dry_Value)&&(moisture<Damp_Value)){
                    System.out.println("Pump On, Moisture Level: "+moisture+"V");
                    display.getCanvas().clear();
                    display.getCanvas().write("Pump ON"); //display on OLED display
                    display.getCanvas().setTextsize(2); //change text size
                    display.getCanvas().write((int)moisture+"V"); //display on OLED display
                    display.display(); //send data to display
                    try {
                        pump.setValue(1); //turn on water pump
                        Thread.sleep(300); //wait for 0.3s
                        pump.setValue(0); //turn off water pump
                    }catch(Exception ex){
                        ex.printStackTrace();
                    } time++;
                }

                // conditional for damp soil
                else if((moisture<Damp_Value)&&(moisture>=Wet_Value)){
                    System.out.println("Pump On, Moisture Level: "+moisture+"V");
                    display.getCanvas().clear();
                    display.getCanvas().write("Pump On");
                    display.getCanvas().setTextsize(2);
                    display.getCanvas().write((int)moisture+"V");
                    display.display();
                    try{
                        pump.setValue(1);
                        Thread.sleep(300);
                        pump.setValue(0);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                // conditional for wet soil
                // pump stays off
                else if(moisture<=Wet_Value){
                    System.out.println("Pump Off, Moisture Level: "+moisture+"V");
                    display.getCanvas().clear();
                    display.getCanvas().write("Pump Off");
                    display.getCanvas().setTextsize(2);
                    display.getCanvas().write((int)moisture+"V");
                    display.display();
                    try {
                        pump.setValue(0);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }else{
                    System.out.println("This is an error");
                    stop = 1;//
                }
          //  }

    }
}
