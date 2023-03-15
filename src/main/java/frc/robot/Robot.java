// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.*;
import frc.robot.commands.AimLimelightAhead;
import frc.robot.commands.AlignGyro;
import frc.robot.commands.ClimbChargeStation;
import frc.robot.commands.ExitCommunity;
import frc.robot.commands.FollowTarget;
import frc.robot.commands.HangOffChargeStation;
import frc.robot.commands.HoldOnChargeStation;
import frc.robot.commands.RunIntakeTime;
import frc.robot.commands.RunIntakeUntilPiece;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  ShuffleboardTab setupTab = Shuffleboard.getTab("Setup");
  // Chooses autonomous command
  private SendableChooser<Object> autoChooser;
  private RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    Shuffleboard.selectTab("Setup");

    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    autoChooser = new SendableChooser<>();

    autoChooser.addOption("Balance and Hang off Edge", new SequentialCommandGroup(
      new ExitCommunity(true),
      new ClimbChargeStation(),
      new HoldOnChargeStation(),
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()+90),
      new HangOffChargeStation()
    ));
    // Autonomous modes
    autoChooser.addOption("Place Cube, Exit Community, Pick Up Cube, Engage", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true),
      new ExitCommunity(true),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+160)),
      new FollowTarget(VisionConstants.kCubeTargetArea, VisionConstants.kCubeXOffset, VisionConstants.kCubeP),
      new RunIntakeTime(0.5, false),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()-160)),
      // new InstantCommand(m_enableAprilTagProcessor, RobotContainer.m_vision),
      // new InlineTargetWall(),
      new ClimbChargeStation(), 
      new HoldOnChargeStation()
    ));

    autoChooser.addOption("Place Cube, Exit Community, Engage", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true),
      new ExitCommunity(true),
      new ClimbChargeStation(), 
      new HoldOnChargeStation(),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+180)),
      new HoldOnChargeStation(),
      new HoldOnChargeStation()
    ));
    autoChooser.addOption("Cube and Exit Community", new SequentialCommandGroup(
      new RunIntakeTime(1.0, true),
      new ExitCommunity(true),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+180))
      ));
    autoChooser.addOption("Cube only", 
      new RunIntakeTime(5.0, true)
    );

    // TODO Test
    autoChooser.addOption("Two Cubes", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true),
      new ExitCommunity(true),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+180)),
      new ParallelCommandGroup(
        new FollowTarget(VisionConstants.kCubeTargetArea, VisionConstants.kCubeXOffset, VisionConstants.kCubeP),
        new RunIntakeUntilPiece()
      ),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+180)),
      new FollowTarget(VisionConstants.kAprilTagTargetArea, VisionConstants.kAprilTagXOffset, VisionConstants.kAprilTagP),
      new RunIntakeTime(1.0, true)
    ));

    autoChooser.addOption("Follow & Pick Up Cube", new SequentialCommandGroup(
      new ParallelCommandGroup(
        new FollowTarget(VisionConstants.kCubeTargetArea, VisionConstants.kCubeXOffset, VisionConstants.kCubeP),
        new RunIntakeUntilPiece()
      )
    ));
    autoChooser.addOption("None", null);

    // Add choosers to setup tab
    setupTab.add(autoChooser);

    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    new AimLimelightAhead();
    RobotContainer.m_drivetrain.disableBrakes();
    Shuffleboard.selectTab("Setup");

  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    Shuffleboard.selectTab("Debug");

    // Gets selected autonomous command
    m_autonomousCommand = (Command) autoChooser.getSelected();
    // Schedules autonomous command
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    Shuffleboard.selectTab("Debug");

    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}

  class ResetGyroZ implements Runnable {
    public void run() {
      RobotContainer.m_gyro.resetZRotation();
    }
  }
  class EnableAprilTagProcessor implements Runnable {
    public void run() {
      RobotContainer.m_vision.enableAprilTagProcessor();
    }
  }
}
