package org.frc4931.robot.drive;

import org.strongback.command.Requirable;
import org.strongback.drive.MecanumDrive;

public class Drivetrain implements Requirable {
    public static final double DRIVE_TRIM_SPEED = 0.2;

    private final MecanumDrive drive;

    public Drivetrain(MecanumDrive drive) {
        this.drive = drive;
    }

    public void absoluteDrive(double x, double y, double rotation) {
        drive.cartesian(x, y, rotation);
    }

    public void relativeDrive(double x, double y, double rotation) {
        y *= -1; // Joystick axis needs to be inverted
        drive.polar(Math.sqrt(x * x + y * y), Math.atan2(-y, x), rotation);
    }

    public void trimDrive(int angle) {
        drive.polar(DRIVE_TRIM_SPEED, angle, 0.0);
    }
}
