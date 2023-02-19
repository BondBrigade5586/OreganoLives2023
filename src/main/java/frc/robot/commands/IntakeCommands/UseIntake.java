// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IntakeCommands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.Intake;

public class UseIntake extends CommandBase {
  Intake intake = RobotContainer.m_intake;
  double intakeSP;
  Supplier<Boolean> intakeIn, intakeOut;
  /** Creates a new UseIntake. */
  public UseIntake(Supplier<Boolean> in, Supplier<Boolean> out) {
    this.intakeIn = in;
    this.intakeOut = out;

    addRequirements(intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (intakeIn.get() && intakeOut.get()) {
      intakeSP = 0;
    } else if (intakeIn.get()) {
      intakeSP = IntakeConstants.kIntakeInSP;
    } else if (intakeOut.get()) {
      intakeSP = IntakeConstants.kIntakeOutSP;
    } else { 
      intakeSP = 0;
    }
    intake.use(intakeSP);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
