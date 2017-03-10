package org.frc4931.robot.Conveyor;

/**
 * Created by jcrane on 1/28/17.
 */

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PWM;
import org.strongback.command.Command;
import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.components.SpeedSensor;
import org.strongback.control.Controller;

public class Conveyor implements Requirable {
    private final Motor sweeper;
    private double intakeSpeed = 1.0;
    private double shooterIdleSpeed = 600;
    private final Controller shooter;
    private final Motor intake;
    private final SpeedSensor shooterSpeed;

    /**
     * Constructs a new Conveyor subsystem given a motor controller and a motor.
     *  @param shooter The Controller that is responsible for controlling the shooting mechanism.
     * @param intake  The Motor that is responsible for driving the conveyor intake.
     * @param shooterSpeed
     */
    public Conveyor(Controller shooter, Motor intake, Motor sweeper, SpeedSensor shooterSpeed) {
        this.shooter = shooter;
        this.intake = intake;
        this.sweeper = sweeper;
        this.shooterSpeed = shooterSpeed;
    }

    /**
     * Turns on the Intake and sets the Shooter to a low speed suitable for collecting balls.
     */
    public void collect() {
        intake.setSpeed(intakeSpeed);
        shooter.withTarget(shooterIdleSpeed);
        sweeper.setSpeed(1.0);
    }

    /**
     * Turns on the Intake and sets the Shooter to a given speed.
     *
     * @param speedRPM The speed of the Shooter in RPM.
     */
    public void shoot(double speedRPM) {
        intake.setSpeed(intakeSpeed);
        shooter.withTarget(speedRPM);
        sweeper.setSpeed(1.0);
    }

    /**
     * Stops the Conveyor mechanism.
     */
    public void stop() {
        shooter.withTarget(0);
        intake.stop();
        sweeper.stop();
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

    public double getShooterSpeed() {
        return shooterSpeed.getSpeed();
    }

    public void eject() {
        intake.setSpeed(-intakeSpeed);
    }
}
