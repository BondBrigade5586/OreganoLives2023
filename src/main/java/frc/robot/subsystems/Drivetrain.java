
/* ------ SEASON BOT DRIVETRAIN CODE ------ */

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {
  
  private final WPI_TalonFX m_frontLeft;
  private final WPI_TalonFX m_frontRight;
  private final WPI_TalonFX m_backLeft;
  private final WPI_TalonFX m_backRight;

  private final MotorControllerGroup m_leftMotors;
  private final MotorControllerGroup m_rightMotors;

  private static NeutralMode kNeutralMode = NeutralMode.Coast; // Keeps track of neutral mode throughout program

  private final DifferentialDrive tankDrive;
  /** Creates a new Drivetrain. */
  public Drivetrain() {
    m_frontLeft = new WPI_TalonFX(DriveConstants.kFrontLeftID);
    m_frontRight = new WPI_TalonFX(DriveConstants.kFrontRightID);
    m_backLeft = new WPI_TalonFX(DriveConstants.kBackLeftID);
    m_backRight = new WPI_TalonFX(DriveConstants.kBackRightID);

    // Encoder
    m_frontLeft.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    m_leftMotors = new MotorControllerGroup(m_frontLeft, m_backLeft);
    m_rightMotors = new MotorControllerGroup(m_frontRight, m_backRight);
    m_leftMotors.setInverted(true);

    tankDrive = new DifferentialDrive(m_leftMotors, m_rightMotors);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


/* ------ PROGRAMMING BOT DRIVETRAIN CODE ------ */

// // // // // Copyright (c) FIRST and other WPILib contributors.
// // // // // Open Source Software; you can modify and/or share it under the terms of
// // // // // the WPILib BSD license file in the root directory of this project.

// // // // package frc.robot.subsystems;

// // // // import com.ctre.phoenix.motorcontrol.NeutralMode;
// // // // import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// // // // import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

// // // // import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// // // // import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// // // // import edu.wpi.first.wpilibj2.command.SubsystemBase;
// // // // import frc.robot.Constants.DriveConstants;

// // // // public class Drivetrain extends SubsystemBase {
  
// // // //   private final WPI_TalonSRX m_frontLeft;
// // // //   private final WPI_TalonSRX m_frontRight;
// // // //   private final WPI_VictorSPX m_backLeft;
// // // //   private final WPI_VictorSPX m_backRight;

// // // //   private static NeutralMode kNeutralMode = NeutralMode.Coast;
// // // //   private final DifferentialDrive tankDrive;

// // // //   /** Creates a new Drivetrain. */
// // // //   public Drivetrain() {
// // // //     m_frontLeft = new WPI_TalonSRX(DriveConstants.kFrontLeftID);
// // // //     m_frontRight = new WPI_TalonSRX(DriveConstants.kFrontRightID);
// // // //     m_backLeft = new WPI_VictorSPX(DriveConstants.kBackLeftID);
// // // //     m_backRight = new WPI_VictorSPX(DriveConstants. kBackRightID);
  
// // // //     m_frontRight.setInverted(true);
// // // //     m_backRight.setInverted(true);
// // // //     m_backLeft.follow(m_frontLeft);
// // // //     m_backRight.follow(m_frontRight);

// // // //     tankDrive = new DifferentialDrive(m_frontLeft, m_frontRight);
// // // //   }

// // // //   @Override
// // // //   public void periodic() {
// // // //     // This method will be called once per scheduler run
// // // //   }


  /* ------- CUSTOM DRIVETRAIN METHODS ------ */

  public void driveRobot(double forward, double turn, double speedFactor, double turnFactor) {
    tankDrive.arcadeDrive(-forward*speedFactor, -turn*turnFactor);
    SmartDashboard.putNumber("Drive Speed", -forward*speedFactor);
  }
  public void turnRobot(double turn) {
    tankDrive.arcadeDrive(0, -turn*0.85);
    SmartDashboard.putNumber("Turn Speed", turn);
  }

  public void enableBrakes() {
    kNeutralMode = NeutralMode.Brake;

    // TODO Test brake mode - how well does it stop the robot from moving at an angle? Can use ramp at KHS to test.
    m_frontLeft.setNeutralMode(NeutralMode.Brake);
    m_frontRight.setNeutralMode(NeutralMode.Brake);
    m_backLeft.setNeutralMode(NeutralMode.Brake);
    m_backRight.setNeutralMode(NeutralMode.Brake);
  }
  public void disableBrakes() {
    kNeutralMode = NeutralMode.Coast;

    m_frontLeft.setNeutralMode(NeutralMode.Coast);
    m_frontRight.setNeutralMode(NeutralMode.Coast);
    m_backLeft.setNeutralMode(NeutralMode.Coast);
    m_backRight.setNeutralMode(NeutralMode.Coast);
  }
  public void switchBrakeMode() {
    if (kNeutralMode == NeutralMode.Coast) {
      enableBrakes();
    } else if (kNeutralMode == NeutralMode.Brake) {
      disableBrakes();
    }
  }
  public NeutralMode getNeutralMode() {
    return kNeutralMode;
  }

  public double getEncoderPosition() {
    return m_frontLeft.getSelectedSensorPosition();
  }
  public void setEncoderPosition(double setpoint) {
    m_frontLeft.setSelectedSensorPosition(setpoint);
  }
  public void resetEncoderPosition() {
    setEncoderPosition(0);
  }

  public double getDriveSpeed() {
    return SmartDashboard.getNumber("Drive Speed", 0);
  }
  public double getTurnSpeed() {
    return SmartDashboard.getNumber("Turn Speed", 0);
  }
}