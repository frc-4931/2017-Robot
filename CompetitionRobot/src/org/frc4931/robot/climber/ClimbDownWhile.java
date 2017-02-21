package org.frc4931.robot.climber;

import org.strongback.command.Command;
import org.strongback.components.Switch;

public class ClimbDownWhile extends Command {
    private final ClimberSubSystem climber;
    private final Switch shouldContinue;

    public ClimbDownWhile(ClimberSubSystem climber, Switch shouldContinue) {
        super(climber);
        this.climber = climber;
        this.shouldContinue = shouldContinue;
    }

    @Override
    public void initialize() {
        climber.climbDown();
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
