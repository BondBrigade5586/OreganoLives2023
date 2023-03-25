// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.AutonomousConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class FollowTarget extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Vision limelight = RobotContainer.m_vision;

  double prevTimestamp = Timer.getFPGATimestamp();

  double x;
  double y;
  double area;
  double skew;
  double driveError;
  double turnError;
  double turnErrorSum;

  double driveSP;
  double turnSP;
  double kPDrive;
  double kPTurn;

  GenericEntry sbArea = RobotContainer.sbArea;
  GenericEntry sbAreaError = RobotContainer.sbAreaError;
  GenericEntry sbDriveSpeed = RobotContainer.sbDriveSpeed;
  GenericEntry sbTurnSpeed = RobotContainer.sbTurnSpeed;

  /** Creates a new FollowTape. */
  public FollowTarget(double dsp, double tsp, double pDrive, double pTurn) {
    this.driveSP = dsp;
    this.turnSP = tsp;
    this.kPDrive = pDrive;
    this.kPTurn = pTurn;
    addRequirements(drivetrain);
    addRequirements(limelight);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Decides which limelight pipeline to use based on target area (each target has a unique target area)
    if (driveSP == VisionConstants.kTapeTargetArea) {
      limelight.enableTapeProcessor();
    } else if (driveSP == VisionConstants.kCubeTargetArea) {
      limelight.enableCubeProcessor();
    } else if (driveSP == VisionConstants.kAprilTagTargetArea) {
      limelight.enableAprilTagProcessor();
    }

    drivetrain.enableBrakes();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    x = limelight.getXOffset();
    y = limelight.getYOffset();
    area = limelight.getArea();

    // Calculate error margins
    driveError = driveSP - area;
    turnError =  turnSP - x;
    turnErrorSum = turnError*(Timer.getFPGATimestamp() - prevTimestamp);
    
    // Prevents robot from moving if there is no tape
    if (area == 0) {
      driveError = 0;
      turnError = 0;
    }

    if (driveError < 2.5) {
      driveError = 2.5;
    } else if (driveError > 9.5) {
      driveError = 9.5;
    }
    // Adjusts motor outputs based on drive and turn errors
    drivetrain.driveArcade(kPDrive * driveError, kPTurn * -turnError + VisionConstants.kITurn*-turnErrorSum, AutonomousConstants.kAutoSpeedFactor, AutonomousConstants.kAutoTurnFactor);
    prevTimestamp = Timer.getFPGATimestamp();

    sbArea.setDouble(area);
    sbAreaError.setDouble(driveError);
    sbDriveSpeed.setDouble(driveError*kPDrive);
    sbTurnSpeed.setDouble(kPTurn * -turnError + VisionConstants.kITurn*-turnErrorSum);

  } 

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    limelight.enableDriverCamera();
    drivetrain.stopRobot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Ends if area is within 5% of the target area
    return area > (driveSP-(driveSP*0.05)) && area < (driveSP+(driveSP*0.05));
  }
}
