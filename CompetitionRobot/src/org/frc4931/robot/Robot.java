/* Created Sat Jan 07 19:18:14 CST 2017 */
package org.frc4931.robot;

import org.frc4931.robot.Conveyor.ConveyorShootWhile;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.Motor;
import org.frc4931.robot.Conveyor.Conveyor;
import org.frc4931.robot.Conveyor.ConveyorCollect;
import org.frc4931.robot.Conveyor.ConveyorStop;
import edu.wpi.first.wpilibj.IterativeRobot;
import org.strongback.components.Switch;
import org.strongback.components.ui.FlightStick;
import org.strongback.control.TalonController;
import org.strongback.hardware.Hardware;


public class Robot extends IterativeRobot {

    @Override
    public void robotInit() {
        TalonController ballShooter = Hardware.Controllers.talonController(1, 2.5, 0);
        Motor conveyorIntake = Hardware.Motors.talonSRX(2);

        ballShooter.setControlMode(TalonController.ControlMode.SPEED);
        ballShooter.withGains(0, 0, 0);

        FlightStick gamepad1 = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        Conveyor conveyor = new Conveyor(ballShooter, conveyorIntake);
        Switch intake = gamepad1.getButton(3);
        Switch shoot = gamepad1.getButton(5);
        Switch stop = gamepad1.getButton(4);

        SwitchReactor reactor = Strongback.switchReactor();
        reactor.onTriggered(intake, () -> new ConveyorCollect(conveyor, intake));
        reactor.onTriggered(shoot, () -> new ConveyorShootWhile(conveyor, shoot, 1600));
        reactor.onTriggered(stop, () -> new ConveyorStop(conveyor));
    }

    @Override
    public void teleopInit() {
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {

    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }
}
