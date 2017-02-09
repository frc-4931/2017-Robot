/* Created Sat Jan 07 19:18:14 CST 2017 */
package org.frc4931.robot;

import org.strongback.SwitchReactor;

import org.frc4931.robot.Conveyor.ConveyorShootWhile;
import org.frc4931.robot.Conveyor.Conveyor;
import org.frc4931.robot.Conveyor.ConveyorCollect;
import org.frc4931.robot.Conveyor.ConveyorStop;

import org.strongback.components.Switch;
import org.strongback.control.TalonController;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.function.DoubleSupplier;
import org.frc4931.robot.components.NavXCompass;
import org.frc4931.robot.drive.Drivetrain;
import org.strongback.Strongback;
import org.strongback.components.Compass;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.MecanumDrive;
import org.strongback.function.DoubleToDoubleFunction;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {
    private static final int LEFT_FRONT_MOTOR_CAN_ID = 1;
    private static final int LEFT_REAR_MOTOR_CAN_ID = 2;
    private static final int RIGHT_FRONT_MOTOR_CAN_ID = 3;
    private static final int RIGHT_REAR_MOTOR_CAN_ID = 4;

    private static final int FLIGHT_STICK_PORT = 0;

    private Drivetrain drivetrain;

    private ContinuousRange driveX;
    private ContinuousRange driveY;
    private ContinuousRange driveRotation;
    private boolean relative;

    @Override
    public void robotInit() {
        TalonController ballShooter = Hardware.Controllers.talonController(1, 2.5, 0)
                .setControlMode(TalonController.ControlMode.SPEED).withGains(0, 0, 0);
        Motor conveyorIntake = Hardware.Motors.talonSRX(2);

        FlightStick gamepad1 = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        Conveyor conveyor = new Conveyor(ballShooter, conveyorIntake);
        Switch intake = gamepad1.getButton(3);
        Switch shoot = gamepad1.getButton(5);
        Switch stop = gamepad1.getButton(4);

        Motor leftFrontMotor = Hardware.Controllers.talonController(LEFT_FRONT_MOTOR_CAN_ID, 0.0, 0.0);
        Motor leftRearMotor = Hardware.Controllers.talonController(LEFT_REAR_MOTOR_CAN_ID, 0.0, 0.0);
        Motor rightFrontMotor = Hardware.Controllers.talonController(RIGHT_FRONT_MOTOR_CAN_ID, 0.0, 0.0)
                .invert();
        Motor rightRearMotor = Hardware.Controllers.talonController(RIGHT_REAR_MOTOR_CAN_ID, 0.0, 0.0)
                .invert();
        AHRS navX = new AHRS(SPI.Port.kMXP);
        Compass compass = new NavXCompass(navX);

        MecanumDrive drive = new MecanumDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor, compass);
        drivetrain = new Drivetrain(drive, compass);
        drivetrain.zeroHeading();

        FlightStick flightStick = Hardware.HumanInterfaceDevices.logitechExtreme3D(FLIGHT_STICK_PORT);
        DoubleSupplier throttle = () -> flightStick.getThrottle().read() / -2 + 0.5;
        DoubleToDoubleFunction squarer = (x) -> x * Math.abs(x);
        driveX = flightStick.getRoll()
                .scale(throttle)
                .map(squarer);
        driveY = flightStick.getPitch()
                .scale(throttle)
                .map(squarer);
        driveRotation = flightStick.getYaw()
                .scale(throttle)
                .map(squarer);
        relative = false;
        Strongback.switchReactor().onTriggered(flightStick.getThumb(), () -> relative = !relative);
        Strongback.switchReactor().onTriggered(flightStick.getButton(7), drivetrain::zeroHeading);
        Strongback.switchReactor().onTriggered(intake, () -> new ConveyorCollect(conveyor, intake));
        Strongback.switchReactor().onTriggered(shoot, () -> new ConveyorShootWhile(conveyor, shoot, 1600));
        Strongback.switchReactor().onTriggered(stop, () -> new ConveyorStop(conveyor));
    }

    @Override
    public void teleopInit() {
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
        if (relative) {
            drivetrain.relativeDrive(driveX.read(), driveY.read(), driveRotation.read());
        } else {
            drivetrain.absoluteDrive(driveX.read(), driveY.read(), driveRotation.read());
        }
        SmartDashboard.putNumber("Heading", drivetrain.getHeading());
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }
}
