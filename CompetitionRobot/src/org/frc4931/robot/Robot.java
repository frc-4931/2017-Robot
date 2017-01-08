/* Created Sat Jan 07 19:18:14 CST 2017 */
package org.frc4931.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.frc4931.robot.drive.Drivetrain;
import org.strongback.Strongback;
import org.strongback.components.AngleSensor;
import org.strongback.components.Switch;
import org.strongback.components.TalonSRX;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.DirectionalAxis;
import org.strongback.components.ui.FlightStick;
import org.strongback.control.TalonController;
import org.strongback.drive.MecanumDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {
    private static final int LEFT_FRONT_MOTOR_CAN_ID = 1;
    private static final int LEFT_REAR_MOTOR_CAN_ID = 2;
    private static final int RIGHT_FRONT_MOTOR_CAN_ID = 3;
    private static final int RIGHT_REAR_MOTOR_CAN_ID = 4;

    private static final int FLIGHT_STICK_PORT = 0;

    /* Stages:
     *
     * 4 pul : 360 deg CTRE Magnetic Encoder
     * 7 : 1 VersaPlanetary Gearbox
     */
    private static final double DRIVE_PULSES_PER_DEGREE = 0.01111111111111111;
    private static final double DRIVE_P_GAIN = 0.0;
    private static final double DRIVE_I_GAIN = 0.0;
    private static final double DRIVE_D_GAIN = 0.0;

    private Drivetrain drivetrain;

    private ContinuousRange driveX;
    private ContinuousRange driveY;
    private ContinuousRange driveRotation;
    private Switch relativeEnable;
    private DirectionalAxis trimPot;

    @Override
    public void robotInit() {
        TalonController leftFrontMotor = Hardware.Controllers.talonController(LEFT_FRONT_MOTOR_CAN_ID, DRIVE_PULSES_PER_DEGREE, 0.0);
        TalonController leftRearMotor = Hardware.Controllers.talonController(LEFT_REAR_MOTOR_CAN_ID, DRIVE_PULSES_PER_DEGREE, 0.0);
        TalonController rightFrontMotor = Hardware.Controllers.talonController(RIGHT_FRONT_MOTOR_CAN_ID, DRIVE_PULSES_PER_DEGREE, 0.0);
        TalonController rightRearMotor = Hardware.Controllers.talonController(RIGHT_REAR_MOTOR_CAN_ID, DRIVE_PULSES_PER_DEGREE, 0.0);
        AngleSensor gyro = AngleSensor.create(() -> 0.0);

        leftFrontMotor.setFeedbackDevice(TalonSRX.FeedbackDevice.MAGNETIC_ENCODER_RELATIVE)
                .withGains(DRIVE_P_GAIN, DRIVE_I_GAIN, DRIVE_D_GAIN)
                .setControlMode(TalonController.ControlMode.SPEED);
        leftRearMotor.setFeedbackDevice(TalonSRX.FeedbackDevice.MAGNETIC_ENCODER_RELATIVE)
                .withGains(DRIVE_P_GAIN, DRIVE_I_GAIN, DRIVE_D_GAIN)
                .setControlMode(TalonController.ControlMode.SPEED);
        rightFrontMotor.setFeedbackDevice(TalonSRX.FeedbackDevice.MAGNETIC_ENCODER_RELATIVE)
                .withGains(DRIVE_P_GAIN, DRIVE_I_GAIN, DRIVE_D_GAIN)
                .setControlMode(TalonController.ControlMode.SPEED);
        rightRearMotor.setFeedbackDevice(TalonSRX.FeedbackDevice.MAGNETIC_ENCODER_RELATIVE)
                .withGains(DRIVE_P_GAIN, DRIVE_I_GAIN, DRIVE_D_GAIN)
                .setControlMode(TalonController.ControlMode.SPEED);

        MecanumDrive drive = new MecanumDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor, gyro);
        drivetrain = new Drivetrain(drive);

        FlightStick flightStick = Hardware.HumanInterfaceDevices.logitechExtreme3D(FLIGHT_STICK_PORT);
        driveX = flightStick.getRoll();
        driveY = flightStick.getPitch();
        driveRotation = flightStick.getYaw();
        relativeEnable = flightStick.getThumb();
        trimPot = flightStick.getDPad(0);
    }

    @Override
    public void teleopInit() {
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
        if (trimPot.getDirection() != -1) {
            drivetrain.trimDrive(trimPot.getDirection());
        } else if (relativeEnable.isTriggered()) {
            drivetrain.relativeDrive(driveX.read(), driveY.read(), driveRotation.read());
        } else {
            drivetrain.absoluteDrive(driveX.read(), driveY.read(), driveRotation.read());
        }
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }

}
