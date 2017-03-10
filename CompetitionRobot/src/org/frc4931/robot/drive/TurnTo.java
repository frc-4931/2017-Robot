package org.frc4931.robot.drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.control.Controller;
import org.strongback.control.SoftwarePIDController;

public class TurnTo extends Command {
    private static final double HEADING_TOLERANCE = 5.0;

    private final Drivetrain drivetrain;
    private final Controller controller;

    public TurnTo(Drivetrain drivetrain, double heading) {
        super(drivetrain);
        this.drivetrain = drivetrain;
        controller = new SoftwarePIDController(
                SoftwarePIDController.SourceType.DISTANCE,
                drivetrain::getHeading,
                (out) -> drivetrain.drive(0.0, 0.0, out)
        )
                .continuousInputs(true)
                .withInputRange(0.0, 360.0)
                .withTolerance(HEADING_TOLERANCE)
                .withGains(
                        SmartDashboard.getNumber("P", 0.0),
                        SmartDashboard.getNumber("I", 0.0),
                        SmartDashboard.getNumber("D", 0.0)
                )
                .withTarget(heading);
    }

    @Override
    public void initialize() {
        controller.enable();
    }

    @Override
    public boolean execute() {
        controller.executable().execute(Strongback.timeSystem().currentTimeInMillis());
//        return controller.isWithinTolerance();
        return false;
    }

    @Override
    public void end() {
        System.out.println("done");
        controller.disable();
        drivetrain.stop();
    }
}
