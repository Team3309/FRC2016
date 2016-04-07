
package org.usfirst.frc.team3309.robot;

/**
 * Map to tell ports of the wiring
 * 
 * @author TheMkrage
 *
 */
public class RobotMap {
	// Motor Controllers (PWM)
	public static final int LEFT_DRIVE = 0;
	public static final int RIGHT_DRIVE = 1;

	public static final int EXTRA_CONTROLLER = 3;
	public static final int LEFT_SHOOTER_MOTOR = 4;
	public static final int FEEDY_WHEEL_MOTOR = 5;
	public static final int HOOD_MOTOR = 6;

	public static final int RIGHT_SHOOTER_MOTOR = 9;

	// CAN
	public static final int INTAKE_PIVOT_ID = 0;

	// DIO
	public static final int ENCODERS_B_LEFT_DRIVE_DIGITAL = 2;
	public static final int ENCODERS_A_LEFT_DRIVE_DIGITAL = 3;
	public static final int ENCODERS_A_RIGHT_DRIVE_DIGITAL = 4;
	public static final int ENCODERS_B_RIGHT_DRIVE_DIGITAL = 5;
	public static final int SHOOTER_OPTICAL_SENSOR = 6;
	public static final int LIGHT = 7;
	public static final int HOOD_ABS = 9;

	// AnalogOut
	public static final int FLEX_SENSORS_ANALOG = 2;

	// Manifold
	public static final int SHIFTER = 0;
	public static final int CLIMBER_LATCHES = 1;
	public static final int CLIMBER_LEFT = 2;
	public static final int CLIMBER_RIGHT = 3;
}
