// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.AutonomousConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;

public class ExitCommunity extends CommandBase {

  // Shuffleboard tab info
  ShuffleboardTab tab = RobotContainer.driveTab;
  GenericEntry sbSpeed = RobotContainer.sbOutputSpeed;
  GenericEntry sbError = RobotContainer.sbDistError;

  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  double sp;
  double outputSpeed;

  boolean inverted;

  double error;
  double positionTicks; // Position of robot in ticks
  double positionIn; // Position of robot in feet


  /** Creates a new DriveDistance. */
  public ExitCommunity(boolean reverse, boolean center) {
    // SETPOINT MUST BE IN INCHES
    addRequirements(drivetrain);
    this.inverted = reverse;
    if (center) {
      this.sp = AutonomousConstants.kDistOutsideCommunityCenter; 
    } else {
      this.sp = AutonomousConstants.kDistOutsideCommunitySide;
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (inverted) {
      sp *= -1;
    }
    drivetrain.disableBrakes();
    drivetrain.resetEncoderPosition();
    positionTicks = drivetrain.getEncoderPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    positionTicks = drivetrain.getEncoderPosition();
    positionIn = positionTicks * DriveConstants.kTicksToInches;

    error = positionIn - sp;
    outputSpeed = -error * DriveConstants.kDistanceP;
    
    if (outputSpeed > 0.75) { // Ranges speed output from 0.40 to 0.75
      outputSpeed = 0.75;
    } else if (outputSpeed < -0.75) {
      outputSpeed = -0.75;
    } else if (outputSpeed < 0.40 && outputSpeed > 0) {
      outputSpeed = 0.40;
    } else if (outputSpeed > -0.40 && outputSpeed < 0) {
      outputSpeed = -0.40;
    }

    sbSpeed.setDouble(outputSpeed);
    sbError.setDouble(error);
    drivetrain.driveArcade(outputSpeed, 0, AutonomousConstants.kAutoSpeedFactor, DriveConstants.kDefaultTurnFactor);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Robot is allowed to be within one inch of the setpoint
    return (error < DriveConstants.kDistErrorMargin && error > -DriveConstants.kDistErrorMargin);
  }
}
