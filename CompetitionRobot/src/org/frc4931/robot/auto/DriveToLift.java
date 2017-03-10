package org.frc4931.robot.auto;

import org.frc4931.robot.drive.Drivetrain;
import org.strongback.command.Command;

public class DriveToLift extends Command {
    private static final double STOP_DISTANCE = 55.0;
    private final Drivetrain drivetrain;

    public DriveToLift(Drivetrain drivetrain) {
        super(drivetrain);
        this.drivetrain = drivetrain;
    }

    public DriveToLift(Drivetrain drivetrain, double timeout) {
        super(timeout, drivetrain);
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
