// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PneumaticConstants;

public class Pneumatics extends SubsystemBase {
  Compressor compressor;
  Solenoid masterSolenoid;

  /** Creates a new Pneumatics. */
  public Pneumatics() {
    compressor = new Compressor(PneumaticConstants.controlModuleType);
    masterSolenoid = new Solenoid(PneumaticConstants.controlModuleType, PneumaticConstants.kSolenoidChannel);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Compressor Pressure", compressor.getPressure());
  }


  public void enableSolenoid() {
    masterSolenoid.set(true);
  }
  public void disableSolenoid() {
    masterSolenoid.set(false);
  }

  public void enableCompressor() {
    compressor.enableDigital();
  }
  public void disableCompressor() {
    compressor.disable();
  }
}
