package org.frc4931.robot.Conveyor;

import org.frc4931.robot.Conveyor.*;
import org.strongback.SwitchReactor;
import org.strongback.command.Command;
import org.strongback.components.Switch;

/**
 * Created by jcrane on 1/28/17.
 */
public class ConveyorCollect extends Command {
    private final Conveyor conveyor;

    public ConveyorCollect(Conveyor conveyor) {
        super(conveyor);
        this.conveyor = conveyor;
    }

    @Override
    public void initialize() {
        conveyor.collect();
    }

    @Override
    public boolean execute() {
        return true;
        //return !shouldContinue.isTriggered();
    }

    @Override
    public void end() {

    }
}
