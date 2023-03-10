// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Vision;

public class InlineTapeWall extends CommandBase {
  Vision limelight = RobotContainer.m_vision;
  Drivetrain drivetrain  = RobotContainer.m_drivetrain;
  Gyro gyro = RobotContainer.m_gyro;
  
  double x;
  double z;
  double b;

  double driveSpeed;
  double turnSpeed;
  /** Creates a new InlineTapeWall. */
  public InlineTapeWall() {
    addRequirements(drivetrain);
    addRequirements(limelight);
    addRequirements(gyro);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    x = limelight.getXOffset();
    z = gyro.getZRotation();

    b = 90 - (Math.abs(z) - Math.abs(x));

    // Perpendicular to tape and wall
    if (b<90-VisionConstants.kBOffset) {
      driveSpeed = 0.65;
    } else if (b>90+VisionConstants.kBOffset) {
      driveSpeed = -0.65;
    } else {
      driveSpeed = 0;
    }

    // Keeps tape in horizontal FOV of limelight
    if (x>VisionConstants.kMaxXOffset) {
      turnSpeed = 0.45;
    } else if (x<-VisionConstants.kMaxXOffset) {
      turnSpeed = -0.45;
    } else {
      turnSpeed = 0;
    }

    drivetrain.driveArcade(driveSpeed, turnSpeed, 0.75, 0.70);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (b>90-VisionConstants.kBOffset && b<90+VisionConstants.kBOffset);
  }
}
