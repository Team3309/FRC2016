package org.usfirst.frc.team3309.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * All the sensors on the robot
 * 
 * @author Krager
 *
 */
public class Sensors {
	public static AnalogGyro gyro = new AnalogGyro(RobotMap.GYRO_ANALOG_PORT);
	public static Encoder leftDrive = new Encoder(RobotMap.ENCODERS_A_LEFT_DRIVE_DIGITAL,
			RobotMap.ENCODERS_B_LEFT_DRIVE_DIGITAL, false);
	public static Encoder rightDrive = new Encoder(RobotMap.ENCODERS_A_RIGHT_DRIVE_DIGITAL,
			RobotMap.ENCODERS_B_RIGHT_DRIVE_DIGITAL, false);
	public static Encoder shooterEncoder = new Encoder(RobotMap.ENCODERS_A_SHOOTER_DIGITAL,
			RobotMap.ENCODERS_B_SHOOTER_DIGITAL, true);
	// public static Counter shooterCounter = new
	// Counter(RobotMap.SHOOTER_COUNTER);
	public static AHRS navX;

	public static void printNavX() {
		/* Display 6-axis Processed Angle Data */
		SmartDashboard.putBoolean("IMU_Connected", Sensors.navX.isConnected());
		SmartDashboard.putBoolean("IMU_IsCalibrating", Sensors.navX.isCalibrating());
		SmartDashboard.putNumber("IMU_Yaw", Sensors.navX.getYaw());
		SmartDashboard.putNumber("IMU_Pitch", Sensors.navX.getPitch());
		SmartDashboard.putNumber("IMU_Roll", Sensors.navX.getRoll());
		SmartDashboard.putNumber("IMU_CompassHeading", Sensors.navX.getCompassHeading());

		/*
		 * Display 9-axis Heading (requires magnetometer calibration to be
		 * useful)
		 */
		SmartDashboard.putNumber("IMU_FusedHeading", Sensors.navX.getFusedHeading());

		/*
		 * Display Processed Acceleration Data (Linear Acceleration, Motion
		 * Detect)
		 */

		SmartDashboard.putNumber("IMU_Accel_X", Sensors.navX.getWorldLinearAccelX());
		SmartDashboard.putNumber("IMU_Accel_Y", Sensors.navX.getWorldLinearAccelY());
		SmartDashboard.putBoolean("IMU_IsMoving", Sensors.navX.isMoving());
		SmartDashboard.putBoolean("IMU_IsRotating", Sensors.navX.isRotating());
	}

	public static double getAngularVel() {
		return gyro.getRate();
	}

	public static double getAngle() {
		return gyro.getAngle();
	}
}