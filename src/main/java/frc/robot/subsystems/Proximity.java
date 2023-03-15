// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.OtherConstants;

public class Proximity extends SubsystemBase {
  private DigitalInput chargeStationSensor;
  /** Creates a new Proximity. */
  public Proximity() {
    chargeStationSensor = new DigitalInput(OtherConstants.kProximitySensorPort);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public boolean getSensorStatus() {
    return chargeStationSensor.get();
  }
}
