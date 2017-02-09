package org.frc4931.robot;

import org.strongback.components.Motor;
import org.strongback.components.Switch;

public class ClimberSubSystem 
{
	private final Motor climbPaddle;
	private final Switch horizontal, vertical, touchPad;
	
	public ClimberSubSystem(Motor climbPaddle1, Motor climbPaddle2, 
							Switch horizontal, Switch vertical, Switch touchPad)
	{
		climbPaddle = Motor.compose(climbPaddle1, climbPaddle2);
		
		this.vertical = vertical;
		this.horizontal = horizontal;
		this.touchPad = touchPad;
	}
	
	public void start()
	{
		climbPaddle.setSpeed(1.0);
	}
	
	public void stop()
	{
		climbPaddle.stop();
	}
	
	public boolean hasScored()
	{
		return touchPad.isTriggered();
	}
	
	public boolean isHorizontal()
	{
		return horizontal.isTriggered();
	}
	
	public boolean isVertical()
	{
		return vertical.isTriggered();
	}
}
