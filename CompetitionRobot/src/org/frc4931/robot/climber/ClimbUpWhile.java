package org.frc4931.robot.climber;

import org.strongback.command.Command;
import org.strongback.components.Switch;

public class ClimbUpWhile extends Command {

    private final ClimberSubSystem climber;
    private final Switch shouldContinue;

    public ClimbUpWhile(ClimberSubSystem climber, Switch shouldContinue) {
        super(climber);
        this.climber = climber;
        this.shouldContinue = shouldContinue;
    }

    @Override
    public void initialize() {
        climber.climbUp();
    }

    @Override
    public boolean execute() {
        return !shouldContinue.isTriggered();
    }

    @Override
    public void end() {
        climber.stop();
    }
}
