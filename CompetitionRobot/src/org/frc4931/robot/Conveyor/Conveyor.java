package org.frc4931.robot.Conveyor;

/**
 * Created by jcrane on 1/28/17.
 */

import org.strongback.command.Requirable;
import org.strongback.control.Controller;
import org.strongback.components.Motor;

public class Conveyor implements Requirable {
    private double intakeSpeed = 0.5;
    private double shooterIdleSpeed = 150;
    private final Controller shooter;
    private final Motor intake;

    /**
     * Constructs a new Conveyor subsystem given a motor controller and a motor.
     *
     * @param shooter The Controller that is responsible for controlling the shooting mechanism.
     * @param intake  The Motor that is responsible for driving the conveyor intake.
     */
    public Conveyor(Controller shooter, Motor intake) {
        this.shooter = shooter;
        this.intake = intake;
    }

    /**
     * Turns on the Intake and sets the Shooter to a low speed suitable for collecting balls.
     */
    public void collect() {
        intake.setSpeed(intakeSpeed);
        shooter.withTarget(shooterIdleSpeed);
    }

    /**
     * Turns on the Intake and sets the Shooter to a given speed.
     *
     * @param speedRPM The speed of the Shooter in RPM.
     */
    public void shoot(double speedRPM) {
        intake.setSpeed(intakeSpeed);
        shooter.withTarget(speedRPM);
    }

    /**
     * Stops the Conveyor mechanism.
     */
    public void stop() {
        shooter.withTarget(0);
        intake.stop();
    }

    /**
     * Set the speed that the intake mechanism will use when turned on.
     *
     * @param intakeSpeed The speed in voltage percent.
     */
    public void setIntakeSpeed(double intakeSpeed) {
        this.intakeSpeed = intakeSpeed;
    }

    /**
     * Sets the speed that the shooter will run at while idle.
     *
     * @param shooterIdleSpeed The speed in RPM.
     */
    public void setShooterIdleSpeed(double shooterIdleSpeed) {
        this.shooterIdleSpeed = shooterIdleSpeed;
    }

}
