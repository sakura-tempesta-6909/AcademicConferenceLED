// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Random;
import java.util.Map;
import java.lang.ModuleLayer.Controller;
import java.util.HashMap;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  // 何個目の実験をするか　例:　イ　→　1   ロ　→　2
  int experimentMode = 1;

  String[] oneModecolors = {"orange", "red", "green", "blue"};
  String[] twoModecolors = {"yellow", "darkRed", "lightBlue", "purple"};
  String[] fourModecolors = {"yellow", "darkRed", "lightBlue", "purple"};
  String[] fiveModecolors = {"yellow_new", "violet", "blue_new", "orange_new"};

  private double startTime = 0.0;
  private boolean timerStarted = false;
  private  int colorNumber;
  private int LEDPatternNumber;

  private AddressableLED led;
  private AddressableLEDBuffer ledBuffer;
  private int ledLength = 32; // LEDの数

  private double waitStartTime = 0.0;
  private double waitDuration = 0.0;
  private boolean isWaiting = false;

  private boolean shouldResetAnser = false;
  private double resetAnserTime = 0.0;
private int counter;

  XboxController xboxController;

  // 色の定義
  private static final Map<String, Color> UseColors = new HashMap<>();
  static {
    UseColors.put("orange", new Color(255, 50, 0));
    UseColors.put("red", new Color(255, 0, 0));
    UseColors.put("green", new Color(0, 255, 0));
    UseColors.put("blue", new Color(0, 0, 255));

    UseColors.put("yellow", new Color(255/2, 241/2, 0));
    UseColors.put("darkRed", new Color(235/2, 97/2, 16/2));
    UseColors.put("lightBlue", new Color(104/2, 200/2, 242/2));
    UseColors.put("purple", new Color(165/2, 61/2, 146/2));

    UseColors.put("yellow_new", new Color(225/2,255/2,0/2));
    UseColors.put("blue_new", new Color(0/2,65/2,255/2));
    UseColors.put("violet", new Color(150/2,0/2,102/2));
    UseColors.put("orange_new", new Color(255/2,100/2,0));
  }


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
  }

  @Override
  public void robotInit() {
    DataLogManager.start();
  }


  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    xboxController = new XboxController(0);
    led = new AddressableLED(0);
    ledBuffer = new AddressableLEDBuffer(ledLength);

    led.setLength(ledBuffer.getLength());
    led.setData(ledBuffer);
    led.start();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // SmartDashboard.putNumber("pov", xboxController.getPOV());
    if (xboxController.getPOV() == 0) {
      experimentMode = 1;
      DataLogManager.log("mode 1");counter=0;
    } else if (xboxController.getPOV() == 90) {
      experimentMode = 2;
      DataLogManager.log("mode 2");counter=0;
    } else if (xboxController.getPOV() == 180) {
      experimentMode = 3;
      DataLogManager.log("mode 3");counter=0;
    } else if (xboxController.getPOV() == 270) {
      experimentMode = 4;
      DataLogManager.log("mode 4");counter=0;
    } else if (xboxController.getBackButtonPressed()) {
      experimentMode = 5;
      DataLogManager.log("mode 5");counter=0;
    }
    SmartDashboard.putString("current mode", "Mode" + experimentMode);
    
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
      case 5:
        modeFive();
      default:
        break;
    }

    // 次の色に変更するまでの処理
    if(xboxController.getRightBumperButtonPressed()){
      timerStarted = false;
      shouldResetAnser = true;
      resetAnserTime = Timer.getFPGATimestamp() + 1.0;
    }
    if (shouldResetAnser && Timer.getFPGATimestamp() >= resetAnserTime) {
      SmartDashboard.putBoolean("anser", false);
      shouldResetAnser = false;
    }
  }

  public void modeOne(){
    if(!timerStarted){
      if (!isWaiting) {
        waitStartTime = Timer.getFPGATimestamp();
        waitDuration = 1.0 + new Random().nextDouble(); 
        isWaiting = true;
        setsolidLED(new Color(0, 0, 0));
      } else {
        if (Timer.getFPGATimestamp() - waitStartTime >= waitDuration) {
          String[] colors = oneModecolors;
          // 色番号を取得
          colorNumber = setColorNumber();
          // 色の名前を取得
          String colorName = colors[colorNumber];
          counter++;
          DataLogManager.log(counter + "," + colorName + ",");
          // 光らせる
          setsolidLED(UseColors.get(colorName));
          startTime = Timer.getFPGATimestamp();
          timerStarted = true;
          isWaiting = false;
          SmartDashboard.putNumber("start time", startTime);
        } 
      }
    } else {
          if (pushSomeButtonPressed()){
            double currentTime = Timer.getFPGATimestamp();
            SmartDashboard.putNumber("current time", currentTime);
            double elapsed = currentTime - startTime;
            int ans = getPushNumber();
            if (ans == colorNumber){
              SmartDashboard.putBoolean("anser",true);
              SmartDashboard.putNumber("time",elapsed);
              DataLogManager.log(elapsed + ",ok");
            } else {
              SmartDashboard.putBoolean("anser",false);
              SmartDashboard.putNumber("time",elapsed);
              DataLogManager.log(elapsed + ",ng");
            }
          }
      }
  }

  public void modeTwo(){
    if(!timerStarted){
      if (!isWaiting) {
        waitStartTime = Timer.getFPGATimestamp();
        waitDuration = 1.0 + new Random().nextDouble(); 
        isWaiting = true;
        setsolidLED(new Color(0, 0, 0));
      } else {
        if (Timer.getFPGATimestamp() - waitStartTime >= waitDuration) {
          String[] colors = twoModecolors;
          // 色番号を取得
          colorNumber = setColorNumber();
          // 色の名前を取得
          String colorName = colors[colorNumber];
          counter++;
          DataLogManager.log(counter + "," + colorName + ",");
          // 光らせる
          setsolidLED(UseColors.get(colorName));
          startTime = Timer.getFPGATimestamp();
          timerStarted = true;
          isWaiting = false;
        } 
      } 
    } else {
        if (pushSomeButtonPressed()){
          double currentTime = Timer.getFPGATimestamp();
          double elapsed = currentTime - startTime;
          int ans = getPushNumber();
          if (ans == colorNumber){
            SmartDashboard.putBoolean("anser",true);
            SmartDashboard.putNumber("time",elapsed);
            DataLogManager.log(elapsed + ",ok");
          } else {
            SmartDashboard.putBoolean("anser",false);
            SmartDashboard.putNumber("time",elapsed);
            DataLogManager.log(elapsed + ",ng");
          }
        }
      }
  }

  public void modeThree(){
    if(!timerStarted){
      if (!isWaiting) {
        LEDPatternNumber = setColorNumber();
        waitStartTime = Timer.getFPGATimestamp();
        waitDuration = 1.0 + new Random().nextDouble(); 
        isWaiting = true;
        setsolidLED(new Color(0, 0, 0));
      } else {
        if (Timer.getFPGATimestamp() - waitStartTime >= waitDuration) {
          startTime = Timer.getFPGATimestamp();
          timerStarted = true;
          isWaiting = false;
                    counter++;
          DataLogManager.log(counter + "," + LEDPatternNumber + ",");
        } 
      } 
    } else {
      // 光らせる
      switch (LEDPatternNumber){
        case 0:
          setsolidLED(Color.kWhite); // 点灯
          break;
        case 1:
          blinkWhite(Color.kWhite,0.1); // 早い点滅（0.3秒）
          break;
        case 2:
          blinkWhite(Color.kWhite,0.3); // 遅い点滅（1.5秒）
          break;
        case 3:
          scrollWhite(Color.kWhite);   // スクロール
          break;
      }
      SmartDashboard.putNumber("case", LEDPatternNumber);
          if (pushSomeButtonPressed()){
            double currentTime = Timer.getFPGATimestamp();
            double elapsed = currentTime - startTime;
            int ans = getPushNumber();
            if (ans == LEDPatternNumber){
              SmartDashboard.putBoolean("anser",true);
              SmartDashboard.putNumber("time",elapsed);
              DataLogManager.log(elapsed + ",ok");
            } else {
              SmartDashboard.putBoolean("anser",false);
              SmartDashboard.putNumber("time",elapsed);
              DataLogManager.log(elapsed + ",ng");
            }
          }
      }
  }

  public void modeFour(){
    if(!timerStarted){
      if (!isWaiting) {
        LEDPatternNumber = setColorNumber();
        waitStartTime = Timer.getFPGATimestamp();
        waitDuration = 1.0 + new Random().nextDouble(); 
        isWaiting = true;
        setsolidLED(new Color(0, 0, 0));
      } else {
        if (Timer.getFPGATimestamp() - waitStartTime >= waitDuration) {
          startTime = Timer.getFPGATimestamp();
          timerStarted = true;
          isWaiting = false;
          counter++;
          DataLogManager.log(counter + "," + LEDPatternNumber + ","); 
        } 
      }
    } else {
      // 光らせる
      switch (LEDPatternNumber){
        case 0:
          setsolidLED(UseColors.get(fourModecolors[0])); // 点灯
          break;
        case 1:
          blinkWhite(UseColors.get(fourModecolors[1]),0.1); // 早い点滅（0.3秒）
          break;
        case 2:
          blinkWhite(UseColors.get(fourModecolors[2]),0.3); // 遅い点滅（1.5秒）
          break;
        case 3:
          scrollWhite(UseColors.get(fourModecolors[3]));   // スクロール
          break;
      }
      SmartDashboard.putNumber("case", LEDPatternNumber);
          if (pushSomeButtonPressed()){
            double currentTime = Timer.getFPGATimestamp();
            double elapsed = currentTime - startTime;
            int ans = getPushNumber();
            if (ans == LEDPatternNumber){
              SmartDashboard.putBoolean("anser",true);
              SmartDashboard.putNumber("time",elapsed);
              DataLogManager.log(elapsed + ",ok");
            } else {
              SmartDashboard.putBoolean("anser",false);
              SmartDashboard.putNumber("time",elapsed);
              DataLogManager.log(elapsed + ",ng");
            }
          }
      }
  }

  public void modeFive() {
    if(!timerStarted){
      if (!isWaiting) {
        waitStartTime = Timer.getFPGATimestamp();
        waitDuration = 1.0 + new Random().nextDouble(); 
        isWaiting = true;
        setsolidLED(new Color(0, 0, 0));
      } else {
        if (Timer.getFPGATimestamp() - waitStartTime >= waitDuration) {
          String[] colors = fiveModecolors;
          // 色番号を取得
          colorNumber = setColorNumber();
          // 色の名前を取得
          String colorName = colors[colorNumber];
          counter++;
          DataLogManager.log(counter + "," + colorName + ",");
          // 光らせる
          setsolidLED(UseColors.get(colorName));
          startTime = Timer.getFPGATimestamp();
          timerStarted = true;
          isWaiting = false;
        } 
      } 
    } else {
        if (pushSomeButtonPressed()){
          double currentTime = Timer.getFPGATimestamp();
          double elapsed = currentTime - startTime;
          int ans = getPushNumber();
          if (ans == colorNumber){
            SmartDashboard.putBoolean("anser",true);
            SmartDashboard.putNumber("time",elapsed);
            DataLogManager.log(elapsed + ",ok");
          } else {
            SmartDashboard.putBoolean("anser",false);
            SmartDashboard.putNumber("time",elapsed);
            DataLogManager.log(elapsed + ",ng");
          }
        }
      }
  }

  public int setColorNumber() {
    Random rand = new Random();
    return rand.nextInt(4);
  }

  public void setsolidLED(Color color){
    // Create an LED pattern that sets the entire strip to solid red
    LEDPattern red = LEDPattern.solid(color);

    // Apply the LED pattern to the data buffer
    red.applyTo(ledBuffer);

    // Write the data to the LED strip
    led.setData(ledBuffer);
    led.start();
  }

  // 点滅
  public void blinkWhite(Color color ,double intervalSeconds) {
    double time = Timer.getFPGATimestamp();
    double period = intervalSeconds * 2;

    if ((time % period) < intervalSeconds) {
      setsolidLED(color);
    } else {
      setsolidLED(new Color(0,0,0));
    }
  }

  // 白色スクロール
  public void scrollWhite(Color color) {
    int step = (int)(Timer.getFPGATimestamp() * 100) % ledLength;
    for (int i = 0; i < ledLength; i++) {
      if (i == step) {
          ledBuffer.setLED(i, color);
      } else {
        // バックグラウンド色を黒（消灯）に設定
        ledBuffer.setLED(i, new Color(0, 0, 0));
      }
    }
    led.setData(ledBuffer);
  }

  public boolean pushSomeButtonPressed(){
    return xboxController.getAButtonPressed() || xboxController.getBButtonPressed() || xboxController.getXButtonPressed() || xboxController.getYButtonPressed();
  }

  public int getPushNumber(){
    String pressedButton = "None";
    int buttonNumber = 4;
    if(xboxController.getAButton()){
      pressedButton = "橙・黄色";
      buttonNumber = 0;
    }else if(xboxController.getBButton()){
      pressedButton = "青・ダークレッド";
      buttonNumber = 1;
    }else if(xboxController.getYButton()){
      pressedButton = "緑・水色";
      buttonNumber = 2;
    }else if(xboxController.getXButton()){
      pressedButton = "青・紫";
      buttonNumber = 3;
    }else{
      buttonNumber = 4;
    }
    SmartDashboard.putString("Pressed Button", pressedButton);
    return buttonNumber;
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
