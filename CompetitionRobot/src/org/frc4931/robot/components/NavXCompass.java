package org.frc4931.robot.components;

import com.kauailabs.navx.frc.AHRS;
import org.strongback.components.Compass;

public class NavXCompass implements Compass {
    private final AHRS navX;

    public NavXCompass(AHRS navX) {
        this.navX = navX;
    }

    @Override
    public double getAngle() {
        return navX.getFusedHeading();
    }

    @Override
    public Compass zero() {
        navX.zeroYaw();
        return this;
    }
}
