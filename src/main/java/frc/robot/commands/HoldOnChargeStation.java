// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.VoltageConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;

public class HoldOnChargeStation extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Gyro gyro = RobotContainer.m_gyro;
  Timer timeEngaged;
  Boolean engaged = false;
  int endSP;
  double driveSP, adjDriveSP;

  /** Creates a new HoldOnChargeStation. */
  public HoldOnChargeStation(int endTime) {
    this.endSP = endTime;
    addRequirements(gyro);
    addRequirements(drivetrain);
    timeEngaged = new Timer();
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timeEngaged.start();
    resetStopTimer();
    engaged = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    /** TODO
     * Base drive speeds off of the voltage so that it is always constant
     * Ensure autonomous will always engage, regardless of battery power 
     */
    if (gyro.getYRotation() > 6.5) {
      driveSP = 0.3335;
    } else if (gyro.getYRotation() < -6.5) {
      driveSP = -0.3335;
    } else if (gyro.getYRotation() > 5) {
      driveSP = 0.31;
      engaged = false;
      resetStopTimer();
   } else if (gyro.getYRotation() < -5) {
      driveSP = -0.31;
      resetStopTimer();
      engaged = false;
   } else if (gyro.getYRotation() < 5 && gyro.getYRotation() > -5 && !engaged) {
      driveSP = 0;
      drivetrain.stopRobot();
      timeEngaged.start();
      engaged = true;
   } else if (engaged) {
      driveSP = 0;
      SmartDashboard.putNumber("Time Engaged", timeEngaged.get());
      drivetrain.stopRobot();
   } else {
      engaged = false;
   }

   if (!engaged) {
    SmartDashboard.putNumber("Time Engaged", 0);
   }

   adjDriveSP = driveSP * (VoltageConstants.kHoldOnChargeStationTestV / RobotController.getBatteryVoltage());
   drivetrain.driveArcade(adjDriveSP, 0, 1.00, 0);
   SmartDashboard.putBoolean("Engaged", engaged);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putNumber("Time Engaged", 0);
    drivetrain.stopRobot();
    resetStopTimer();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (timeEngaged.get() > endSP);
  }

  public void resetStopTimer() {
    timeEngaged.stop();
    timeEngaged.reset();
  }
}
