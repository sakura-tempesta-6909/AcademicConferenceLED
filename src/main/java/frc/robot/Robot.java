// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  // 何個目の実験をするか　例:　イ　→　1   ロ　→　2
  int experimentMode = 1;



  private AddressableLED led;
  private AddressableLEDBuffer ledBuffer;
  private int ledLength = 30; // LEDの数


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
  }


  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    // 色の定義
    Map<String,Color> Colors = new HashMap<>();
    Colors.put("orange",new Color(255, 50, 0));
    Colors.put("red",new Color(255, 0, 0));
    Colors.put("green",new Color(0, 255, 0));
    Colors.put("blue",new Color(0, 0, 255));
    Colors.put("yerro",new Color(255, 241, 0));
    Colors.put("darkRed",new Color(235, 97, 16));
    Colors.put("lightBlue",new Color(104, 200, 242));
    Colors.put("purple",new Color(165, 61, 146));

    led = new AddressableLED(0);
    ledBuffer = new AddressableLEDBuffer(ledLength);

    led.setLength(ledBuffer.getLength());
    led.setData(ledBuffer);
    led.start();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    switch(experimentMode){
      case 1:
        modeOne();
        break;
      case 2:
        modeTwo();
        break;
      case 3:
        modeThree();
        break;
      case 4:
        modeFour();
        break;
      default:
        break;
    }
  }

  public void modeOne(){
    int colorNumber = setColorNumber();
  }
  public void modeTwo(){

  }
  public void modeThree(){

  }
  public  void modeFour(){

  }

  public static int setColorNumber() {
    Random rand = new Random();
    return rand.nextInt(4) + 1;
  }

  public void setLED(int R,int G,int B){
    // Create an LED pattern that sets the entire strip to solid red
    LEDPattern red = LEDPattern.solid(new Color(R,G,B));

    // Apply the LED pattern to the data buffer
    red.applyTo(ledBuffer);

    // Write the data to the LED strip
    led.setData(ledBuffer);
    led.start();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
