package org.frc4931.robot.drive;

import org.strongback.command.Command;

public class DriveFor extends Command {
    private final Drivetrain drivetrain;
    private final double x;
    private final double y;
    private final double rotation;

    public DriveFor(Drivetrain drivetrain, double x, double y, double rotation, double duration) {
        super(duration, drivetrain);
        this.drivetrain = drivetrain;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    @Override
    public void initialize() {
        drivetrain.drive(x, y, rotation);
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public void end() {
        drivetrain.stop();
    }
}
