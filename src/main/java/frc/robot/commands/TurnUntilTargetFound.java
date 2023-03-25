// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class TurnUntilTargetFound extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Vision limelight = RobotContainer.m_vision;

  double turnSP;
  double areaSP;

  /** Creates a new TurnUntilTargetFound. */
  public TurnUntilTargetFound(double tSpeed, double areaEndSP) {
    this.turnSP = tSpeed;
    this.areaSP = areaEndSP;

    addRequirements(drivetrain);
    addRequirements(limelight);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (limelight.getArea() < areaSP) {
      drivetrain.turnRobot(turnSP);
    } else {
      drivetrain.stopRobot();
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
    return limelight.getArea() >= areaSP;
  }
}
