package org.frc4931.robot.auto;

import org.frc4931.robot.drive.DriveToRearDistance;
import org.frc4931.robot.drive.Drivetrain;
import org.strongback.command.CommandGroup;

public class AutoDrive extends CommandGroup {
    public AutoDrive(Drivetrain drivetrain) {
        sequentially(new DriveToRearDistance(drivetrain, 284.0));
    }
}
