package org.frc4931.robot.drive;

import org.strongback.command.Requirable;
import org.strongback.components.Compass;
import org.strongback.drive.MecanumDrive;

public class Drivetrain implements Requirable {
    public static final double DRIVE_TRIM_SPEED = 0.2;

    private final MecanumDrive drive;
    private final Compass heading;

    public Drivetrain(MecanumDrive drive, Compass heading) {
        this.drive = drive;
        this.heading = heading;
    }

    public void absoluteDrive(double x, double y, double rotation) {
        drive.cartesian(x, y, rotation);
    }

    public void relativeDrive(double x, double y, double rotation) {
        drive.polar(Math.sqrt(x * x + y * y), Math.toDegrees(Math.atan2(x, y)), rotation);
    }

    public void trimDrive(int angle) {
        drive.polar(DRIVE_TRIM_SPEED, angle, 0.0);
    }

    public double getHeading() {
        return heading.getHeading();
    }
}
