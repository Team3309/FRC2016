package org.usfirst.frc.team3309.robot;

/**
 * Map to tell ports of the wiring
 * 
 * @author TheMkrage
 *
 */
public class RobotMap {
	// Motor Controllers
	// Drive
	public static final int RIGHT_DRIVE = 1;
	public static final int LEFT_DRIVE = 0;

	public static final int LIGHT = 7;

	// Shooter
	public static final int LEFT_SHOOTER_MOTOR = 4;
	public static final int RIGHT_SHOOTER_MOTOR = 9;
	public static final int HOOD_MOTOR = 6;

	public static final int INTAKE_SIDE_MOTOR = 7;
	public static final int INTAKE_FRONT_MOTOR = 8;
	public static final int CLIMBER_HOOK_MOTOR = 18;

	public static final int FEEDY_WHEEL_MOTOR = 5;
	public static final int CLIMBER_PIVOT_MOTOR = 19;

	public static final int INTAKE_PIVOT_ID = 0;
	public static final int CLIMBER_CARRIAGE_ID = 1;

	// Sensors
	public static final int ENCODERS_A_HOOK_DIGITAL = 15;
	public static final int ENCODERS_B_HOOK_DIGITAL = 6;
	public static final int ENCODERS_A_FEEDY_WHEEL_DIGITAL = 14;
	public static final int ENCODERS_B_FEEDY_WHEEL_DIGITAL = 13;
	public static final int ENCODERS_A_INTAKE_PIVOT_DIGITAL = 0;
	public static final int ENCODERS_B_INTAKE_PIVOT_DIGITAL = 1;
	public static final int ENCODERS_A_RIGHT_DRIVE_DIGITAL = 4;
	public static final int ENCODERS_B_RIGHT_DRIVE_DIGITAL = 5;
	public static final int ENCODERS_A_LEFT_DRIVE_DIGITAL = 2;
	public static final int ENCODERS_B_LEFT_DRIVE_DIGITAL = 3;
	public static final int SHOOTER_OPTICAL_SENSOR = 8;
	public static final int ENCODERS_B_SHOOTER_DIGITAL = 12;
	public static final int HOOD_ABS = 9;
	public static final int SHOOTER_COUNTER = 6;

}
