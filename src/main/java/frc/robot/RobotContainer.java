// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.BalanceCommands.HoldOnChargeStation;
import frc.robot.commands.DriveCommands.ForzaDrive;
import frc.robot.commands.IntakeCommands.UseIntake;
import frc.robot.commands.VisionCommands.FollowTape;
import frc.robot.subsystems.*;
import frc.robot.Constants.*;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // Runnables for instant commands
  Runnable resetEncoder = new ResetEncoder();
  Runnable resetGyroZ = new ResetGyroZ();
  Runnable switchLimelightMode = new SwitchLimelightMode();

  // Create and populate driver station tab
  public static ShuffleboardTab driverStationTab = Shuffleboard.getTab("Driver Station");
  public static GenericEntry sbDriveSpd = driverStationTab.add("Drive Speed", 0).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", -1, "max", 1)).getEntry();
  public static GenericEntry sbTurnSpd = driverStationTab.add("Turn Speed", 0).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", -1, "max", 1)).getEntry();
  public static GenericEntry sbGyroY = driverStationTab.add("Gyro Y Rotation", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -90, "max", 90)).getEntry();
  public static GenericEntry sbGyroZ = driverStationTab.add("Gyro Z Rotation", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -180, "max", 180)).getEntry();
  public static GenericEntry sbIntakeDir = driverStationTab.add("Intake Direction", "None").getEntry();
  
  // Set up Drivetrain tab on Shuffleboard
  public static ShuffleboardTab driveTab = Shuffleboard.getTab("Drivetrain");
  public static GenericEntry sbInches = driveTab.add("Encoder inches", 0).getEntry();
  public static GenericEntry sbFeet = driveTab.add("Encoder feet", 0).getEntry();
  public static GenericEntry sbMiles = driveTab.add("Encoder miles", 0).getEntry();
  public static GenericEntry sbVelFt = driveTab.add("Robot FPS", 0).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", -20, "max", 20)).getEntry();
  public static GenericEntry sbVelMi = driveTab.add("Robot MPH", 0).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", -20, "max", 20)).getEntry();
  public static GenericEntry sbOutputSpeed = driveTab.add("Output Speed", 0).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", -3, "max", 3)).getEntry();
  public static GenericEntry sbDistError = driveTab.add("Distance Error", 0).getEntry();
  
  // Create controllers
  public final static Joystick m_flightstickDriverController = new Joystick(OperatorConstants.kFlightstickDriverControllerPort);
  public final static XboxController m_xboxDriverController = new XboxController(OperatorConstants.kXboxDriverControllerPort);
  public final static XboxController m_subsystemController = new XboxController(OperatorConstants.kSubsystemControllerPort);
  
  // Declare subsystems
  public final static Drivetrain m_drivetrain = new Drivetrain();
  public final static Intake m_intake = new Intake();
  public final static Vision m_vision = new Vision();
  public final static Gyro m_gyro = new Gyro();
  public final static LED m_led = new LED();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    // // // // Sets LEDs to blue when RobotContainer is called from the Robot class
    // // // for (int i=1;i<m_led.getLength();i++) {
    // // //   m_led.setColorRGB(i, 0, 0, 255);
    // // // }

    // Set default commands for subsystems
    m_drivetrain.setDefaultCommand(
      new ForzaDrive(() -> m_xboxDriverController.getRightTriggerAxis(), () -> m_xboxDriverController.getLeftTriggerAxis(), () -> m_xboxDriverController.getLeftX())
    );
    m_intake.setDefaultCommand(
      new UseIntake(
        () -> m_subsystemController.getRawButton(IntakeConstants.kIntakeInButton), 
        () -> m_subsystemController.getRawButton(IntakeConstants.kIntakeOutButton))
    );
    
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    // Bind commands to buttons on controller
    new JoystickButton(m_subsystemController, 3).whileTrue(new FollowTape(VisionConstants.kSetpointCharge, VisionConstants.kSetpointTurn));
    new JoystickButton(m_subsystemController, 2).whileTrue(new HoldOnChargeStation()); // TODO Should be button 'B'

    // // // // Bind commands to buttons on joystick for debug\

    // // // new JoystickButton(m_driverController, 1).whileTrue(new FollowTape(VisionConstants.kSetpointCharge, VisionConstants.kSetpointTurn)); // Requires fine-tuning
    // // // new JoystickButton(m_driverController, 3).whileTrue(new AlignGyro(() -> DriveConstants.kGyroSPAngle)); // Test use only
    // // // new JoystickButton(m_driverController, 5).whileTrue(new ClimbChargeStation()); // Test use only
    // // // new JoystickButton(m_driverController, 12).whileTrue(new DriveDistance(290)); // Drives robot to 16' 10.5"  
  
    // // // // Instant Commands
    // // // new JoystickButton(m_driverController, 4).whileTrue(new InstantCommand(switchLimelightMode, m_vision));
    // // // new JoystickButton(m_driverController, 5).whileTrue(new InstantCommand(resetGyroZ, m_gyro));
    // // // new JoystickButton(m_driverController, 6).whileTrue(new InstantCommand(resetEncoder, m_drivetrain));

    // // // // Sequential Command Groups
    // // // new JoystickButton(m_driverController, 11).whileTrue(
    // // //   new SequentialCommandGroup(
    // // //     new AlignGyro(() -> -m_gyro.getZRotation()),
    // // //     new DriveUntilTapeFound(-0.55, 0.05),
    // // //     new InlineTapeWall(),
    // // //     new AlignGyro(() -> 0.0),
    // // //     new FollowTape(VisionConstants.kSetpointCharge, VisionConstants.kSetpointTurn)
    // // //   )
    // // // );
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }


  // Runnable classes for inline InstantCommands -- Is this correct?
  class ResetEncoder implements Runnable {
    public void run() {
      m_drivetrain.resetEncoderPosition();
    }
  }
  class ResetGyroZ implements Runnable {
    public void run() {
      m_gyro.resetZRotation();
    }
  }
  class SwitchLimelightMode implements Runnable {
    public void run() {
      m_vision.switchCameraMode();
    }
  }
}