package org.frc4931.robot.Conveyor;

import org.strongback.command.Command;
import org.strongback.components.Switch;

/**
 * Created by jcrane on 2/1/17.
 */
public class ConveyorStop extends Command {
    private final Conveyor conveyor;

    public ConveyorStop(Conveyor conveyor) {
        super(conveyor);
        this.conveyor = conveyor;
    }

    @Override
    public void initialize() {
        conveyor.stop();
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public void end() {
        super.end();
    }
}
