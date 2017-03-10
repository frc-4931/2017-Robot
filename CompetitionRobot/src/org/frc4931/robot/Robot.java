/* Created Sat Jan 07 19:18:14 CST 2017 */
package org.frc4931.robot;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc4931.robot.Conveyor.Conveyor;
import org.frc4931.robot.Conveyor.ConveyorCollect;
import org.frc4931.robot.Conveyor.ConveyorEjectWhile;
import org.frc4931.robot.Conveyor.ConveyorShootWhile;
import org.frc4931.robot.auto.*;
import org.frc4931.robot.climber.ClimbDownWhile;
import org.frc4931.robot.climber.ClimbUpWhile;
import org.frc4931.robot.climber.ClimberSubSystem;
import org.frc4931.robot.components.HardwarePixy;
import org.frc4931.robot.components.NavXCompass;
import org.frc4931.robot.drive.Drivetrain;
import org.frc4931.robot.drive.TurnTo;
import org.frc4931.robot.vision.VisionSystem;
import org.strongback.Executor;
import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.components.*;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.control.TalonController;
import org.strongback.drive.MecanumDrive;
import org.strongback.function.DoubleToDoubleFunction;
import org.strongback.hardware.Hardware;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class Robot extends IterativeRobot {
    private static final int LEFT_FRONT_MOTOR_CAN_ID = 1;
    private static final int LEFT_REAR_MOTOR_CAN_ID = 2;
    private static final int RIGHT_FRONT_MOTOR_CAN_ID = 3;
    private static final int RIGHT_REAR_MOTOR_CAN_ID = 4;
    private static final int CONVEYOR_SHOOTER_MOTOR_CAN_ID = 5;

    private static final int LEFT_CLIMBER_MOTOR_PWM_PORT = 0;
    private static final int RIGHT_CLIMBER_MOTOR_PWM_PORT  = 1;
    private static final int CONVEYOR_INTAKE_MOTOR_PWM_PORT = 2;
    private static final int CONVEYOR_SWEEPER_MOTOR_PWM_PORT = 3;

    private static final int CLIMBER_HORIZONTAL_SWITCH_DIO_PORT = 0;
    private static final int CLIMBER_VERTICAL_SWITCH_DIO_PORT = 1;
    private static final int CLIMBER_SCORE_SWITCH_DIO_PORT = 2;

    private static final int FRONT_ULTRASONIC_A_ANALOG_PORT = 0;
    private static final int FRONT_ULTRASONIC_B_ANALOG_PORT = 1;
    private static final int REAR_ULTRASONIC_ANALOG_PORT = 2;

    private static final int FLIGHT_STICK_PORT = 0;

    private Drivetrain drivetrain;
    private Conveyor conveyor;
    private ClimberSubSystem climber;
    private VisionSystem visionSystem;

    private ContinuousRange driveX;
    private ContinuousRange driveY;
    private ContinuousRange driveRotation;
    private SendableChooser<Supplier<Command>> autoChooser;

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
        DistanceSensor frontDistanceSensorA = Hardware.DistanceSensors.analogUltrasonic(FRONT_ULTRASONIC_A_ANALOG_PORT, 102.4);
        DistanceSensor frontDistanceSensorB = Hardware.DistanceSensors.analogUltrasonic(FRONT_ULTRASONIC_B_ANALOG_PORT, 102.4);
        DistanceSensor rearDistanceSensor = Hardware.DistanceSensors.analogUltrasonic(REAR_ULTRASONIC_ANALOG_PORT, 102.4);
//        DistanceSensor frontDistanceSensor = DistanceSensor.create(
//                () -> Math.min(frontDistanceSensorA.getDistanceInInches(), frontDistanceSensorB.getDistanceInInches())
//        );
        DistanceSensor frontDistanceSensor = frontDistanceSensorB;
        MecanumDrive drive = new MecanumDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor, compass);
        drivetrain = new Drivetrain(drive, compass, frontDistanceSensor, rearDistanceSensor);
        drivetrain.zeroHeading();

        CANTalon shooterMotor = new CANTalon(CONVEYOR_SHOOTER_MOTOR_CAN_ID);
        shooterMotor.configEncoderCodesPerRev(1024);
        TalonController ballShooter = Hardware.Controllers.talonController(shooterMotor, 1.0, 0.0)
                .setFeedbackDevice(TalonSRX.FeedbackDevice.QUADRATURE_ENCODER)
                .setControlMode(TalonController.ControlMode.SPEED)
                .withGains(0.009038, 0.0, 0.0);
        Motor conveyorIntake = Hardware.Motors.victorSP(CONVEYOR_INTAKE_MOTOR_PWM_PORT);
        Motor conveyorSweeper = Hardware.Motors.victorSP(CONVEYOR_SWEEPER_MOTOR_PWM_PORT)
                .invert();
        conveyor = new Conveyor(ballShooter, conveyorIntake, conveyorSweeper, shooterMotor::getSpeed);

        Motor leftClimberMotor = Hardware.Motors.talon(LEFT_CLIMBER_MOTOR_PWM_PORT)
                .invert();
        Motor rightClimberMotor = Hardware.Motors.talon(RIGHT_CLIMBER_MOTOR_PWM_PORT);
        Motor climberMotor = Motor.compose(leftClimberMotor, rightClimberMotor);
        Switch paddleHorizontal = () -> false; //Hardware.Switches.normallyClosed(CLIMBER_HORIZONTAL_SWITCH_DIO_PORT);
        Switch paddleVertical = () -> false; //Hardware.Switches.normallyClosed(CLIMBER_VERTICAL_SWITCH_DIO_PORT);
        Switch climbScore = () -> false; //Hardware.Switches.normallyOpen(CLIMBER_SCORE_SWITCH_DIO_PORT);
        climber = new ClimberSubSystem(climberMotor, paddleHorizontal, paddleVertical, climbScore);

        HardwarePixy pixy = new HardwarePixy(SPI.Port.kOnboardCS0);
        visionSystem = new VisionSystem(pixy, 0.0);
        Strongback.executor().register((millis) -> pixy.sync(), Executor.Priority.MEDIUM);
        Strongback.executor().register((millis) -> visionSystem.update(), Executor.Priority.MEDIUM);

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
        Switch relativeToggle = flightStick.getThumb();
        Switch zeroHeading = flightStick.getButton(7);
        Switch intake = flightStick.getButton(3);
        Switch shoot = flightStick.getButton(5);
        Switch climbUp = flightStick.getButton(6);
        Switch climbDown = flightStick.getButton(4);
        Switch eject = flightStick.getButton(12);

        Strongback.switchReactor().onTriggered(relativeToggle, drivetrain::toggleRelativeEnabled);
        Strongback.switchReactor().onTriggered(zeroHeading, drivetrain::zeroHeading);
        Strongback.switchReactor().onTriggeredSubmit(intake, () -> new ConveyorCollect(conveyor, intake));
        Strongback.switchReactor().onTriggeredSubmit(shoot, () -> new ConveyorShootWhile(conveyor, shoot, 4800));
        Strongback.switchReactor().onTriggeredSubmit(climbUp, () -> new ClimbUpWhile(climber, climbUp));
        Strongback.switchReactor().onTriggeredSubmit(climbDown, () -> new ClimbDownWhile(climber, climbDown));
        Strongback.switchReactor().onTriggeredSubmit(eject, () -> new ConveyorEjectWhile(conveyor, eject));

        autoChooser = new SendableChooser<>();
        autoChooser.addDefault("Do nothing", AutoNoOp::new);
        autoChooser.addObject("Drive forward", () -> new AutoDrive(drivetrain));
