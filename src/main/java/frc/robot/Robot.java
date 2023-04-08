// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
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
import frc.robot.commands.HoldOnChargeStation;
import frc.robot.commands.IntakeDown;
import frc.robot.commands.IntakeUp;
import frc.robot.commands.MoveForwardUntilPiece;
import frc.robot.commands.RunIntakeTime;
import frc.robot.commands.TurnUntilTargetFound;
import frc.robot.subsystems.Vision;

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
  Runnable enableAT1Processing = new EnableAprilTag1Processor();
  Runnable enableAT2Processing = new EnableAprilTag2Processor();
  Runnable enableCubeProcessing = new EnableCubeProcessor();
  
  int firstLightHue;
  public static Timer runtime = new Timer();
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    runtime.start();
    Shuffleboard.selectTab("Setup");
    RobotContainer.m_led.update();
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    autoChooser = new SendableChooser<>();

    // Autonomous modes
    autoChooser.addOption("1 Cube Engage (Center)", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true),
      new ExitCommunity(true, true),
      new ClimbChargeStation(), 
      new HoldOnChargeStation(10)
    ));
    
    autoChooser.addOption("Two Cubes (Red, Side w/o Wires)", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true), // Places piece
      new ParallelCommandGroup( // Exits community and lowers intake
        new ExitCommunity(true, false),
        new IntakeDown(),
        new InstantCommand(enableCubeProcessing, RobotContainer.m_vision) // Enables cube processing
      ),
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()+175),
      new TurnUntilTargetFound(-AutonomousConstants.kDefaultTurnSpeed, VisionConstants.kCube1SideMinArea), // Spins robot until cube found
      new MoveForwardUntilPiece(VisionConstants.kCubePDrive, VisionConstants.kCubePTurn, VisionConstants.kCubeTargetArea),
      new InstantCommand(enableAT1Processing, RobotContainer.m_vision),
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()+170),
      new TurnUntilTargetFound(AutonomousConstants.kInchingTurnSpeed, VisionConstants.kS1AprilTagNonwiredMinArea),
      new FollowTarget(VisionConstants.kS1AprilTagNonwiredTargetArea, VisionConstants.kS1AprilTagNonwiredXOffset, VisionConstants.kS1AprilTagNonwiredPDrive, VisionConstants.kS1AprilTagNonwiredPTurn), // Drives up to node with apriltag
      new FollowTarget(VisionConstants.kS2AprilTagNonwiredTargetArea, VisionConstants.kS2AprilTagNonwiredXOffset, VisionConstants.kS2AprilTagNonwiredPDrive, VisionConstants.kS2AprilTagNonwiredPTurn), // Drives up to node with apriltag
      new RunIntakeTime(0.5, true), // Places piece
      new ParallelCommandGroup( // Exits community and lowers intake
        new ExitCommunity(true, false),
        new InstantCommand(enableCubeProcessing, RobotContainer.m_vision) // Enables cube processing
      ),
      new TurnUntilTargetFound(AutonomousConstants.kDefaultTurnSpeed, VisionConstants.kCube2SideMinArea) // Turns opposite direction to find cube faster
    ));

    autoChooser.addOption("Two Cubes (Blue, Side w/o Wires)", new SequentialCommandGroup(
      new RunIntakeTime(0.5, true), // Places piece
      new ParallelCommandGroup(
        new ExitCommunity(true, false),
        new InstantCommand(enableCubeProcessing, RobotContainer.m_vision)
      ),
      new TurnUntilTargetFound(AutonomousConstants.kDefaultTurnSpeed, VisionConstants.kCube1SideMinArea),
      new MoveForwardUntilPiece(VisionConstants.kCubePDrive, VisionConstants.kCubePTurn, VisionConstants.kCubeTargetArea),
      new ParallelCommandGroup(
        new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()+150),
        new InstantCommand(enableAT1Processing, RobotContainer.m_vision)  
      ),
      new TurnUntilTargetFound(AutonomousConstants.kDefaultTurnSpeed, VisionConstants.kS1AprilTagNonwiredMinArea),
      new FollowTarget(VisionConstants.kS1AprilTagNonwiredTargetArea, -VisionConstants.kS1AprilTagNonwiredXOffset, VisionConstants.kS1AprilTagNonwiredPDrive, VisionConstants.kS1AprilTagNonwiredPTurn),
      new InstantCommand(enableAT2Processing, RobotContainer.m_vision),
      new FollowTarget(VisionConstants.kS2AprilTagNonwiredTargetArea, -VisionConstants.kS2AprilTagNonwiredXOffset, VisionConstants.kS2AprilTagNonwiredPDrive, VisionConstants.kS2AprilTagNonwiredPTurn),
      new RunIntakeTime(0.5, true)
    ));
    autoChooser.addOption("Gyro/NavX Test", new SequentialCommandGroup(
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()+160),
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()-200),
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()-200),
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()+240),
      new AlignGyro(() -> RobotContainer.m_gyro.getZRotation()+180)

    ));
    autoChooser.addOption("1 Cube & Exit (Side)", new SequentialCommandGroup(
      new RunIntakeTime(1.0, true),
      new ExitCommunity(true, false),
      new AlignGyro(() -> (RobotContainer.m_gyro.getZRotation()-160))
    ));

    autoChooser.addOption("Cube only (Any)", 
      new RunIntakeTime(5.0, true)
    );
        
    autoChooser.addOption("2 Cube Engage (Testing, Center)", new SequentialCommandGroup(
      new RunIntakeTime(0.30, true), // Place cube low
      new ParallelCommandGroup( // Leave community and enable cube processing
        new ExitCommunity(true, true),
        new InstantCommand(enableCubeProcessing, RobotContainer.m_vision)
      ),
      new ParallelCommandGroup( // Lower intake and spin to align with cube
        new IntakeDown(),
        new TurnUntilTargetFound(-AutonomousConstants.kDefaultTurnSpeed, VisionConstants.kCubeCenterMinArea)
      ),
      new MoveForwardUntilPiece(VisionConstants.kCubePDrive, VisionConstants.kCubePTurn, VisionConstants.kCubeTargetArea),
      new ParallelCommandGroup( // Lifts intake, spins back around, ensures cube is secured in intake
        new RunIntakeTime(0.15, false),
        new AlignGyro(() -> (0.0)), // Robot facing wall
        new IntakeUp()
      ),
      new FollowTarget(VisionConstants.kEngageAprilTagTargetArea, VisionConstants.kEngageAprilTagXOffset, VisionConstants.kEngageAprilTagPDrive, VisionConstants.kEngageAprilTagPTurn),
      new ClimbChargeStation(), // Climb charge station
      new ParallelCommandGroup(
        new RunIntakeTime(1.0, true),
        new HoldOnChargeStation(5) // Hold robot on the charge station
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
    Shuffleboard.selectTab("SmartDashboard");
    // Gets selected autonomous command
    m_autonomousCommand = (Command) autoChooser.getSelected();
    // Schedules autonomous command
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    Shuffleboard.selectTab("SmartDashboard");

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
  class EnableCubeProcessor implements Runnable {
    public void run() {
      RobotContainer.m_vision.enableCubeProcessor();
    }
  }
}
