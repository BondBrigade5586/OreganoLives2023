// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.AutonomousConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.Intake;

public class PickUpPiece extends CommandBase {
  Intake intake = RobotContainer.m_intake;

  // TODO Test limit switch functionality that runs the intake until the limit switch has been activated 

  /** Creates a new RunIntakeUntilPiece. */
  public PickUpPiece() {
    addRequirements(intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (intake.getProximityStatus()) {
      intake.use(0);
    } else {
      intake.use(AutonomousConstants.kAutoIntakeInSP);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // TODO Lift intake immediately after piece is picked up
    intake.stopIntake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intake.getProximityStatus();
  }
}