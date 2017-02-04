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

    /**
     * Provides a field-oriented control scheme that accepts raw joystick inputs.
     * @param x The joystick's X (roll) axis, a value in [-1.0..1.0]. Translates to horizontal motion on the field.
     * @param y The joystick's Y (pitch) axis, a value in [-1.0..1.0]. Translates to motion toward/away the driver station.
     * @param rotation The joystick's twist (yaw) axis, a value in [-1.0..1.0]. Translates to rotation of the robot.
     */
    public void absoluteDrive(double x, double y, double rotation) {
        drive.cartesian(x, y, rotation);
    }

    /**
     * Provides a robot-oriented control scheme that accepts raw joystick inputs.
     * @param x The joystick's X (roll) axis, a value in [-1.0..1.0]. Translates to leftward/rightward motion.
     * @param y The joystick's Y (pitch) axis, a value in [-1.0..1.0]. Translates to forward/backward motion.
     * @param rotation The joystick's twist (yaw) axis, a value in [-1.0..1.0]. Translates to rotation of the robot.
     */
    public void relativeDrive(double x, double y, double rotation) {
        y *= -1;
        drive.polar(Math.sqrt(x * x + y * y), Math.toDegrees(Math.atan2(x, y)), rotation);
    }

    /**
     * Provides a low-speed robot-oriented control scheme for fine control.
     * @param angle The angle (in degrees) at which the robot should drive.
     */
    public void trimDrive(int angle) {
        drive.polar(DRIVE_TRIM_SPEED, angle, 0.0);
    }

    /**
     * Gets the reading from the heading sensor.
     * @return An angle in degrees describing the robot's heading. 0 is calibrated toward the far end of the field, and
     *         clockwise is positive.
     */
    public double getHeading() {
        return heading.getHeading();
    }

    /**
     * Sets the robot's current heading to zero. All future {@link #getHeading()} calls will be relative to the new
     * calibration.
     */
    public void zeroHeading() { heading.zero(); }
}
