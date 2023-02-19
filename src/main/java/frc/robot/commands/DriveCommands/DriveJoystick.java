// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.DriveCommands;

import java.util.function.Supplier;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.Vision;

public class DriveJoystick extends CommandBase {
  // Encoders
  double encoderInches;
  double encoderFeet;
  double encoderMiles;
  double prevFeet = 0;
  double prevTimestamp = Timer.getFPGATimestamp();
  double velocity;
  
  // Shuffleboard tab info
  ShuffleboardTab tab = RobotContainer.driveTab;
  GenericEntry sbInches = RobotContainer.sbInches;
  GenericEntry sbFeet = RobotContainer.sbFeet;
  GenericEntry sbMiles = RobotContainer.sbMiles;
  GenericEntry sbVelocityFPS = RobotContainer.sbVelFt;
  GenericEntry sbVelocityMPH = RobotContainer.sbVelMi;


  // Subsystem dependencies
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Vision limelight = RobotContainer.m_vision;
  Gyro gyro = RobotContainer.m_gyro;
  LED leds = RobotContainer.m_led;
  Supplier<Double> speed, turn, speedFactor;

  /** Creates a new driveJoystick. */
  public DriveJoystick(Drivetrain drivetrain, Supplier<Double> d, Supplier<Double> turn, Supplier<Double> speedFactor) {
    this.drivetrain = drivetrain;
    this.speed = d;
    this.turn = turn;
    this.speedFactor = speedFactor;

    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.disableBrakes(); // Disables brake mode at beginning of teleop
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double kSpeed = speed.get();
    double kTurn = turn.get();
    double kSpeedFactor = DriveConstants.kDefaultSpeedFactor;
    double kTurnFactor = DriveConstants.kDefaultTurnFactor;
    encoderInches = drivetrain.getEncoderPosition() * DriveConstants.kTicksToInches;
    encoderFeet = encoderInches * DriveConstants.kInchesToFt;
    encoderMiles = encoderFeet * DriveConstants.kFeetToMiles;
    velocity = (encoderFeet - prevFeet) / (Timer.getFPGATimestamp() - prevTimestamp); // Speed in feet/second
    // Update shuffleboard values
    sbInches.setDouble(encoderInches);
    sbFeet.setDouble(encoderFeet);
    sbMiles.setDouble(encoderMiles);
    sbVelocityFPS.setDouble(velocity);
    sbVelocityMPH.setDouble(velocity * DriveConstants.kFPStoMPH);
  
    // updateEncoderShuffleboard(encoderInches, encoderFeet, encoderMiles, velocity);
    
    // Calculate speed and turn factors
    if (speedFactor.get() <= 1 && speedFactor.get() > 0.75) {
      kSpeedFactor = 0.5;
      kTurnFactor = 0.5586;
    } else if (speedFactor.get() <= 0.75 && speedFactor.get() >= -0.75) {
      kSpeedFactor = 0.7;
      kTurnFactor = 0.5586;
    } else if (speedFactor.get() < -0.75) {
      kSpeedFactor = 0.90;
      kTurnFactor = 0.75;
    }

    drivetrain.driveArcade(-kSpeed, kTurn, kSpeedFactor, kTurnFactor);
    setLEDLightColor(-kSpeed);    
    limelight.update();
    prevFeet = encoderFeet;
    prevTimestamp = Timer.getFPGATimestamp();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  /* ------ CUSTOM METHODS ------ */
  public void setLEDLightColor(double speed) {
    if (speed > 0) {
      for (int i = 1;i<leds.getLength();i++) {
        leds.setColorRGB(i, 0, 255, 0);
      }
    } else if (speed < 0) {
      for (int i = 1;i<leds.getLength();i++) {
        leds.setColorRGB(i, 255, 0, 0);
      }
    } else {
      for (int i = 1;i<leds.getLength();i++) {
        leds.setColorRGB(i, 255, 255, 255);
      }
    }
  }
}
