package org.frc4931.robot.Conveyor;

import org.strongback.command.Command;
import org.strongback.components.Switch;

/**
 * Created by jcrane on 2/1/17.
 */
public class ConveyorShootWhile extends Command {
    private final Conveyor conveyor;
    private final Switch shouldContinue;
    private final int shootingSpeed;

    /**
     * Turns the conveyor on and sets the shooter to the given value.
     *
     * @param conveyor       The conveyor to set to shoot.
     * @param shouldContinue A Switch that when released will stop the command.
     * @param shootingSpeed  The speed in RPM that the shooter will run at.
     */
    public ConveyorShootWhile(Conveyor conveyor, Switch shouldContinue, int shootingSpeed) {
        super(conveyor);
        this.conveyor = conveyor;
        this.shouldContinue = shouldContinue;
        this.shootingSpeed = shootingSpeed;
    }

    @Override
    public void initialize() {
        conveyor.shoot(shootingSpeed);
    }

    @Override
    public boolean execute() {
        return !shouldContinue.isTriggered();
    }

    @Override
    public void end() {
        conveyor.stop();
    }
}
