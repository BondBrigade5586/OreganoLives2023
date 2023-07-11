// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PneumaticConstants;

public class Pneumatics extends SubsystemBase {
  Solenoid masterSolenoid;
  // // Solenoid flSolenoid;
  // // Solenoid frSolenoid;
  // // Solenoid blSolenoid;
  // // Solenoid brSolenoid;

  /** Creates a new Pneumatics. */
  public Pneumatics() {
    masterSolenoid = new Solenoid(PneumaticConstants.controlModuleType, PneumaticConstants.kMasterSolenoidID);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


  public void enable() {
    masterSolenoid.set(true);
  }
  public void disable() {
    masterSolenoid.set(false);
  }
  // // // Enable, disable, or enable for set duration of select solenoid(s)
  // // public void enable(Solenoid ... solenoids) {
  // //   for (Solenoid sol: solenoids) {
  // //     sol.set(true);
  // //   }
  // // }
  // // public void disable(Solenoid ... solenoids) {
  // //   for (Solenoid sol: solenoids) {
  // //     sol.set(false);
  // //   }  
  // // }

  // // // Enable, disable, or enable for set duration of all solenoids at once
  // // public void enableAll() {
  // //   enable(flSolenoid, frSolenoid, blSolenoid, brSolenoid);
  // // }
  // // public void disableAll() {
  // //   disable(flSolenoid, frSolenoid, blSolenoid, brSolenoid);
  // // }
}
