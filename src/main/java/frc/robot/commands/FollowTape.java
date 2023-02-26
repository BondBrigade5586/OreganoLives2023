// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
// import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class FollowTape extends CommandBase {
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
  /** Creates a new FollowTape. */
  public FollowTape(double dsp, double tsp) {
    driveSP = dsp;
    turnSP = tsp;
    addRequirements(drivetrain);
    addRequirements(limelight);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.enableBrakes();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SmartDashboard.putString("AUTONOMOUS", "FOLLOWING TAPE");

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
    // Adjusts motor outputs based on drive and turn errors
    drivetrain.driveArcade(VisionConstants.kPCharge * driveError, VisionConstants.kPTurn * -turnError + VisionConstants.kITurn*-turnErrorSum, 1.00, 1.00);
    prevTimestamp = Timer.getFPGATimestamp();
  } 

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();
    // Robot.m_drivetrainCommand.schedule();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return false;
    return ((drivetrain.getTurnSpeed() < 0.15 && drivetrain.getTurnSpeed() > -0.15 && drivetrain.getDriveSpeed() < 0.30 && drivetrain.getDriveSpeed() > -0.30) || area == 0);
  }
}
