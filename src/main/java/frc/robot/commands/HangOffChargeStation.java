// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.EngageProximity;

public class HangOffChargeStation extends CommandBase {
  GenericEntry sbSensorStatus = RobotContainer.sbProxSensorStatus;

  boolean offEdge;
  EngageProximity proximitySensor = RobotContainer.m_proximity;
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  /** Creates a new HangOffChargeStation. */
  public HangOffChargeStation() {
    addRequirements(proximitySensor);
    // addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    offEdge = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    offEdge = proximitySensor.getSensorStatus();
    
    if (offEdge) {
      drivetrain.enableBrakes();
      drivetrain.stopRobot();
    } else {
      drivetrain.driveArcade(0.70, 0, DriveConstants.kSecondarySpeedFactor, DriveConstants.kSecondaryTurnFactor);
    }

    sbSensorStatus.setBoolean(offEdge);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return offEdge;
  }
}
