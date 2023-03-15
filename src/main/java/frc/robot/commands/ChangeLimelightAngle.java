// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Vision;

public class ChangeLimelightAngle extends CommandBase {
  Vision limelight = RobotContainer.m_vision;

  boolean kDirectionUp;
  double sp;
  /** Creates a new ChangeLimelightAngle. */
  public ChangeLimelightAngle(boolean up) {
    addRequirements(limelight);
    this.kDirectionUp = up;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  } 

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (kDirectionUp) {
      sp = limelight.getLimelightAngle()+0.5;
    } else {
      sp = limelight.getLimelightAngle()-0.5;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
