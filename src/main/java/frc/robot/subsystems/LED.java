// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LED extends SubsystemBase {
  AddressableLED m_led;
  AddressableLEDBuffer m_ledBuffer;

  /** Creates a new LED. */
  public LED() {
    m_led = new AddressableLED(Constants.OtherConstants.kLEDPort); // PWM Port
    m_ledBuffer = new AddressableLEDBuffer(Constants.OtherConstants.kLEDCount); // Number of LEDs on strip
    m_led.setLength(m_ledBuffer.getLength()); // Sets length of LED strip
    m_led.setData(m_ledBuffer);
    m_led.start();
  }

  // Set color with HSV 
  public void setColorHSV(int index, int h, int s, int v) {
    m_ledBuffer.setHSV(index, h, s, v);
  }
  // Set color with RGB
  public void setColorRGB(int index, int r, int g, int b) {
    m_ledBuffer.setRGB(index, r, g, b);
  }

  // Get LED count
  public int getLength() {
    return m_ledBuffer.getLength();
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
