// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.Intake;

public class IntakeUp extends CommandBase {
  GenericEntry sbIntakeStatus = RobotContainer.sbIntakeStatus;
  Intake intake = RobotContainer.m_intake;
  
  DigitalInput switchUp;

  /** Creates a new IntakeUp. */
  public IntakeUp() {
    switchUp = new DigitalInput(IntakeConstants.kSwitchUpPort);
    addRequirements(intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    sbIntakeStatus.setBoolean(switchUp.get());
    if (switchUp.get()) {
      intake.moveIntakeUnit(0);
    } else {
      intake.moveIntakeUnit(IntakeConstants.kIntakeUnitMoveSP);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopLift();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return switchUp.get();
  }
}
