/* Created Sat Jan 07 19:18:14 CST 2017 */
package org.frc4931.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc4931.robot.Conveyor.Conveyor;
import org.frc4931.robot.Conveyor.ConveyorCollect;
import org.frc4931.robot.Conveyor.ConveyorShootWhile;
import org.frc4931.robot.climber.ClimbDownWhile;
import org.frc4931.robot.climber.ClimbUpWhile;
import org.frc4931.robot.climber.ClimberSubSystem;
import org.frc4931.robot.components.NavXCompass;
import org.frc4931.robot.drive.Drivetrain;
import org.strongback.Strongback;
import org.strongback.components.Compass;
import org.strongback.components.Motor;
import org.strongback.components.Switch;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.control.TalonController;
import org.strongback.drive.MecanumDrive;
import org.strongback.function.DoubleToDoubleFunction;
import org.strongback.hardware.Hardware;

import java.util.function.DoubleSupplier;

public class Robot extends IterativeRobot {
    private static final int LEFT_FRONT_MOTOR_CAN_ID = 1;
    private static final int LEFT_REAR_MOTOR_CAN_ID = 2;
    private static final int RIGHT_FRONT_MOTOR_CAN_ID = 3;
    private static final int RIGHT_REAR_MOTOR_CAN_ID = 4;
    private static final int CONVEYOR_SHOOTER_MOTOR_CAN_ID = 5;

    private static final int LEFT_CLIMBER_MOTOR_PWM_PORT = 0;
    private static final int RIGHT_CLIMBER_MOTOR_PWM_PORT  = 1;
    private static final int CONVEYOR_INTAKE_MOTOR_PWM_PORT = 2;

    private static final int CLIMBER_HORIZONTAL_SWITCH_DIO_PORT = 0;
    private static final int CLIMBER_VERTICAL_SWITCH_DIO_PORT = 1;
    private static final int CLIMBER_SCORE_SWITCH_DIO_PORT = 2;

    private static final int FLIGHT_STICK_PORT = 0;

    private Drivetrain drivetrain;

    private ContinuousRange driveX;
    private ContinuousRange driveY;
    private ContinuousRange driveRotation;
    private boolean relative;

    @Override
    public void robotInit() {
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

        TalonController ballShooter = Hardware.Controllers.talonController(CONVEYOR_SHOOTER_MOTOR_CAN_ID, 0, 0)
                .setControlMode(TalonController.ControlMode.SPEED).withGains(0, 0, 0);
        Motor conveyorIntake = Hardware.Motors.victorSP(CONVEYOR_INTAKE_MOTOR_PWM_PORT);

        Conveyor conveyor = new Conveyor(ballShooter, conveyorIntake);

        Motor leftClimberMotor = Hardware.Motors.talon(LEFT_CLIMBER_MOTOR_PWM_PORT)
                .invert();
        Motor rightClimberMotor = Hardware.Motors.talon(RIGHT_CLIMBER_MOTOR_PWM_PORT);
        Motor climberMotor = Motor.compose(leftClimberMotor, rightClimberMotor);
        Switch paddleHorizontal = () -> false; //Hardware.Switches.normallyClosed(CLIMBER_HORIZONTAL_SWITCH_DIO_PORT);
        Switch paddleVertical = () -> false; //Hardware.Switches.normallyClosed(CLIMBER_VERTICAL_SWITCH_DIO_PORT);
        Switch climbScore = () -> false; //Hardware.Switches.normallyOpen(CLIMBER_SCORE_SWITCH_DIO_PORT);

        ClimberSubSystem climber = new ClimberSubSystem(climberMotor, paddleHorizontal, paddleVertical, climbScore);

        FlightStick flightStick = Hardware.HumanInterfaceDevices.logitechExtreme3D(FLIGHT_STICK_PORT);
        DoubleSupplier throttle = () -> flightStick.getThrottle().read() / -2 + 0.5;
        DoubleToDoubleFunction squarer = (x) -> x * Math.abs(x);
        driveX = flightStick.getRoll()
                .scale(throttle)
                .map(squarer);
        driveY = flightStick.getPitch()
                .invert()
                .scale(throttle)
                .map(squarer);
        driveRotation = flightStick.getYaw()
                .invert()
                .scale(throttle)
                .map(squarer);
        relative = false;
        Switch relativeToggle = flightStick.getThumb();
        Switch zeroHeading = flightStick.getButton(7);
        Switch intake = flightStick.getButton(3);
        Switch shoot = flightStick.getButton(5);
        Switch climbUp = flightStick.getButton(6);
        Switch climbDown = flightStick.getButton(4);

        Strongback.switchReactor().onTriggered(relativeToggle, () -> relative = !relative);
        Strongback.switchReactor().onTriggered(zeroHeading, drivetrain::zeroHeading);
        Strongback.switchReactor().onTriggeredSubmit(intake, () -> new ConveyorCollect(conveyor, intake));
        Strongback.switchReactor().onTriggeredSubmit(shoot, () -> new ConveyorShootWhile(conveyor, shoot, 1600));
        Strongback.switchReactor().onTriggeredSubmit(climbUp, () -> new ClimbUpWhile(climber, climbUp));
        Strongback.switchReactor().onTriggeredSubmit(climbDown, () -> new ClimbDownWhile(climber, climbUp));
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

        SmartDashboard.putBoolean("Relative Enabled", relative);
        SmartDashboard.putNumber("Heading", drivetrain.getHeading());
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }
}
