package org.usfirst.frc.team3309.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * All the sensors on the robot
 * 
 * @author Krager
 *
 */
public class Sensors {
	private static Encoder leftDrive = new Encoder(RobotMap.ENCODERS_A_LEFT_DRIVE_DIGITAL,
			RobotMap.ENCODERS_B_LEFT_DRIVE_DIGITAL, false);
	private static Encoder rightDrive = new Encoder(RobotMap.ENCODERS_A_RIGHT_DRIVE_DIGITAL,
			RobotMap.ENCODERS_B_RIGHT_DRIVE_DIGITAL, false);
	private static Encoder shooterEncoder = new Encoder(RobotMap.ENCODERS_A_SHOOTER_DIGITAL,
			RobotMap.ENCODERS_B_SHOOTER_DIGITAL, true);
	private static Encoder hookEncoder = new Encoder(RobotMap.ENCODERS_A_HOOK_DIGITAL, RobotMap.ENCODERS_B_HOOK_DIGITAL,
			false);
	private static Encoder feedyWheelEncoder = new Encoder(RobotMap.ENCODERS_A_FEEDY_WHEEL_DIGITAL, RobotMap.ENCODERS_B_FEEDY_WHEEL_DIGITAL, false);
	private static Counter hoodEncoder = new Counter(11);
	public static AHRS navX;

	public static void printNavX() {
		/* Display 6-axis Processed Angle Data */
		SmartDashboard.putBoolean("IMU_Connected", Sensors.navX.isConnected());
		SmartDashboard.putBoolean("IMU_IsCalibrating", Sensors.navX.isCalibrating());
		SmartDashboard.putNumber("IMU_Yaw", Sensors.navX.getYaw());
		SmartDashboard.putNumber("IMU_Pitch", Sensors.navX.getPitch());
		SmartDashboard.putNumber("IMU_Roll", Sensors.navX.getRoll());
		SmartDashboard.putNumber("IMU_CompassHeading", Sensors.navX.getCompassHeading());
		SmartDashboard.putNumber("IMU_FusedHeading", Sensors.navX.getFusedHeading());
		SmartDashboard.putNumber("IMU_Accel_X", Sensors.navX.getWorldLinearAccelX());
		SmartDashboard.putNumber("IMU_Accel_Y", Sensors.navX.getWorldLinearAccelY());
		SmartDashboard.putBoolean("IMU_IsMoving", Sensors.navX.isMoving());
		SmartDashboard.putBoolean("IMU_IsRotating", Sensors.navX.isRotating());
	}

	// Drive
	public static double getAngularVel() {
		return navX.getRate();
	}

	public static double getAngle() {
		return navX.getFusedHeading();
	}

	public static void resetDrive() {
		rightDrive.reset();
		leftDrive.reset();
	}

	public static double getRightDrive() {
		return rightDrive.get();
	}

	public static double getRightDriveVel() {
		return rightDrive.getRate();
	}

	public static double getLeftDrive() {
		return leftDrive.get();
	}

	public static double getLeftDriveVel() {
		return leftDrive.getRate();
	}

	// Shooter
	public static double getShooterRPS() {
		return shooterEncoder.getRate();
	}

	public static double getHoodAngle() {
		return hoodEncoder.getDistance();
	}

	// Climber
	public static double getHookAngle() {
		return hookEncoder.getDistance();
	}
	
	public static double getFeedyWheelVel() {
		return feedyWheelEncoder.getDistance();
	}
}