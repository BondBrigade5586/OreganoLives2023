// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

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
  // Initialize autonomous data
  ShuffleboardTab tab = Shuffleboard.getTab("Autonomous");
  GenericEntry autoMode = tab.add("Set autonomous mode", "Center").withWidget(BuiltInWidgets.kTextView)/*.withProperties(Map.of("min", 1, "max", 2))*/.getEntry();

  // Create controllers
  private final Joystick m_driverController = new Joystick(OperatorConstants.kDriverControllerPort);
  private final XboxController m_subsystemController = new XboxController(OperatorConstants.kSubsystemControllerPort);
  
  // Declare subsystems
  public final static Drivetrain m_drivetrain = new Drivetrain();
  public final static Vision m_vision = new Vision();
  public final static Gyro m_gyro = new Gyro();
  public final static LED m_led = new LED();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    // Sets LEDs to blue when RobotContainer is called from the Robot class
    for (int i=1;i<m_led.getLength();i++) {
      m_led.setColorRGB(i, 0, 0, 255);
    }

    m_drivetrain.setDefaultCommand(
      new DriveJoystick(
        m_drivetrain, 
        () -> m_driverController.getRawAxis(OperatorConstants.kDriveAxis), 
        () -> m_driverController.getRawAxis(OperatorConstants.kTurnAxis), 
        () -> m_driverController.getRawAxis(OperatorConstants.kSpeedFactorAxis)
      )
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
    
    // Aligns robot with tape, then turns the robot to 0 degrees on Z-axis

    new JoystickButton(m_subsystemController, 1).whileTrue(new FollowTape(VisionConstants.kSetpointCharge, VisionConstants.kSetpointTurn));
    
    new JoystickButton(m_driverController, 1).whileTrue(new FollowTape(VisionConstants.kSetpointCharge, VisionConstants.kSetpointTurn)); // Requires fine-tuning
    new JoystickButton(m_driverController, 2).whileTrue(new AlignGyro(() -> DriveConstants.kGyroSPAngle)); // Test use only
    new JoystickButton(m_driverController, 3).whileTrue(new SwitchLimelightMode());
    new JoystickButton(m_driverController, 4).whileTrue(new AutoBalance()); // Test use only
    new JoystickButton(m_driverController, 7).whileTrue(new ResetGyroZ()); 
    new JoystickButton(m_driverController, 3).whileTrue(new ParallelPark(() -> m_gyro.getZRotation())); // TODO Test/refine
    
    }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new AutoBalance();
  }
}