// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  WPI_VictorSPX m_leftIntake;
  WPI_VictorSPX m_rightIntake;
  /** Creates a new Intake. */
  public Intake() {
    m_leftIntake = new WPI_VictorSPX(IntakeConstants.kLeftControllerID);
    m_rightIntake = new WPI_VictorSPX(IntakeConstants.kRightControllerID);

    m_leftIntake.setInverted(false);
    m_rightIntake.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void use(double setpoint) {
    m_leftIntake.set(setpoint);
    m_rightIntake.set(setpoint);
    if (setpoint > 0) {
      RobotContainer.sbIntakeDir.setString("In");
    } else if (setpoint < 0) {
      RobotContainer.sbIntakeDir.setString("Out");
    } else {
      RobotContainer.sbIntakeDir.setString("None");
    }
    
  }
  public void stop() {
    m_leftIntake.set(0);
    m_rightIntake.set(0);
    RobotContainer.sbIntakeDir.setString("None");
  }
}
