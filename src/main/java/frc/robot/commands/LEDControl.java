// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.regex.MatchResult;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.IntakePiece;
import frc.robot.subsystems.LED;

public class LEDControl extends CommandBase {
  IntakePiece intake = RobotContainer.m_intake;
  LED led = RobotContainer.m_led;
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
    if (RobotState.isDisabled()) {
      LEDPulseWhite();
    } else if (RobotState.isAutonomous()) {
      LEDFlashWhite();
    } else if (RobotState.isTeleop()) {
      if (!intake.pieceInIntake()) {
        LEDSolidGreen();
      } else {
        LEDSolidRed();
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
  private void LEDSolidGreen() {
    for (int i=0;i<led.getLength();i++) {
      led.setColorRGB(i, 0, 150, 0);
    }
  }
  private void LEDSolidRed() {
    for (int i=0;i<led.getLength();i++) {
      led.setColorRGB(i,150, 0, 0);
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
}