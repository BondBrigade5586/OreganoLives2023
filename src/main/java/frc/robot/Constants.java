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
    public static final int kFlightstickDriverControllerPort = 0;
    public static final int kXboxDriverControllerPort = 1;
    public static final int kSubsystemControllerPort = 3;

    // Control Axis
    public static final int kDriveAxis = 1;
    public static final int kTurnAxis = 2;
    public static final int kSpeedFactorAxis = 3;
  }


  public static class AutonomousConstants {
    public static final double kDistCommunityToGrid = 188;
    public static final double kMaxYAngleOffset = 10;

    public static final double kAutoSpeedFactor = 0.80;
    public static final double kAutoTurnFactor = 0.90;
  }


  public static class DriveConstants {
    // Drive motor controller IDs
    public static final int kFrontLeftID = 1;
    public static final int kFrontRightID = 22;
    public static final int kBackLeftID = 3;
    public static final int kBackRightID = 44;

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
    public static final double kGyroP = 0.008;
    public static final double kGyroI = 0.000;
    public static final double kGyroD = 0;
    
    // Drive distance PID (used in DriveDistance)
    public static final double kDistanceP = 0.015;
    public static final double kDistErrorMargin = 0.50;

    public static final double kDefaultSpeedFactor = 0.80;
    public static final double kSecondarySpeedFactor = 0.40;

    public static final double kDefaultTurnFactor = 0.90;
    public static final double kSecondaryTurnFactor = 0.55;
  }


  public static class VisionConstants {
    public static final double kPTurn = 0.05;
    public static final double kITurn = 0.004;

    public static final double kTapeTargetArea = 1.25; // Target area of RR tape in limelight
    public static final double kTapeXOffset = 0; // Target X offset of tape in limelight
    public static final double kTapeP = 0.5;

    public static final double kCubeTargetArea = 7.65;
    public static final double kCubeXOffset = 0;
    public static final double kCubeP = 0.13;

    public static final double kAprilTagTargetArea = 5;
    public static final double kAprilTagXOffset = 0;
    public static final double kAprilTagP = 0;

    public static final double kBOffset = 2;
    public static final double kMaxXOffset = 25;
    public static final double kMaxZOffset = 6;
  }


  public static class IntakeConstants {
    public static final int kLeftControllerID = 4;
    public static final int kRightControllerID = 5;

    public static final int kIntakeInButton = 6;
    public static final int kIntakeOutButton = 5;

    public static final double kIntakePullInSP = -0.75;
    public static final double kIntakePushOutSP = 1.00;
  }

  public static class OtherConstants {
    public static final int kLEDPort = 0;
    public static final int kLEDCount = 150;
  }
}
