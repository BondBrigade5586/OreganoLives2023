// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.DriveCommands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class ForzaDrive extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Vision limelight = RobotContainer.m_vision;

  Supplier<Double> kForward, kBackward, kTurn;
  double kSpeed;

  /** Creates a new ForzaDrive. */
  public ForzaDrive(Supplier<Double> forward, Supplier<Double> backward, Supplier<Double> turn) {
    this.kForward = forward;
    this.kBackward = backward;
    this.kTurn = turn;
    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    kSpeed = kForward.get() - kBackward.get();

    drivetrain.driveArcade(kSpeed, kTurn.get(), DriveConstants.kDefaultSpeedFactor, DriveConstants.kDefaultTurnFactor);
    limelight.update();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
