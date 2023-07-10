
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
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {
  private double prevForwardSpd = 0;

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
    m_frontRight.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    m_leftMotors = new MotorControllerGroup(m_frontLeft, m_backLeft);
    m_rightMotors = new MotorControllerGroup(m_frontRight, m_backRight);
    m_leftMotors.setInverted(true);

    tankDrive = new DifferentialDrive(m_leftMotors, m_rightMotors);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /* ------- CUSTOM DRIVETRAIN METHODS ------ */

  private double getCurrentForwardSpeed() {
    return prevForwardSpd; // Returns the forward speed at the previous iteration
  }
  private double adjustSP(double targetSP) {
    double adjSP;
    double currentSP = getCurrentForwardSpeed();
    if (Math.abs(targetSP) < 0.25) {
      return 0;
    } else if (Math.abs(Math.abs(currentSP) - Math.abs(targetSP)) < DriveConstants.kMaxSpeedChange) {
      return targetSP; // Returns the target setpoint in order to prevent a setpoint out of bounds
    } else if (targetSP < currentSP) {
      adjSP = currentSP - DriveConstants.kMaxSpeedChange;
    } else {
      adjSP = currentSP+DriveConstants.kMaxSpeedChange;
    }
    return adjSP; // Returns the adjusted setpoint in order to prevent overacceleration
  }
  public void driveTank(double left, double right) {
    tankDrive.tankDrive(left, right);
  }
  public void driveArcade(double forward, double turn, double speedFactor, double turnFactor) {
    // double adjForward = adjustSP(forward);
    tankDrive.arcadeDrive(forward*speedFactor, -turn*turnFactor);
    // prevForwardSpd = adjForward;

    SmartDashboard.putNumber("Current Speed", prevForwardSpd);
    SmartDashboard.putNumber("Target Speed", forward);
  }
  public void turnRobot(double turn) {
    tankDrive.arcadeDrive(0, -turn*0.85);
    prevForwardSpd = 0;

  }
  public void stopRobot() {
    tankDrive.tankDrive(0, 0);
    prevForwardSpd = 0;

  }

  public void enableBrakes() {
    kNeutralMode = NeutralMode.Brake;

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
    return m_frontRight.getSelectedSensorPosition();
  }
  public void setEncoderPosition(double setpoint) {
    m_frontRight.setSelectedSensorPosition(setpoint);
  }
  public void resetEncoderPosition() {
    setEncoderPosition(0);
  }

  public boolean isShutDown(WPI_TalonFX motor) {
    return motor.isAlive();
  }

  public void sendTempsToSmartDash() {
    SmartDashboard.putNumber("FL Status", (((9/5)*m_frontLeft.getTemperature()) + 32));
    SmartDashboard.putNumber("FR Status", (((9/5)*m_frontRight.getTemperature()) + 32)); 
    SmartDashboard.putNumber("BL Status", (((9/5)*m_backLeft.getTemperature()) + 32));
    SmartDashboard.putNumber("BR Status", (((9/5)*m_backRight.getTemperature()) + 32));
  }
}