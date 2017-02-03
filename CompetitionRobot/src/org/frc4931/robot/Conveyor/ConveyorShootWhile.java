package org.frc4931.robot.Conveyor;

import org.strongback.command.Command;
import org.strongback.command.Requirable;
import org.strongback.components.Switch;

/**
 * Created by jcrane on 2/1/17.
 */
public class ConveyorShootWhile extends Command{
    private final Conveyor conveyor;
    private final Switch shouldContinue;
    private final int shootingSpeed;

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
        conveyor.collect();
    }
}
