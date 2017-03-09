package org.frc4931.robot.auto;

import org.frc4931.robot.drive.DriveToFrontDistance;
import org.frc4931.robot.drive.Drivetrain;
import org.frc4931.robot.vision.VisionSystem;
import org.strongback.command.Command;

public class DriveToLift extends DriveToFrontDistance {
    private static final double STOP_DISTANCE = 55.0;

    private final Drivetrain drivetrain;

    public DriveToLift(Drivetrain drivetrain) {
        super(drivetrain, STOP_DISTANCE);
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        drivetrain.relativeDrive(0.0, 0.25, 0.0);
    }

    @Override
    public boolean execute() {
        return drivetrain.getFrontDistance() <= STOP_DISTANCE;
    }

    @Override
    public void end() {
        drivetrain.stop();
    }
}
