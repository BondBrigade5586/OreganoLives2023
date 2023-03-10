// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gyro extends SubsystemBase {
  
  private final AHRS gyro;
  /** Creates a new Gyro. */
  public Gyro() {
    gyro = new AHRS(Port.kMXP);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void calibrate() {
    gyro.calibrate();
  }
  public double getXRotation() {
    return gyro.getPitch();
  }
  public double getYRotation() {
    return gyro.getRoll();
  }
  public double getZRotation() {
    return gyro.getYaw();
  }
  public void resetZRotation() {
    gyro.reset();
  }
}
