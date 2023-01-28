// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;

public class AutoBalance extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Gyro gyro = RobotContainer.m_gyro;
  /** Creates a new AutoBalance. */
  public AutoBalance() {
    addRequirements(drivetrain);
    addRequirements(gyro);
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

    /* TODO Use Limelight and AprilTag image in the center of the Coopertition grid to center and find the distance to the wall
     * Then, use this position to determine how close until robot is on top of the charge station
    */
    SmartDashboard.putNumber("Gyro Y Rotation", gyro.getYRotation());  
    SmartDashboard.putString("AUTONOMOUS", "BALANCING");
      
    if (gyro.getYRotation() < -7) {
      drivetrain.driveRobot(-0.65, 0, 0.65, 0);
    } else if (gyro.getYRotation() > 7) {
      drivetrain.driveRobot(0.65, 0, 0.65, 0);
    } else {
      drivetrain.enableBrakes();
      drivetrain.driveRobot(0, 0, 0, 0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
    // return (drivetrain.getNeutralMode() == NeutralMode.Brake); // Ends if brake mode is enabled
  }
}