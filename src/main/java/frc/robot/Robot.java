// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.util.Color;

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

  private double startTime = 0.0;
  private boolean timerStarted = false;
  private  int colorNumber;
  private int LEDPatternNumber;

  private AddressableLED led;
  private AddressableLEDBuffer ledBuffer;
  private int ledLength = 30; // LEDの数

  XboxController xboxController;

  // 色の定義
  private static final Map<String, Color> UseColors = new HashMap<>();
  static {
    UseColors.put("orange", new Color(255, 50, 0));
    UseColors.put("red", new Color(255, 0, 0));
    UseColors.put("green", new Color(0, 255, 0));
    UseColors.put("blue", new Color(0, 0, 255));
    UseColors.put("yellow", new Color(255, 241, 0));
    UseColors.put("darkRed", new Color(235, 97, 16));
    UseColors.put("lightBlue", new Color(104, 200, 242));
    UseColors.put("purple", new Color(165, 61, 146));
  }


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
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

    // 次の色に変更するまでの処理
    if(xboxController.getRightBumperButtonPressed()){
      timerStarted = false;
    }
  }

  public void modeOne(){
    if(!timerStarted){
      String[] colors = oneModecolors;
      // 色番号を取得
      colorNumber = setColorNumber();
      // 色の名前を取得
      String colorName = colors[colorNumber];
      // 光らせる
      setsolidLED(UseColors.get(colorName));
      startTime = Timer.getFPGATimestamp();
      timerStarted = true;
    }else{
      if(pushSomeButton()){
        double currentTime = Timer.getFPGATimestamp();
        double elapsed = currentTime - startTime;
        int ans = getPushNumber();
        if(ans == colorNumber){
          System.out.println("正解");
          System.out.println(elapsed);
        }else{
          System.out.println("不正解");
          System.out.println(elapsed);
        }
      }
    }
  }

  public void modeTwo(){
    if(!timerStarted){
      String[] colors = twoModecolors;
      // 色番号を取得
      colorNumber = setColorNumber();
      // 色の名前を取得
      String colorName = colors[colorNumber];
      // 光らせる
      setsolidLED(UseColors.get(colorName));
      startTime = Timer.getFPGATimestamp();
      timerStarted = true;
    }else{
      if(pushSomeButton()){
        double currentTime = Timer.getFPGATimestamp();
        double elapsed = currentTime - startTime;
        int ans = getPushNumber();
        if(ans == colorNumber){
          System.out.println("正解");
          System.out.println(elapsed);
        }else{
          System.out.println("不正解");
          System.out.println(elapsed);
        }
      }
    }
  }
  public void modeThree(){
    if(!timerStarted){
      // パターン番号を取得
      LEDPatternNumber = setColorNumber();
      // 光らせる
      switch (LEDPatternNumber){
        case 0:
          setsolidLED(Color.kWhite); // 点灯
          break;
        case 1:
          blinkWhite(0.3); // 早い点滅（0.3秒）
          break;
        case 2:
          blinkWhite(1.5); // 遅い点滅（1.5秒）
          break;
        case 3:
          scrollWhite();   // スクロール
          break;
      }
      startTime = Timer.getFPGATimestamp();
      timerStarted = true;
    }else{
      if(pushSomeButton()){
        double currentTime = Timer.getFPGATimestamp();
        double elapsed = currentTime - startTime;
        int ans = getPushNumber();
        if(ans == LEDPatternNumber){
          System.out.println("正解");
          System.out.println(elapsed);
        }else{
          System.out.println("不正解");
          System.out.println(elapsed);
        }
      }
    }
  }
  public  void modeFour(){

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

  // 白色の点滅
  public void blinkWhite(double intervalSeconds) {
    double time = Timer.getFPGATimestamp();
    double period = intervalSeconds * 2;

    if ((time % period) < intervalSeconds) {
      setsolidLED(Color.kWhite);
    } else {
      setsolidLED(Color.kBlack);
    }
  }

  // 白色スクロール
  public void scrollWhite() {
    int step = (int)(Timer.getFPGATimestamp() * 10) % ledLength;

    for (int i = 0; i < ledLength; i++) {
      if (i == step) {
        ledBuffer.setLED(i, Color.kWhite);
      } else {
        ledBuffer.setLED(i, Color.kBlack);
      }
    }

    led.setData(ledBuffer);
  }

  public boolean pushSomeButton(){
    return xboxController.getAButton() || xboxController.getBackButton() || xboxController.getXButton() || xboxController.getYButton();
  }

  public int getPushNumber(){
    if(xboxController.getAButton()){
      return 0;
    }else if(xboxController.getBButton()){
      return 1;
    }else if(xboxController.getYButton()){
      return 2;
    }else if(xboxController.getXButton()){
      return  3;
    }else{
      return 4;
    }
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
