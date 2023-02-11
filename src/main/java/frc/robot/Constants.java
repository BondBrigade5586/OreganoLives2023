// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */

public final class Constants {
  
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kSubsystemControllerPort = 1;

    // Control Axis
    public static final int kDriveAxis = 1;
    public static final int kTurnAxis = 2;
    public static final int kSpeedFactorAxis = 3;
  }

  public static class DriveConstants {
    // Drive motor controller IDs
    public static final int kFrontLeftID = 1;
    public static final int kFrontRightID = 2;
    public static final int kBackLeftID = 3;
    public static final int kBackRightID = 4;

    public static final double kGyroSPAngle = 0; // Robot should be powered on facing the wall/driver stations
    
    // Encoder constants
    public static final double kGearRatio = 12.0 / 72.0; // Teeth on gear attached to motor (12) divided by teeth attached to wheel (78); 
    public static final double kTicksPerRotation = 2048 / kGearRatio;
    public static final double kWheelDiameter = 5.75; // Wheel diameter in inches
    public static final double kTicksToInches = (Math.PI * kWheelDiameter) / kTicksPerRotation ; // Wheel circumference divided by ticks in each rotation
    
    // Converting between measurement systems
    public static final double kInchesToFt = 1.0 / 12.0;
    public static final double kFeetToM = 1.0 /3.28084;
    public static final double kFeetToMiles = 1.0 / 5208.0;
    public static final double kFPStoMPH = 225.0 / 330.0;
    public static final double kMPHtoFPS = 330.0 / 225.0;

    // Gyroscope PID (used in AlignGyro)
    public static final double kGyroP = 0.03;
    public static final double kGyroI = 0.000;
    public static final double kGyroD = 0;
    
    // Drive distance PID (used in DriveDistance)
    public static final double kDistanceP = 0.015;
    public static final double kDistErrorMargin = 0.25;

    public static final double kDefaultSpeedFactor = 0.80;
    public static final double kDefaultTurnFactor = 0.80;
  }

  public static class VisionConstants {
    public static final double kPTurn = 0.05;
    public static final double kITurn = 0.004;

    public static final double kPCharge = 0.5;
    public static final double kSetpointCharge = 1.25; // Target area of RR tape in limelight
    public static final double kSetpointTurn = 0; // Target X offset of tape in limelight

    public static final double kBOffset = 2;
    public static final double kMaxXOffset = 25;
    public static final double kMaxZOffset = 6;
  }

  public static class OtherConstants {
    public static final int kLEDPort = 0;
    public static final int kLEDCount = 150;
  }
}
