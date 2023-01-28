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
    public static final int kFrontLeftID = 1;
    public static final int kFrontRightID = 2;
    public static final int kBackLeftID = 3;
    public static final int kBackRightID = 4;

    public static final int kGyroSPAngle = 180;
    public static final double kGyroP = 0.006;
    public static final double kGyroI = 0.0006; // Tune
    public static final double kGyroD = 0;
    
    public static final double kDefaultTurnFactor = 0.80;
    public static final double kDefaultSpeedFactor = 0.50;
  }
  public static class VisionConstants {
    public static final double kPTurn = 0.04;
    public static final double kSetpointTurn = 0;

    public static final double kPCharge = 0.5;
    public static final double kSetpointCharge = 1.25; // Target area of RR tape in limelight
  }
  public static class OtherConstants {
    public static final int kLEDPort = 0;
    public static final int kLEDCount = 150;
  }
}
