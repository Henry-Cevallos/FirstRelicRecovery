/*
Henry Cevallos
This is the Autonomous program for the First Relic Recovery Challenge
Team: Rolling Drones
*/
package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import com.qualcomm.robotcore.hardware.CRServo;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
/*
 *
 * This is an example LinearOpMode that shows how to use
 * a legacy (NXT-compatible) Hitechnic Color Sensor v2.
 * It assumes that the color sensor is configured with a name of "sensor_color".
 *
 * You can use the X button on gamepad1 to toggle the LED on and off.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "[R]AutonomousPeriod", group = "Sensor")

public class AutonomousPeriod extends LinearOpMode {

    ColorSensor colorSensor;  // Hardware Device Object
   
   //Autonomous motor + servo set up
   
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor topLeft = null;    
    private DcMotor topRight = null;
    private DcMotor botLeft = null;
    private DcMotor botRight = null;
    private DcMotor height = null;
    private DcMotor relic = null;
    private DcMotor glyph = null; 

    Servo servoLeft;



    @Override
    public void runOpMode() {

        //Sets Up DC Motors
        topLeft  = hardwareMap.get(DcMotor.class, "topLeft");
        topRight = hardwareMap.get(DcMotor.class, "topRight");
        botLeft  = hardwareMap.get(DcMotor.class, "botLeft");
        botRight = hardwareMap.get(DcMotor.class, "botRight");
        height = hardwareMap.get(DcMotor.class, "height");
        glyph = hardwareMap.get(DcMotor.class, "glyph");
    
        //Sets up servos
        servoLeft = hardwareMap.servo.get("servoLeft");
  
        //Finish up configuration of DC motors
        topLeft.setDirection(DcMotor.Direction.FORWARD);
        topRight.setDirection(DcMotor.Direction.REVERSE);
        botLeft.setDirection(DcMotor.Direction.REVERSE);
        botRight.setDirection(DcMotor.Direction.FORWARD);
        height.setDirection(DcMotor.Direction.FORWARD);
        glyph.setDirection(DcMotor.Direction.REVERSE);

         
    
        //Lines 77-103 were taken from a testing program
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a refrence to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        // bPrevState and bCurrState represent the previous and current state of the button.
        boolean bPrevState = false;
        boolean bCurrState = false;

        // bLedOn represents the state of the LED.
        boolean bLedOn = true;

        // get a reference to our ColorSensor object.
        colorSensor = hardwareMap.colorSensor.get("sensor_color");

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        colorSensor.enableLed(bLedOn);

        // wait for the start button to be pressed.
        waitForStart();
    
    
    
    
    
        //Sets Servo into starting position
        servoLeft.setPosition(1.5);
    
    
    

        // loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive())  {

            // check the status of the x button on gamepad1.
            bCurrState = gamepad1.x;

            // check for button state transitions.
            if (bCurrState && (bCurrState != bPrevState))  {

                // button is transitioning to a pressed state.  Toggle LED.
                // on button press, enable the LED.
                bLedOn = !bLedOn;
                colorSensor.enableLed(bLedOn);
            }

            // update previous state variable.
            bPrevState = bCurrState;

            // convert the RGB values to HSV values.
            Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
        });
      
//CONFIGURATION AND SET UP COMPLETE
//ROBOT DIRECTIONS ARE WHAT FOLLOWS

        sleep(850);
        /* The following checks if the color sensor reads a blue object
        If blue is detected the robot will turn left, drop the servo arm,
        turn right then stop
        */
        if(colorSensor.blue()>=3){
            turnL();
            sleep(300);
            plsStop();
            sleep(150);
            servoLeft.setPosition(.13);
            turnR();
            sleep(300);
            plsStop();
            sleep(150);
        }
        /* The following checks if the color sensor reads a red object
        If blue is detected the robot will turn right, drop the servo arm,
        turn left then stop
        */
        else if(colorSensor.red()>=3){
            turnR();
            sleep(300);
            plsStop();
            sleep(150);
            servoLeft.setPosition(.13);
            turnL();
            sleep(300);
            plsStop();
            sleep(150);
        }

         telemetry.update();
    }


    // Set the panel back to the default color
    relativeLayout.post(new Runnable() {
        public void run() {
            relativeLayout.setBackgroundColor(Color.WHITE);
        }
    });
    


}
  
//Function to make Robot strafe to the right
public void strafeR(){
    botLeft.setPower(-100); 
        
    botRight.setPower(100);
    
    height.setPower(-100); 
      
    glyph.setPower(100);  
                       
}

//This function wil make the robot run forward
public void foward(){
    botLeft.setPower(-100); 
        
    botRight.setPower(-100);
    
    height.setPower(-100); 
      
    glyph.setPower(-100);  
                       
}

//This function will make the robot move backwards
public void backward(){
        botLeft.setPower(100); 
        
        botRight.setPower(100);
    
        height.setPower(100);
      
        glyph.setPower(100); 
                       
}

//This function will make the robot turn left
public void turnL(){
    botLeft.setPower(-25);
        
    botRight.setPower(25);
    
    height.setPower(25);
      
    glyph.setPower(-25);
                       
}

//This function will make the robot turn right
public void turnR(){
    botLeft.setPower(25);
        
    botRight.setPower(-25);
    
    height.setPower(-25); 
      
    glyph.setPower(25);
                       
}

//This function will make the robot stop
public void plsStop(){
    botLeft.setPower(0); 
        
    botRight.setPower(0);
    
    height.setPower(0); 
      
    glyph.setPower(0);
                       
}

  
  
  
  
}//End class AutonomousPeriod