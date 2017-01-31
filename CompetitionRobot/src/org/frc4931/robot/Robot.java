/* Created Sat Jan 07 19:18:14 CST 2017 */
package org.frc4931.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.function.DoubleSupplier;
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

    @Override
    public void robotInit() {
        Motor leftFrontMotor = Hardware.Controllers.talonController(LEFT_FRONT_MOTOR_CAN_ID, 0.0, 0.0);
        Motor leftRearMotor = Hardware.Controllers.talonController(LEFT_REAR_MOTOR_CAN_ID, 0.0, 0.0);
        Motor rightFrontMotor = Hardware.Controllers.talonController(RIGHT_FRONT_MOTOR_CAN_ID, 0.0, 0.0)
                .invert();
        Motor rightRearMotor = Hardware.Controllers.talonController(RIGHT_REAR_MOTOR_CAN_ID, 0.0, 0.0)
                .invert();
        Compass compass = Compass.create(() -> 0);

        MecanumDrive drive = new MecanumDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor, compass);
        drivetrain = new Drivetrain(drive, compass);

        FlightStick flightStick = Hardware.HumanInterfaceDevices.logitechExtreme3D(FLIGHT_STICK_PORT);
        DoubleSupplier throttle = () -> flightStick.getThrottle().read() / -2 + 0.5;
        DoubleToDoubleFunction squarer = (x) -> x * Math.abs(x);
        driveX = flightStick.getRoll().scale(throttle).map(squarer);
        driveY = flightStick.getPitch().scale(throttle).map(squarer);
        driveRotation = flightStick.getYaw().scale(throttle).map(squarer);
    }

    @Override
    public void teleopInit() {
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.absoluteDrive(driveX.read(), driveY.read(), driveRotation.read());
        SmartDashboard.putNumber("Heading", drivetrain.getHeading());
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

}
