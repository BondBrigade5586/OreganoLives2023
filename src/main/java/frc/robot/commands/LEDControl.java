// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.regex.MatchResult;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.IntakePiece;
import frc.robot.subsystems.LED;

public class LEDControl extends CommandBase {
  IntakePiece intake = RobotContainer.m_intake;
  LED led = RobotContainer.m_led;

  int firstLEDHue, previousLEDIndex = 0;
  int previousAltLEDIndex = led.getLength()-1;
  boolean reverseDirection = false;

  /** Creates a new LEDControl. */
  public LEDControl() {
    addRequirements(led);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // LED Defaults
    if (Robot.runtime.get()<5) {
      LEDPoliceLights();
    } else if (RobotState.isDisabled()) {
        // LEDDoubleBounce();
        LEDProgressiveStaticRainbow();
    } else if (RobotState.isAutonomous()) {
      LEDFlashWhite();
    } else if (RobotState.isTeleop()) { 
      if (intake.pieceInIntake()) {
        LEDSolidGreen(); // Green if intake is loaded
      } else {
        // Alliance color if no piece in intake
        if (DriverStation.getAlliance() == Alliance.Red) {
          LEDSolidRed();
        } else {
          LEDSolidBlue();
        }
      }
    } else if (RobotState.isEStopped()) {
      LEDFlashRed();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
  
  // LED Modes
  private void LEDSolidRed() {
    for (int i=0;i<led.getLength();i++) {
      led.setColorRGB(i,150, 0, 0);
    }
  }
  private void LEDSolidGreen() {
    for (int i=0;i<led.getLength();i++) {
      led.setColorRGB(i, 0, 150, 0);
    }
  }
  private void LEDSolidBlue() {
    for (int i=0;i<led.getLength();i++) {
      led.setColorRGB(i, 0, 0, 150);
    }
  }

  private void LEDRapidFlashWhite() {
    if (((int)(4*Robot.runtime.get())) %8 == 0 || ((int)(4*Robot.runtime.get())) %8 == 2 || ((int)(4*Robot.runtime.get())) %8 == 4 || ((int)(4*Robot.runtime.get())) %8 == 6) {
      for (int i=0;i<led.getLength();i++) {
        led.setColorRGB(i, 75, 75, 75);
      }
    } else if (((int)(4*Robot.runtime.get())) %4 == 1 || ((int)(4*Robot.runtime.get())) %4 == 3 || ((int)(4*Robot.runtime.get())) %8 == 5 || ((int)(4*Robot.runtime.get())) %8 == 7) {
      for (int i=0;i<led.getLength();i++) {
        led.setColorRGB(i, 0, 0, 0);
      }
    }
  }
  private void LEDFlashWhite() {
    if (((int)(2*Robot.runtime.get())) %4 == 0 || ((int)(2*Robot.runtime.get())) %4 == 2) {
      for (int i=0;i<led.getLength();i++) {
        led.setColorRGB(i, 75, 75, 75);
      }
    } else if (((int)(2*Robot.runtime.get())) %4 == 1 || ((int)(2*Robot.runtime.get())) %4 == 3) {
      for (int i=0;i<led.getLength();i++) {
        led.setColorRGB(i, 0, 0, 0);
      }
    } 
  }
  private void LEDFlashRed() {
    if (((int)(2*Robot.runtime.get())) %4 == 0 || ((int)(2*Robot.runtime.get())) %4 == 2) {
      for (int i=0;i<led.getLength();i++) {
        led.setColorRGB(i, 150, 0, 0);
      }
    } else if (((int)(2*Robot.runtime.get())) %4 == 1 || ((int)(2*Robot.runtime.get())) %4 == 3) {
      for (int i=0;i<led.getLength();i++) {
        led.setColorRGB(i, 0, 0, 0);
      }
    }
  }
  private void LEDFlashBlue() {
    if (((int)(2*Robot.runtime.get())) %4 == 0 || ((int)(2*Robot.runtime.get())) %4 == 2) {
      for (int i=0;i<led.getLength();i++) {
        led.setColorRGB(i, 0, 0, 150);
      }
    } else if (((int)(2*Robot.runtime.get())) %4 == 1 || ((int)(2*Robot.runtime.get())) %4 == 3) {
      for (int i=0;i<led.getLength();i++) {
        led.setColorRGB(i, 0, 0, 0);
      }
    }
  }

  private void LEDPulseWhite() {
    int v;
    v = (int)(50*(Robot.runtime.get() % 5));
    if (Robot.runtime.get() % 5 >= 2.5) {
      v = 250-(int)(50*(Robot.runtime.get() % 5));
    }
    for (int i=0;i<led.getLength();i++) {
      led.setColorHSV(i, 0, 0, v);
    }
  }

  private void LEDPoliceLights() {
    if (((int)(2*Robot.runtime.get())) %4 == 0 || ((int)(2*Robot.runtime.get())) %4 == 2) {
      for (int i=0; i<led.getLength()/2; i++) {
        led.setColorRGB(i, 0, 0, 150);
      }
      for (int i=led.getLength()/2; i<led.getLength(); i++) {
        led.setColorRGB(i, 150, 0, 0);
      }
    } else if (((int)(2*Robot.runtime.get())) %4 == 1 || ((int)(2*Robot.runtime.get())) %4 == 3) {
      for (int i=0; i<led.getLength()/2; i++) {
        led.setColorRGB(i, 150, 0, 0);
      }
      for (int i=led.getLength()/2; i<led.getLength(); i++) {
        led.setColorRGB(i, 0, 0, 150);
      }
    }
  }

  public void LEDDoubleBounce() {
    int altI;    
    // Determines direction the LED travels & checks bounds
    if (previousLEDIndex >= led.getLength()-1) {
      reverseDirection = true;
    } else if (previousLEDIndex <= 0) {
      reverseDirection = false;
    }

    // Sets LED strip colors
    for (int i=0;i<led.getLength();i++) {
      altI = led.getLength()-1-i;

      if (i==previousLEDIndex) {
        if (!reverseDirection) {
          led.setColorRGB(altI-1, 0, 0, 150);
          led.setColorRGB(altI, 0, 0, 75);
          // Set LED at i to dim green and LED at i+1 to bright green
          led.setColorRGB(i+1, 150, 0, 0);
          led.setColorRGB(i, 75, 0, 0);
        } else {
          led.setColorRGB(altI+1, 0, 0, 150);
          led.setColorRGB(altI, 0, 0, 75);
          // Set LED at i to dim green and LED at i-1 to bright green
          led.setColorRGB(i-1, 150, 0, 0);
          led.setColorRGB(i, 75, 0, 0);
        }
      } else if (i!=previousLEDIndex && i!=previousLEDIndex+1 && i!=previousLEDIndex-1 && i!=previousAltLEDIndex && i!=previousAltLEDIndex+1 && i!=previousAltLEDIndex-1) {
        // Set LED at i to off
        led.setColorRGB(i, 0, 0, 0);
      }
    }
    if (!reverseDirection) {
      previousLEDIndex++; // Increase previous bright LED index
      previousAltLEDIndex--;
    } else {
      previousLEDIndex--; // Decrease previous bright LED index
      previousAltLEDIndex++;
    }
  }

  private void ShootSingleLED(int startIndex) {
    for (int i=0;i<led.getLength();i++) {
      if (i==startIndex) {
        led.setColorRGB(i, 135, 135, 135);
      } else {
        led.setColorRGB(i-1, 0, 0, 0);
      }
    }

    if (startIndex>=led.getLength()-1) {
      return;
    } else {
      ShootSingleLED(startIndex+1);
    }
  }

  // **** RAINBOW LED Methods ****
  private void LEDEntireStripRainbow() {
   double cycleTime = 6; // Time between rainbow cycles
   double maxH = 180; // Maximum value of H
   
   int h = (int) (Robot.runtime.get() * (maxH/cycleTime));
   h %= maxH; // Check bounds
   for (int i=0;i<led.getLength();i++) {
    led.setColorHSV(i, h, 255, 255);
   }
  }
  private void LEDProgressiveStaticRainbow() {
    double cycleTime = 1.0;
    double LEDCount = led.getLength();
    double maxH = 180;
    double hueIncreaseInterval = LEDCount/maxH;

    firstLEDHue = (int) (Robot.runtime.get() * (maxH/cycleTime));

    for (int i=0;i<LEDCount;i++) {
      int h = (int) (i * hueIncreaseInterval); // Set base hue (0-180 based on index)
      h += firstLEDHue; // Makes rainbow move
      h %= maxH; // Check bounds

      led.setColorHSV(i, h, 255, 255);
    }
  }
}