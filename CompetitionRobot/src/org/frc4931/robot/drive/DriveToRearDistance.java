package org.frc4931.robot.drive;

import org.strongback.command.Command;

public class DriveToRearDistance extends Command {

    private static final double TOLERANCE = 5.0;
    private static final double DRIVE_SPEED = 0.3;

    private final Drivetrain drivetrain;
    private final double stopDistance;

    public DriveToRearDistance(Drivetrain drivetrain, double stopDistance) {
        super(drivetrain);
        this.drivetrain = drivetrain;
        this.stopDistance = stopDistance;
    }

    public DriveToRearDistance(Drivetrain drivetrain, double stopDistance, double timeout) {
        super(timeout, drivetrain);
        this.drivetrain = drivetrain;
        this.stopDistance = stopDistance;
    }

    @Override
    public boolean execute() {
        double error = stopDistance - drivetrain.getRearDistance();

        drivetrain.relativeDrive(0.0, Math.copySign(error, DRIVE_SPEED), 0.0);
        return Math.abs(error) <= TOLERANCE;
    }

    @Override
    public void end() {
        drivetrain.stop();
    }
}
