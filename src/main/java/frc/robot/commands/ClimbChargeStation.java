// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;
import frc.robot.Constants.AutonomousConstants;
import frc.robot.Constants.VoltageConstants;;

public class ClimbChargeStation extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Gyro gyro = RobotContainer.m_gyro;
  boolean interactedWithRamp, engaged;
  double driveSP, adjDriveSP;

  /** Creates a new AutoBalance. */
  public ClimbChargeStation() {
    addRequirements(drivetrain);
    addRequirements(gyro);
    interactedWithRamp = false;
    engaged = false;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    gyro.calibrate();
    gyro.resetZRotation();
    drivetrain.disableBrakes();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (gyro.getYRotation() > AutonomousConstants.kMaxYAngleOffset || gyro.getYRotation() < -AutonomousConstants.kMaxYAngleOffset) {
      interactedWithRamp = true;
    }



    if (!interactedWithRamp) {
      driveSP = 0.740625;
      // drivetrain.driveArcade(0.740625, 0, 0.75, 0);
    } else if (interactedWithRamp) {
      if (gyro.getYRotation() < -8) {
        driveSP = -0.525;
      } else if (gyro.getYRotation() > 8) {
        driveSP = 0.525;
      } else {
        driveSP = 0;
        engaged = true;
        drivetrain.enableBrakes();
      }
      SmartDashboard.putBoolean("Engaged", engaged);
    }
    
    adjDriveSP = driveSP * (VoltageConstants.kClimbChargeStationTestV / RobotController.getBatteryVoltage());
    drivetrain.driveArcade(adjDriveSP, 0, 0.75, 0);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();
    engaged = false;
    interactedWithRamp = false;
  }
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Ends if within 3 degrees of flat & is on the charge station
    return gyro.getYRotation() < AutonomousConstants.kMaxYAngleOffset && gyro.getYRotation() > -AutonomousConstants.kMaxYAngleOffset && engaged;
  }
}