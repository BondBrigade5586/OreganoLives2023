// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.PneumaticConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Pneumatics;

public class MotorTempControl extends CommandBase {
  Pneumatics pneumatics = RobotContainer.m_pneumatics;
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  /** Creates a new PneumaticControl. */
  public MotorTempControl() {
    addRequirements(pneumatics);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    Double[] temps = {drivetrain.getFLTempF(), drivetrain.getFRTempF(), drivetrain.getBLTempF(), drivetrain.getBRTempF()};

    // TODO **** Uncomment after initial functionality testing ****
    // // if (findMax(temps) > PneumaticConstants.kOverheatThreshold) {
    // //   pneumatics.enable();
    // // } else {
    // //   pneumatics.disable();
    // // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    pneumatics.disable();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  public double findMax(Double[] numList) {
    double maxVal = 0;
    for (double x: numList) {
      if (x > maxVal) {
        maxVal = x;
      }
    }
    return maxVal;
  }
}
