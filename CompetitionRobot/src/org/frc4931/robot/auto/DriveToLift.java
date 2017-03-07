package org.frc4931.robot.auto;

import org.frc4931.robot.drive.Drivetrain;
import org.frc4931.robot.vision.VisionSystem;
import org.strongback.command.Command;

public class DriveToLift extends Command {
    private final double STOP_DISTANCE = 55.0;

    private final Drivetrain drivetrain;
    private final VisionSystem visionSystem;

    public DriveToLift(Drivetrain drivetrain, VisionSystem visionSystem) {
        super(drivetrain, visionSystem);
        this.drivetrain = drivetrain;
        this.visionSystem = visionSystem;
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
