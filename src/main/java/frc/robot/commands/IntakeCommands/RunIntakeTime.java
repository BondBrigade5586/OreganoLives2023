// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IntakeCommands;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.Intake;

public class RunIntakeTime extends CommandBase {
  Intake intake = RobotContainer.m_intake;
  
  Timer timer = new Timer();

  double endTime;
  boolean directionIn;
  /** Creates a new RunIntakeTime. */
  public RunIntakeTime(double runTime, boolean in) {
    this.endTime = runTime;
    this.directionIn = in;
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
    if (directionIn) {
      intake.use(IntakeConstants.kIntakeInSP);
      RobotContainer.driverStationTab.add("Intake Speed", IntakeConstants.kIntakeInSP);
    } else {
      intake.use(IntakeConstants.kIntakeOutSP);
      RobotContainer.driverStationTab.add("Intake Speed", IntakeConstants.kIntakeOutSP);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stop();
    RobotContainer.driverStationTab.add("Intake Speed", 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() > endTime;
  }
}
