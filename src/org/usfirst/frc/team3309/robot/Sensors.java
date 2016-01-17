package org.usfirst.frc.team3309.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;

/**
 * All the sensors on the robot
 * 
 * @author Krager
 *
 */
public class Sensors {
	public static Gyro gyro = new Gyro(RobotMap.GYRO_ANALOG_PORT);
	public static Encoder leftDrive = new Encoder(RobotMap.ENCODERS_A_LEFT_DRIVE_DIGITAL,
			RobotMap.ENCODERS_B_LEFT_DRIVE_DIGITAL, false);
	public static Encoder rightDrive = new Encoder(RobotMap.ENCODERS_A_RIGHT_DRIVE_DIGITAL,
			RobotMap.ENCODERS_B_RIGHT_DRIVE_DIGITAL, false);
	//public static Counter shooterCounter = new Counter(RobotMap.SHOOTER_COUNTER);
	//public static AHRS navX;

	public static double getAngularVel() {
		return gyro.getRate();
	}

	public static double getAngle() {
		return gyro.getAngle();
	}
}