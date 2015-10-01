package org.usfirst.frc.team3309.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;

/**
 * All the sensors on the robot
 * @author TheMkrage
 *
 */
public class Sensors {
	public static Gyro gyro = new Gyro(RobotMap.GYRO_ANALOG_PORT);
	public static Encoder leftDrive = new Encoder(RobotMap.ENCODERS_A_LEFT_DRIVE_DIGITAL, RobotMap.ENCODERS_B_LEFT_DRIVE_DIGITAL, false);
	public static Encoder rightDrive = new Encoder(RobotMap.ENCODERS_A_RIGHT_DRIVE_DIGITAL, RobotMap.ENCODERS_B_RIGHT_DRIVE_DIGITAL, false);
	
	public static double getAngularVel() {
		return gyro.getRate();
	}
	
	public static double getAngle() {
		return gyro.getAngle();
	}
}