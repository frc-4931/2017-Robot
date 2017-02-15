package org.frc4931.robot;

import org.strongback.components.Motor;
import org.strongback.components.Switch;

/**
 * Created by christianmeinz1516 on 2/14/17
 */
public class ClimberSubSystem{
    private final Motor climbPaddle;
    private final Switch horizontal, vertical, touchPad;
	
    /*
     * @param climbPaddle The combined motor for the climber
     * @param horizontal  Trip sensor for detecting horizontal
     * @param vertical    Trip sensor for detecting vertical
     * @param touchPad    Trip sensor for detecting touch pad
     */
    public ClimberSubSystem(Motor climbPaddle, Switch horizontal, Switch vertical, Switch touchPad){
        this.climbPaddle = climbPaddle;
		
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.touchPad = touchPad;
    }
	
    /*
     * Starts the climber motor at a speed of 1
     */
    public void start(){
        climbPaddle.setSpeed(1.0);
    }
	
    /*
     * Stops the climber motor
     */
    public void stop(){
        climbPaddle.stop();
    }
	
    /*
     * Returns if the touch pad has been triggered
     */
    public boolean hasScored(){
        return touchPad.isTriggered();
    }
	
    /*
     * Returns if the horizontal switch has been triggered
     */
    public boolean isHorizontal(){
        return horizontal.isTriggered();
    }
	
    /*
     * Returns if the vertical switch has been triggered
     */
    public boolean isVertical(){
        return vertical.isTriggered();
    }
    
}
