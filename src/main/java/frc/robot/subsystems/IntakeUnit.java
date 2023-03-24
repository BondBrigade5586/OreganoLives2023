// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeUnit extends SubsystemBase {
  WPI_VictorSPX m_intakeHeight;
  /** Creates a new IntakeUnit. */
  public IntakeUnit() {
    m_intakeHeight = new WPI_VictorSPX(IntakeConstants.kUnitControllerID);

  }

  public void moveIntake(double sp) {
    m_intakeHeight.set(sp);
  }
  public void stop() {
    m_intakeHeight.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
