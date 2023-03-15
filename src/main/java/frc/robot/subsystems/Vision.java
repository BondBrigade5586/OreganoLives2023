// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.VisionConstants;

public class Vision extends SubsystemBase {
  Servo limelightAngleControl;
  GenericEntry sbLimelightAngle;

  NetworkTable table;
  NetworkTableEntry tx;
  NetworkTableEntry ty;
  NetworkTableEntry ta;
  NetworkTableEntry ts;
  NetworkTableEntry pipeline;

  double x;
  double y;
  double area;
  double skew;
  /** Creates a new Vision. */
  public Vision() {
    limelightAngleControl = new Servo(VisionConstants.kLimelightAngleControlPWMPort);
    sbLimelightAngle = RobotContainer.sbLimelightAngle;
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx"); // Horizontal Offset From Crosshair To Target  (-27 degrees to 27 degrees)
    ty = table.getEntry("ty"); // Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
    ta = table.getEntry("ta"); // Target Area (0% of image to 100% of image)
    ts = table.getEntry("ts"); // Target Skew
    pipeline = table.getEntry("pipeline"); // Gets camera mode

    x = tx.getDouble(0);
    y = ty.getDouble(0);
    area = ta.getDouble(0);
  }

  public void update() {
    x = tx.getDouble(0);
    y = ty.getDouble(0);
    area = ta.getDouble(0);
    skew = ts.getDouble(0);
  }

  public double getXOffset() {
    update();
    return x;
  }
  public double getYOffset() {
    update();
    return y;
  }
  public double getArea() {
    update();
    return area;
  }
  
  public void enableDriverCamera() {
    pipeline.setNumber(0);
  }
  public void enableTapeProcessor() {
    pipeline.setNumber(1);
  }
  public void enableCubeProcessor() {
    pipeline.setNumber(2);
  }
  public void enableAprilTagProcessor() {
    pipeline.setNumber(3);
  }

  public double getLimelightAngle() {
    return limelightAngleControl.getAngle();
  }
  public void setLimelightAngle(double angle) {
    limelightAngleControl.setAngle(angle);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
