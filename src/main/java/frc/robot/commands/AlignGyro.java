// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;

public class AlignGyro extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Gyro gyro = RobotContainer.m_gyro;
  double z;
  double sp;
  double error;
  double errorSum;
  double errorRate;
  double lastError;
  double prevTimestamp;
  double outputSpeed;

  Supplier<Double> setpt;

  /** Creates a new AlignGyro. */
  public AlignGyro(Supplier<Double> setpoint) {
    this.setpt = setpoint;

    lastError = 0;
    prevTimestamp = Timer.getFPGATimestamp();
    addRequirements(drivetrain);
    addRequirements(gyro);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    sp = setpt.get();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    z = gyro.getZRotation();

    if (sp > 360) {
      sp -= 360;
    }
    // Converts z and setpoint values to 360-degree scale (0<x<360)
    if (sp < 0) {
      sp = (360-Math.abs(sp));
    }
    if (z<0) {
      z = (360-Math.abs(z));
    }

    // Finds distance between current position and setpoint
    error = Math.min(Math.abs(sp-z), 360-Math.abs(z-sp));
    errorSum += error * (Timer.getFPGATimestamp() - prevTimestamp);
    errorRate = (error - lastError) / (Timer.getFPGATimestamp() - prevTimestamp);

    outputSpeed = error*DriveConstants.kGyroP /* + errorSum*DriveConstants.kGyroI + errorRate*DriveConstants.kGyroD */;

    // Sets turn speed parameters
    if (outputSpeed > 0.65) {
      outputSpeed = 0.65;
    } else if (outputSpeed < -0.65) {
      outputSpeed = -0.65;
    } else if (outputSpeed < 0.43 && outputSpeed > 0) {
      outputSpeed = 0.43;
    } else if (outputSpeed > -0.43 && outputSpeed < 0) {
    }
    // Prevents turning more than 180 degrees, uses PID to determine setpoint speeds
    if (Math.abs(sp-z) <= 180 && sp>z) {
      drivetrain.turnRobot(outputSpeed);
    } else if (Math.abs(sp-z) <= 180 && z>sp) {
      drivetrain.turnRobot(-outputSpeed);
    } else if (Math.abs(sp-z) > 180 && sp>z) {
      drivetrain.turnRobot(-outputSpeed);
    } else if (Math.abs(sp-z) > 180 && z>sp) {
      drivetrain.turnRobot(outputSpeed);
    }

    prevTimestamp = Timer.getFPGATimestamp();
    lastError = error;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    error = 0;
    errorSum = 0;
    lastError = 0;
    sp = 0;
    
    // Stops robot
    drivetrain.stopRobot();
    drivetrain.enableBrakes();

    // Robot.m_drivetrainCommand.schedule(); // Reschedules drivetrain command
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (error<1 && error>-1);
  }
}
