// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Proximity;
import frc.robot.subsystems.Vision;

public class DriveForza extends CommandBase {
  Drivetrain drivetrain = RobotContainer.m_drivetrain;
  Vision limelight = RobotContainer.m_vision;
  Proximity proximity = RobotContainer.m_proximity;

  Supplier<Double> kForward, kBackward, kTurn;
  Supplier<Boolean> kSTFactor;

  double kSpeed;
  double spdFact;
  double trnFact;

  GenericEntry sbSensorStatus = RobotContainer.sbProxSensorStatus;

  /** Creates a new ForzaDrive. */
  public DriveForza(Supplier<Double> forward, Supplier<Double> backward, Supplier<Double> turn, Supplier<Boolean> stFact) {
    this.kForward = forward;
    this.kBackward = backward;
    this.kTurn = turn;
    this.kSTFactor = stFact;
    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    kSpeed = kForward.get() - kBackward.get();
    
    if (kSTFactor.get()) {
      spdFact = DriveConstants.kSecondarySpeedFactor;
      trnFact = DriveConstants.kSecondaryTurnFactor;
    } else {
      spdFact = DriveConstants.kDefaultSpeedFactor;
      trnFact = DriveConstants.kDefaultTurnFactor;
    }
    drivetrain.driveArcade(kSpeed, kTurn.get(), spdFact, trnFact);
    limelight.update();

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}