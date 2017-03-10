package org.frc4931.robot.drive;

import org.strongback.command.Command;
import org.strongback.components.Switch;

public class DriveWhile extends Command {

    private final Drivetrain drivetrain;
    private final Switch condition;
    private final double driveX;
    private final double driveY;
    private final double driveRotation;

    public DriveWhile(Drivetrain drivetrain, double driveX, double driveY, double driveRotation, Switch condition) {
        super(drivetrain);
        this.drivetrain = drivetrain;
        this.condition = condition;
        this.driveX = driveX;
        this.driveY = driveY;
        this.driveRotation = driveRotation;
    }

    @Override
    public void initialize() {
        drivetrain.drive(driveX, driveY, driveRotation);
    }

    @Override
    public boolean execute() {
        return !condition.isTriggered();
    }

    @Override
    public void end() {
        drivetrain.stop();
    }
}
