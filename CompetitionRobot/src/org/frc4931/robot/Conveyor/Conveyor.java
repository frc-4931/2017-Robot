package org.frc4931.robot.Conveyor;

/**
 * Created by jcrane on 1/28/17.
 */

import org.strongback.command.Requirable;
import org.strongback.control.Controller;
import org.strongback.components.Motor;
import org.strongback.control.PIDController;

public class Conveyor implements Requirable{
    private static final double INTAKE_SPEED = 0.5;
    private static final double SHOOTER_SPEED_IDLE = 150;
    private final Controller shooter;
    private Motor intake;

    public Conveyor(Controller shooter, Motor intake) {
        this.shooter = shooter;
        this.intake = intake;
    }

    public void collect () {
        intake.setSpeed(INTAKE_SPEED);
        shooter.withTarget(SHOOTER_SPEED_IDLE);
    }

    public void shoot (double speedRPM) {
        intake.setSpeed(INTAKE_SPEED);
        shooter.withTarget(speedRPM);
    }

    public void stop () {
        shooter.withTarget(0);
        intake.stop();
    }

}
