// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.BalanceCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Gyro;

public class HoldOnChargeStation extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Gyro gyro = RobotContainer.m_gyro;
  /** Creates a new HoldOnChargeStation. */
  public HoldOnChargeStation() {
    addRequirements(gyro);
    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   if (gyro.getZRotation() > 14) {
    drivetrain.driveArcade(0.30, 0, 0.75, 0);
   } 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopRobot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
