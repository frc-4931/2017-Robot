package org.frc4931.robot.Conveyor;

import org.strongback.command.Command;
import org.strongback.components.Switch;

/**
 * Created by jcrane on 1/28/17.
 */

public class ConveyorCollect extends Command {
    private final Conveyor conveyor;
    private final Switch shouldContinue;

    /**
     * Turns the intake on and the shooter to shooterIdleSpeed.
     *
     * @param conveyor       The conveyor to set to collect.
     * @param shouldContinue A Switch that when released will stop the command.
     */
    public ConveyorCollect(Conveyor conveyor, Switch shouldContinue) {
        super(conveyor);
        this.conveyor = conveyor;
        this.shouldContinue = shouldContinue;
    }

    @Override
    public void initialize() {
        conveyor.collect();
    }

    @Override
    public boolean execute() {
        return !shouldContinue.isTriggered();
    }

    @Override
    public void end() {
        super.end();
    }
}
