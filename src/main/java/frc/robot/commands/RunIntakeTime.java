// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.AutonomousConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.IntakePiece;

public class RunIntakeTime extends CommandBase {
  IntakePiece intake = RobotContainer.m_intake;
  
  Timer timer = new Timer();

  double endTime;
  boolean directionOut;
  /** Creates a new RunIntakeTime. */
  public RunIntakeTime(double runTime, boolean out) {
    this.endTime = runTime;
    this.directionOut = out;
    addRequirements(intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (directionOut) {
      intake.use(AutonomousConstants.kAutoIntakeOutSP);
    } else {
      intake.use(AutonomousConstants.kAutoIntakeOutSP);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stop();
    // RobotContainer.driverStationTab.add("Intake Speed", 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() > endTime;
  }
}
