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
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.*;
import frc.robot.commands.AlignGyro;
import frc.robot.commands.ClimbChargeStation;
import frc.robot.commands.ExitCommunity;
import frc.robot.commands.FollowTarget;
import frc.robot.commands.HangOffChargeStation;
import frc.robot.commands.HoldOnChargeStation;
import frc.robot.commands.InlineTargetWall;
import frc.robot.commands.IntakeDown;
import frc.robot.commands.IntakeUp;
import frc.robot.commands.RunIntakeTime;
import frc.robot.commands.PickUpPiece;

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
  Runnable enableATProcessing = new EnableAprilTagProcessor();
  Runnable enableCubeProcessing = new EnableCubeProcessor();
  int firstLightHue;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    Shuffleboard.selectTab("Setup");
    RobotContainer.m_led.update();
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    autoChooser = new SendableChooser<>();

    // Autonomous modes
    autoChooser.addOption("Place Cube, Exit Community, Engage (Center)", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true),
      new ExitCommunity(true, true),
      new ClimbChargeStation(), 
      new HoldOnChargeStation(1),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+180)),
      new HoldOnChargeStation(5)
    ));
    autoChooser.addOption("Cube and Exit Community (Side)", new SequentialCommandGroup(
      new RunIntakeTime(1.0, true),
      new ExitCommunity(true, false),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()-160))
      ));
    autoChooser.addOption("Cube only", 
      new RunIntakeTime(5.0, true)
    );


    // TODO Test
    autoChooser.addOption("Place Cube, Exit Community, Pick Up Cube, Engage (Testing)", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true), // Place cube low
      new ParallelCommandGroup(
        // Leave community and enable cube processing
        new ExitCommunity(true, true),
        new InstantCommand(enableCubeProcessing, RobotContainer.m_vision)
        ),
      new ParallelCommandGroup(
        // Lower intake and spin to align with cube
        new IntakeDown(),
        new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+160))
      ),
      new ParallelCommandGroup(
        // Follows and picks up piece
        new FollowTarget(VisionConstants.kCubeTargetArea, VisionConstants.kCubeXOffset, VisionConstants.kCubeP),
        new PickUpPiece()
      ),
      new ParallelCommandGroup(
        // Lifts intake, spins back around, and enables apriltag processing
        new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()-160)),
        new InstantCommand(enableATProcessing, RobotContainer.m_vision),
        new IntakeUp()
      ),
      new InlineTargetWall(), // Aligns robot with apriltag
      new AlignGyro(() -> 0.0), // Robot is facing the wall
      new ClimbChargeStation(), // Climb charge station
      new HoldOnChargeStation(5) // Hold robot on the charge station
    ));

    autoChooser.addOption("Two Cubes (Testing)", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true), // Places piece
      new ParallelCommandGroup( // Exits community and lowers intake
        new ExitCommunity(true, false),
        new IntakeDown()
      ),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+180)), // Spins robot 180 degrees
      new InstantCommand(enableCubeProcessing, RobotContainer.m_vision), // Enables cube processing
      new ParallelCommandGroup( // Follows cube until it is in the intake
        new FollowTarget(VisionConstants.kCubeTargetArea, VisionConstants.kCubeXOffset, VisionConstants.kCubeP),
        new PickUpPiece()
      ),
      new ParallelCommandGroup( // Lifts intake, spins back around, enables apriltag processor
        new IntakeUp(),
        new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()+180)),
        new InstantCommand(enableATProcessing, RobotContainer.m_vision)
      ),
      new FollowTarget(VisionConstants.kAprilTagTargetArea, VisionConstants.kAprilTagXOffset, VisionConstants.kAprilTagP), // Drives up to node with apriltag
      new RunIntakeTime(1.0, true) // Places piece
    ));


    autoChooser.addOption("Balance and Hang off Edge (Debug)", new SequentialCommandGroup(
      new ExitCommunity(true, true),
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()+90),
      new ClimbChargeStation(),
      new HangOffChargeStation()
    ));
    autoChooser.addOption("Follow & Pick Up Cube (Debug)", new SequentialCommandGroup(
      new ParallelCommandGroup(
        new FollowTarget(VisionConstants.kCubeTargetArea, VisionConstants.kCubeXOffset, VisionConstants.kCubeP),
        new PickUpPiece()
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
    pulse_led_red();
    RobotContainer.m_led.update();
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
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
    RobotContainer.m_vision.enableDriverCamera();

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
  class EnableCubeProcessor implements Runnable {
    public void run() {
      RobotContainer.m_vision.enableCubeProcessor();
    }
  }

  private void pulse_led_red() {
    for (int v=0;v<235;v++) {
      for (int i=0;i<RobotContainer.m_led.getLength();i++) {
        // int h = i*(180/RobotContainer.m_led.getLength());
        int h =0;
        RobotContainer.m_led.setColorHSV(i, h, 235, 230);
      }
    }

    for (int v=235;v>0;v--) {
      for (int i=0;i<RobotContainer.m_led.getLength();i++) {
        // int h = i*(180/RobotContainer.m_led.getLength());
        int h =0;
        
        RobotContainer.m_led.setColorHSV(i, h, 235, v);
      }
    }

  }
}
