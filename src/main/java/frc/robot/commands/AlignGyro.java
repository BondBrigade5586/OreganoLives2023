// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;

public class AlignGyro extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Gyro gyro = RobotContainer.m_gyro;
  int sp = DriveConstants.kGyroSPAngle;
  double z;
  double error;
  double errorSum;
  double prevTimestamp = Timer.getFPGATimestamp();

  /** Creates a new AlignGyro. */
  public AlignGyro() {
    addRequirements(drivetrain);
    addRequirements(gyro);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    z = gyro.getZRotation();

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
    SmartDashboard.putNumber("Gyro Z Error", error);

    // Prevents turning more than 180 degrees
    if (Math.abs(sp-z) <= 180 && sp>z) {
      drivetrain.turnRobot(error*DriveConstants.kGyroP + errorSum*DriveConstants.kGyroI);

    } else if (Math.abs(sp-z) <= 180 && z>sp) {
      drivetrain.turnRobot(-error*DriveConstants.kGyroP - errorSum*DriveConstants.kGyroI);

    } else if (Math.abs(sp-z) > 180 && sp>z) {
      drivetrain.turnRobot(-error*DriveConstants.kGyroP - errorSum*DriveConstants.kGyroI);
    
    } else if (Math.abs(sp-z) > 180 && z>sp) {
      drivetrain.turnRobot(error*DriveConstants.kGyroP + errorSum*DriveConstants.kGyroI);
    }

    prevTimestamp = Timer.getFPGATimestamp();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    error = 0;
    errorSum = 0;
    drivetrain.driveRobot(0, 0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (gyro.getZRotation() <= DriveConstants.kGyroSPAngle+1 && gyro.getZRotation() >= DriveConstants.kGyroSPAngle-1);
  }
}
