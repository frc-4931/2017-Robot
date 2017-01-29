/* Created Sat Jan 07 19:18:14 CST 2017 */
package org.frc4931.robot;

import org.junit.Test;
import org.strongback.drive.MecanumDrive;
import org.strongback.mock.Mock;
import org.strongback.mock.MockMotor;

public class TestRobot {

    public static void main(String[] args) {
        MockMotor leftFront = Mock.stoppedMotor();
        MockMotor leftRear = Mock.stoppedMotor();
        MockMotor rightFront = Mock.stoppedMotor();
        MockMotor rightRear = Mock.stoppedMotor();
        MecanumDrive drive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear, () -> 0);

        drive.polar(1.0, 0.0, 0.0);
        System.out.println("lf " + leftFront.getSpeed());
        System.out.println("lr " + leftRear.getSpeed());
        System.out.println("rf " + rightFront.getSpeed());
        System.out.println("rr " + rightRear.getSpeed());
    }

    @Test
    public void test() {
        // do something here
    }

}
