// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.VisionConstants;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ParallelPark extends SequentialCommandGroup {

  /** Creates a new ParallelPark. */
  public ParallelPark(Supplier<Double> zRotation) {
    addCommands(
      new AlignGyro(() -> -zRotation.get()), // Sets robot to negative value of current Z rotation
      new DriveUntilTapeFound(0.55, 0.05), // Robot sees tape on limelight
      new InlineTapeWall(), // Sets robot inline with tape and perpendicular to wall (rotated randomly)
      new AlignGyro(() -> 0.0), // Sets robot straight on with tape and wall
      new FollowTape(VisionConstants.kSetpointCharge, VisionConstants.kSetpointTurn)); // Drives towards tape
  }
}
