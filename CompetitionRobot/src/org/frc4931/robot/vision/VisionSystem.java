package org.frc4931.robot.vision;

import org.frc4931.robot.components.Pixy;
import org.frc4931.robot.components.PixyBlock;
import org.strongback.command.Requirable;

import java.util.Arrays;
import java.util.List;

public class VisionSystem implements Requirable {
    public static final int TARGET_SIGNATURE = 1;

    private final Pixy pixy;
    private final double lateralTarget;

    private double forwardDistanceToLift;
    private double lateralDistanceToLift;

    public VisionSystem(Pixy pixy, double lateralTarget) {
        this.pixy = pixy;
        this.lateralTarget = lateralTarget;
        forwardDistanceToLift = Double.NaN;
        lateralDistanceToLift = Double.NaN;
    }

    public synchronized void update() {
        List<PixyBlock> targets = Arrays.asList(pixy.getBlocks());
        targets.removeIf((block) -> block.getSignature() != TARGET_SIGNATURE);

        if (targets.size() == 2) {
            forwardDistanceToLift = 1 / Math.abs(targets.get(0).getCenterX() - targets.get(1).getCenterX()); //TODO
            lateralDistanceToLift = (targets.get(0).getCenterX() + targets.get(1).getCenterX()) / 2.0 - lateralTarget;
        }
    }

    public synchronized double getForwardDistanceToLift() {
        return forwardDistanceToLift;
    }

    public synchronized double getLateralDistanceToLift() {
        return lateralDistanceToLift;
    }
}
