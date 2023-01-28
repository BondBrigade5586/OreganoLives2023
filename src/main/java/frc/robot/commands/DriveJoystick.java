// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.Vision;

public class DriveJoystick extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Vision limelight = RobotContainer.m_vision;
  Gyro gyro = RobotContainer.m_gyro;
  LED leds = RobotContainer.m_led;
  Supplier<Double> speed, turn, speedFactor;

  /** Creates a new driveJoystick. */
  public DriveJoystick(Drivetrain drivetrain, Supplier<Double> speed, Supplier<Double> turn, Supplier<Double> speedFactor) {
    this.drivetrain = drivetrain;
    this.speed = speed;
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

    if (speedFactor.get() <= 1 && speedFactor.get() > 0.25) {
      kSpeedFactor = 0.5;
      kTurnFactor = 0.5;
    } else if (speedFactor.get() <= 0.25 && speedFactor.get() >= -0.25) {
      kSpeedFactor = 0.75;
      kTurnFactor = 0.6;
    } else if (speedFactor.get() < -0.25) {
      kSpeedFactor = 1.00;
      kTurnFactor = 0.7;
    }

    // Sets color of LED lights based on move direction
    if (kSpeed > 0) {
      for (int i = 0;i<=leds.getLength();i++) {
        leds.setColorRGB(i, 0, 255, 0);
      }
    } else if (kSpeed < 0) {
      for (int i = 0;i<=leds.getLength();i++) {
        leds.setColorRGB(i, 255, 0, 0);
      }
    } else {
      for (int i = 0;i<=leds.getLength();i++) {
        leds.setColorRGB(i, 255, 255, 255);
      }
    }

    drivetrain.driveRobot(kSpeed, kTurn, kSpeedFactor, kTurnFactor);
    limelight.update();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.driveRobot(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
