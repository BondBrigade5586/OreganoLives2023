// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.AlignGyro;
import frc.robot.commands.DriveForza;
import frc.robot.commands.HangOffChargeStation;
import frc.robot.commands.HoldOnChargeStation;
import frc.robot.commands.IntakeDown;
import frc.robot.commands.IntakeUp;
import frc.robot.commands.UseIntake;
import frc.robot.subsystems.*;
import frc.robot.Constants.*;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
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
  Runnable enableCubeProcessing = new EnableCubeProcessor();
  Runnable enableAT1Processing = new EnableAprilTag1Processor();
  Runnable enableAT2Processing = new EnableAprilTag2Processor();
  
  // Declare subsystems
  public final static Drivetrain m_drivetrain = new Drivetrain();
  public final static IntakePiece m_intake = new IntakePiece();
  public final static IntakeUnit m_intakeunit = new IntakeUnit();
  public final static Vision m_vision = new Vision();
  public final static Gyro m_gyro = new Gyro();
  public final static EngageProximity m_proximity = new EngageProximity();
  // // // public final static LED m_led = new LED();
  
  // Create and populate debug tab
  public static ShuffleboardTab debugTab = Shuffleboard.getTab("Debug");
  public static GenericEntry sbArea = debugTab.add("Area", 0).getEntry();
  public static GenericEntry sbAreaError = debugTab.add("Area Error", 0).getEntry();
  public static GenericEntry sbDriveSpeed = debugTab.add("Drive Setpoint", 0).getEntry();
  public static GenericEntry sbTurnSpeed = debugTab.add("Turn Speed", 0).getEntry();
  public static GenericEntry sbProxSensorStatus = debugTab.add("Hang Sensor Status", false).getEntry();
  public static GenericEntry sbIntakeStatus = debugTab.add("Intake Up", false).getEntry();
  public static GenericEntry sbIntakeStatusD = debugTab.add("Intake Down", false).getEntry();
  public static GenericEntry sbGyroX = debugTab.add("Gyro X", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -90, "max", 90)).getEntry();
  public static GenericEntry sbGyroY = debugTab.add("Gyro Y", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -90, "max", 90)).getEntry();
  public static GenericEntry sbGyroZ = debugTab.add("Gyro Z", 0).withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", -180, "max", 180)).getEntry();

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

  // Limit Switches
  public final static DigitalInput m_intakeDownLimitSwitch = new DigitalInput(IntakeConstants.kSwitchDownPort);
  public final static DigitalInput m_intakeUpLimitSwitch = new DigitalInput(IntakeConstants.kSwitchUpPort);

  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {   

    // Set default commands for subsystems
    m_drivetrain.setDefaultCommand(
      new DriveForza(
        () -> m_xboxDriverController.getRightTriggerAxis(),
        () -> m_xboxDriverController.getLeftTriggerAxis(),
        () -> m_xboxDriverController.getLeftX() * 0.65,
        () -> m_xboxDriverController.getXButton()
      )
    );
    m_intake.setDefaultCommand(
      new UseIntake(
        () -> m_subsystemController.getRawButton(IntakeConstants.kIntakeInButton), 
        () -> m_subsystemController.getRawButton(IntakeConstants.kIntakeOutButton))
    );
    // // // m_led.setDefaultCommand(
    // // //   new LEDControl()
    // // // );

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
    
    // Bind commands to buttons on p1 controller
    new POVButton(m_xboxDriverController,270).whileTrue(new AlignGyro(() -> (m_gyro.getZRotation()-90)));
    new POVButton(m_xboxDriverController,90).whileTrue(new AlignGyro(() -> (m_gyro.getZRotation()+90)));
    new JoystickButton(m_xboxDriverController, 4).whileTrue(new HangOffChargeStation());
    
    // Bind commands to buttons on p2 controller
    new JoystickButton(m_subsystemController, 2).whileTrue(new HoldOnChargeStation(15));
    new JoystickButton(m_subsystemController, 1).onTrue(new IntakeDown());
    new JoystickButton(m_subsystemController, 1).onFalse(new IntakeUp());
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
  class EnableCubeProcessor implements Runnable {
    public void run() {
      RobotContainer.m_vision.enableCubeProcessor();
    }
  }
  class EnableAprilTag1Processor implements Runnable {
    public void run() {
      RobotContainer.m_vision.enableAprilTag1Processor();
    }
  }
  class EnableAprilTag2Processor implements Runnable {
    public void run() {
      RobotContainer.m_vision.enableAprilTag2Processor();
    }
  }
}