//        autoChooser.addObject("Left gear", () -> new AutoGearLeft(drivetrain));
        autoChooser.addObject("Center gear", () -> new AutoGearCenter(drivetrain));
//        autoChooser.addObject("Right gear", () -> new AutoGearRight(drivetrain));
//        autoChooser.addObject("Shoot", () -> new AutoShoot(drivetrain, visionSystem, conveyor));
        SmartDashboard.putData("Auto Mode", autoChooser);
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putBoolean("Relative Enabled", drivetrain.isRelativeEnabled());
        SmartDashboard.putNumber("Heading", drivetrain.getHeading());
        SmartDashboard.putNumber("Shooter Speed", conveyor.getShooterSpeed());
//        SmartDashboard.putNumber("Forward Distance", visionSystem.getForwardDistanceToLift());
//        SmartDashboard.putNumber("Lateral Distance", visionSystem.getLateralDistanceToLift());
        SmartDashboard.putNumber("Front distance", drivetrain.getFrontDistance());
        SmartDashboard.putNumber("Rear distance", drivetrain.getRearDistance());
    }

    @Override
    public void teleopInit() {
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.drive(driveX.read(), driveY.read(), driveRotation.read());
    }

    @Override
    public void autonomousInit() {
        Strongback.start();
        drivetrain.zeroHeading(); // Robot should always start with its back against the alliance wall

        Strongback.submit(autoChooser.getSelected().get());
    }

    @Override
    public void autonomousPeriodic() {
        
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }
}
