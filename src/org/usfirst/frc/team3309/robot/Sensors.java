package org.usfirst.frc.team3309.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * All the sensors on the robot
 * 
 * @author Krager
 *
 */
public class Sensors {
	private static Encoder leftDrive;
	private static Encoder rightDrive;
	private static Encoder shooterEncoder;
	private static Encoder hookEncoder;
	private static Encoder feedyWheelEncoder;
	private static Encoder intakePivot;
	private static AHRS navX;
	// private static Counter hoodEncoder;

	public static void init() {

	}

	static {
		System.out.println("STARTING INIT");
		rightDrive = new Encoder(RobotMap.ENCODERS_A_RIGHT_DRIVE_DIGITAL, RobotMap.ENCODERS_B_RIGHT_DRIVE_DIGITAL,
				false);
		leftDrive = new Encoder(RobotMap.ENCODERS_A_LEFT_DRIVE_DIGITAL, RobotMap.ENCODERS_B_LEFT_DRIVE_DIGITAL, false);
		// hoodEncoder = new Counter(0);
		intakePivot = new Encoder(RobotMap.ENCODERS_A_INTAKE_PIVOT_DIGITAL, RobotMap.ENCODERS_B_INTAKE_PIVOT_DIGITAL,
				false);
		feedyWheelEncoder = new Encoder(RobotMap.ENCODERS_A_FEEDY_WHEEL_DIGITAL,
				RobotMap.ENCODERS_B_FEEDY_WHEEL_DIGITAL, false);
		hookEncoder = new Encoder(RobotMap.ENCODERS_A_HOOK_DIGITAL, RobotMap.ENCODERS_B_HOOK_DIGITAL, false);
		shooterEncoder = new Encoder(RobotMap.ENCODERS_A_SHOOTER_DIGITAL, RobotMap.ENCODERS_B_SHOOTER_DIGITAL, true);
		navX = new AHRS(SerialPort.Port.kMXP);
		System.out.println("HEY FRIENDS");
	}

	// Drive
	public static double getAngularVel() {
		// return 2.0;
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
		return 7;
	}

	// Climber
	public static double getHookAngle() {
		return hookEncoder.getDistance();
	}

	public static double getFeedyWheelVel() {
		return feedyWheelEncoder.getDistance();
	}

	public static double getIntakePivotAngle() {
		return intakePivot.getDistance();
	}
}