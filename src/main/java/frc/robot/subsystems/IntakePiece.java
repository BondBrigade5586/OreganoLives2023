// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakePiece extends SubsystemBase {
  WPI_VictorSPX m_leftIntake;
  WPI_VictorSPX m_rightIntake;


  private DigitalInput cubeSensor;
  /** Creates a new Intake. */
  public IntakePiece() {
    m_leftIntake = new WPI_VictorSPX(IntakeConstants.kLeftControllerID);
    m_rightIntake = new WPI_VictorSPX(IntakeConstants.kRightControllerID);

    m_leftIntake.setInverted(false);
    m_rightIntake.setInverted(true);
    
    cubeSensor = new DigitalInput(IntakeConstants.kCubeProxSensorPort);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Piece In Intake", pieceInIntake());
    // This method will be called once per scheduler run
  }

  public void use(double setpoint) {
    m_leftIntake.set(setpoint);
    m_rightIntake.set(setpoint);
  }
  public void twist(double setpoint) {
    m_leftIntake.set(setpoint);
    m_rightIntake.set(-setpoint);
  }
  
  

  public void stop() {
    m_leftIntake.set(0);
    m_rightIntake.set(0);
  }
 

  public boolean pieceInIntake() {
    return !cubeSensor.get();
  }
}
