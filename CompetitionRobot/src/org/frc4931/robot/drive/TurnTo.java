package org.frc4931.robot.drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.control.Controller;
import org.strongback.control.SoftwarePIDController;

public class TurnTo extends Command {
    private static final double HEADING_TOLERANCE = 5.0;
    private static final double TURN_SPEED = 0.3;

    private final Drivetrain drivetrain;
    private final double heading;

    public TurnTo(Drivetrain drivetrain, double heading) {
        super(drivetrain);
        this.drivetrain = drivetrain;
        this.heading = heading;
    }

    @Override
    public boolean execute() {
        double error = heading - drivetrain.getHeading();
        if (Math.abs(error) > 180.0) {
            error = 180 - error;
        }
        drivetrain.relativeDrive(0.0, 0.0, Math.copySign(error, TURN_SPEED));
        return Math.abs(error) <= HEADING_TOLERANCE;
    }

    @Override
    public void end() {
        drivetrain.stop();
    }
}
