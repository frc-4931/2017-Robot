package org.frc4931.robot.Conveyor;

import org.strongback.command.Command;
import org.strongback.components.Switch;

public class ConveyorEjectWhile extends Command {
    private final Conveyor conveyor;
    private final Switch condition;

    public ConveyorEjectWhile(Conveyor conveyor, Switch condition) {
        super(conveyor);
        this.conveyor = conveyor;
        this.condition = condition;
    }

    @Override
    public void initialize() {
        conveyor.eject();
    }

    @Override
    public boolean execute() {
        return !condition.isTriggered();
    }

    @Override
    public void end() {
        conveyor.stop();
    }
}
