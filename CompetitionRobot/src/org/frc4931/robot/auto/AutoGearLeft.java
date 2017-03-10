package org.frc4931.robot.auto;

import org.frc4931.robot.drive.DriveFor;
import org.frc4931.robot.drive.Drivetrain;
import org.frc4931.robot.drive.TurnTo;
import org.frc4931.robot.vision.VisionSystem;
import org.strongback.command.CommandGroup;

public class AutoGearLeft extends CommandGroup {
    public AutoGearLeft(Drivetrain drivetrain, VisionSystem visionSystem) {
        sequentially(
                new DriveFor(drivetrain, 0.0, 0.4, 0.0, 1.0),
                new TurnTo(drivetrain, 300),
                new DriveToLift(drivetrain, visionSystem)
        );
    }
}
