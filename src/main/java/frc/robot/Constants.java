// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

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
    public static final double kDistOutsideCommunityCenter = 182;
    public static final double kDistOutsideCommunitySide = 148;
    public static final double kMaxYAngleOffset = 13.00;

    public static final double kAutoSpeedFactor = 0.80;
    public static final double kAutoTurnFactor = 0.90;

    public static final double kAutoIntakeInSP = -0.50;
    public static final double kAutoIntakeOutSP = 1.00;

    public static final double kDefaultDriveSpeed = 0.625;
    public static final double kDefaultTurnSpeed = 0.65;

    public static final double kInchingDriveSpeed = 0.40;
    public static final double kInchingTurnSpeed = 0.4125;
  }


  public static class DriveConstants {
    // Drive motor controller IDs
    public static final int kFrontLeftID = 1;
    public static final int kFrontRightID = 26;
    public static final int kBackLeftID = 3;
    public static final int kBackRightID = 48;

    public static final double kGyroSPAngle = 0; // Robot should be powered on facing the wall/driver stations
    
    // Encoder constants
    public static final double kGearRatio = 12.0 / 72.0; // Teeth on gear attached to motor (12) divided by teeth attached to wheel (78); 
    public static final double kTicksPerRotation = 2048 / kGearRatio; // Enocder ticks per wheel rotation
    public static final double kWheelDiameter = 5.75; // Wheel diameter in inches
    public static final double kTicksToInches = (Math.PI * kWheelDiameter) / kTicksPerRotation ; // Wheel circumference divided by ticks in each rotation
    
    // Converting between measurement systems
    public static final double kInchesToFt = 1.0 / 12.0;
    public static final double kFeetToM = 1.0 / 3.28084;
    public static final double kFeetToMiles = 1.0 / 5208.0;
    public static final double kFPStoMPH = 225.0 / 330.0;
    public static final double kMPHtoFPS = 330.0 / 225.0;

    // Gyroscope PID (used in AlignGyro)
    public static final double kGyroP = 0.008;
    public static final double kGyroI = 0.000;
    public static final double kGyroD = 0;
    
    // Drive distance PID (used in DriveDistance)
    public static final double kDistanceP = 0.017;
    public static final double kDistErrorMargin = 2.5;

    public static final double kDefaultSpeedFactor = 0.90;
    public static final double kSecondarySpeedFactor = 0.50 * kDefaultSpeedFactor;
    public static final double kMaxSpeedChange = 0.03;

    public static final double kDefaultTurnFactor = 0.90;
    public static final double kSecondaryTurnFactor = 0.60;
  }

  public static class VisionConstants {
    public static final double kPTurn = 0.05;
    public static final double kITurn = 0.004;

    public static final double kTapeTargetArea = 1.15; // Target area of RR tape in limelight
    public static final double kTapeXOffset = 0; // Target X offset of tape in limelight
    public static final double kTapePDrive = 0.515;
    public static final double kTapePTurn = 0.05;

    public static final double kCube1SideMinArea = 1.25;
    public static final double kCube2SideMinArea = 1.00;

    public static final double kCubeCenterMinArea = 2.35;
    public static final double kCubeTargetArea = 11.75;
    public static final double kCubeXOffset = 0;
    public static final double kCubePDrive = 0.045;
    public static final double kCubePTurn = 0.03675;

    public static final double kEngageAprilTagTargetArea = 1;
    public static final double kEngageAprilTagXOffset = 0;
    public static final double kEngageAprilTagPDrive = 0.35;
    public static final double kEngageAprilTagPTurn = 0.045;

    // AprilTag constants for the red side without wires -- inverted for blue
    public static final double kS1AprilTagNonwiredMinArea = 0.10;
    public static final double kS1AprilTagNonwiredTargetArea = 4;
    public static final double kS1AprilTagNonwiredXOffset = 2.10;
    public static final double kS1AprilTagNonwiredPDrive = 0.25;
    public static final double kS1AprilTagNonwiredPTurn = 0.054;

    public static final double kS2AprilTagNonwiredTargetArea = 5.00;
    public static final double kS2AprilTagNonwiredXOffset = -0.40;
    public static final double kS2AprilTagNonwiredPDrive = 0.21;
    public static final double kS2AprilTagNonwiredPTurn = 0.057;

    // AprilTag constants for the red side with wires -- inverted for blue with wires
    public static final double kS1AprilTagWiredMinArea = 0.15;
    public static final double kS1AprilTagWiredTargetArea = 4;
    public static final double kS1AprilTagWiredXOffset = -4.25;
    public static final double kS1AprilTagWiredPDrive = 0.25;
    public static final double kS1AprilTagWiredPTurn = 0.06875;

    public static final double kS2AprilTagWiredTargetArea = 2.80;
    public static final double kS2AprilTagWiredXOffset = 0.40;
    public static final double kS2AprilTagWiredPDrive = 0.20;
    public static final double kS2AprilTagWiredPTurn = 0.07;

    public static final double kBOffset = 2;
    public static final double kMaxXOffset = 25;
    public static final double kMaxZOffset = 6;
  }


  public static class IntakeConstants {
    public static final int kLeftControllerID = 5;
    public static final int kRightControllerID = 6;
    public static final int kUnitControllerID = 7;

    public static final int kIntakeInButton = 6;
    public static final int kIntakeOutButton = 5;

    public static final int kSwitchUpPort = 7;
    public static final int kSwitchDownPort = 6;
    public static final int kCubeProxSensorPort = 8;
    
    public static final double kIntakeUnitMoveSP = 0.35;

    public static final double kIntakePullInSP = -0.75;
    public static final double kIntakePushOutSP = 1.00;
  }

  public static class PneumaticConstants {
    public static final PneumaticsModuleType controlModuleType = PneumaticsModuleType.CTREPCM;
    public static final int kSolenoidChannel = 0; // ID on PCM
    public static final int kOverheatThreshold = 115; // Temperature (F) at which the motors will be cooled // TODO Set correct val

  }

  public static class VoltageConstants {
    public static final double kClimbChargeStationTestV = 11.70;
    public static final double kHoldOnChargeStationTestV = 12.00;
    public static final double kAlignGyroAlignTargetTestV = 11.30;
  }
  public static class OtherConstants {
    public static final int kLEDPort = 1;
    public static final int kLEDCount = 34; // 111
    public static final int kLEDMidpointIndex = 75;
    
    public static final int kProximitySensorPort = 9;
  }
}
