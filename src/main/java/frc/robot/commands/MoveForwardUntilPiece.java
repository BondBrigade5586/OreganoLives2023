// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.AutonomousConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakePiece;
import frc.robot.subsystems.Vision;

public class MoveForwardUntilPiece extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Vision limelight = RobotContainer.m_vision;
  IntakePiece intake = RobotContainer.m_intake;

  double driveSP;
  double turnSP;

  double kTurnP;
  double kDriveP;
  double kTargetArea;

  double x;
  double kAreaError;

  /** Creates a new MoveForwardUntilPiece. */
  public MoveForwardUntilPiece(double driveP, double turnP, double targetArea) {
    this.kDriveP = driveP;
    this.kTurnP = turnP;
    this.kTargetArea = targetArea;

    addRequirements(drivetrain);
    addRequirements(intake);
    addRequirements(limelight);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    x = limelight.getXOffset();
    kAreaError = kTargetArea - limelight.getArea();
    
    // Set drive and turn speeds
    turnSP = x*kTurnP;
    driveSP = kAreaError * kDriveP;

    if (driveSP < AutonomousConstants.kInchingDriveSpeed) {
      driveSP = AutonomousConstants.kInchingDriveSpeed;
    }
    // Determine if piece is in intake or not
    if (!intake.pieceInIntake()) {
      intake.use(0);
    } else {
      intake.use(AutonomousConstants.kAutoIntakeInSP);
    }

    // Drive & turn robot
    drivetrain.driveArcade(driveSP, turnSP, DriveConstants.kDefaultSpeedFactor, DriveConstants.kDefaultTurnFactor);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();
    intake.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !intake.pieceInIntake();
  }
}
