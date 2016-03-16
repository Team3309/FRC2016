package org.usfirst.frc.team3309.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * All the sensors on the robot
 * 
 * @author Krager
 *
 */
public class Sensors {
	public static Encoder leftDrive;
	public static Encoder rightDrive;
	private static Counter shooterEncoder;
	private static Encoder hookEncoder;
	private static Counter hoodEncoder;
	private static Encoder feedyWheelEncoder;
	private static Encoder intakePivot;
	private static AHRS navX;
	private static double pastShooter = 0.0;
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
		shooterEncoder = new Counter(RobotMap.SHOOTER_OPTICAL_SENSOR);

		navX = new AHRS(SerialPort.Port.kMXP);
		hoodEncoder = new Counter(new DigitalInput(RobotMap.HOOD_ABS));
		// Counter.Mode.kPulseLengt
		hoodEncoder.setSemiPeriodMode(true);
		hoodEncoder.setReverseDirection(false);
		hoodEncoder.reset();

		// hoodEncoder.
		System.out.println("HEY FRIENDS");
	}

	// Drive
	public static double getAngularVel() {
		// return 2.0;
		return navX.getRate();
	}

	public static double getAngle() {
		// System.out.println(navX.isMagnetometerCalibrated());
		return navX.getYaw();
	}

	public static void resetDrive() {
		rightDrive.reset();
		leftDrive.reset();
	}

	public static double getRightDrive() {
		return rightDrive.get();
	}

	public static double getRightDriveVel() {
		return rightDrive.getRate()/100;
	}

	public static double getLeftDrive() {
		return leftDrive.get();
	}

	public static double getLeftDriveVel() {
		return leftDrive.getRate()/100;
	}

	// Shooter
	public static double getShooterRPS() {
		double currentShooter = (1 / shooterEncoder.getPeriod());
		System.out.println("FDASF " + shooterEncoder.getPeriod());
		if (Math.abs(1 / shooterEncoder.getPeriod()) - (pastShooter) > 250)
			return pastShooter;
		pastShooter = currentShooter;
		return currentShooter;
	}

	public static double getHoodAngle() {
		double hoodAngle = Constants.getHoodBottomValue()
				- ((1000000.0 * (hoodEncoder.getPeriod())) * (360.0 / 4096.0));
		// double hoodAngle = (-1 * ((1000000.0 * (hoodEncoder.getPeriod())) *
		// (360.0 / 4096.0)));

		//System.out.println("HOOD ANGLE: " + hoodAngle);
		while (hoodAngle > 360 || hoodAngle < -20) {
			
			if (hoodAngle > 360) {
				hoodAngle -= 360;
			}
			if (hoodAngle < -20) {
				hoodAngle += 360;
			}
		}

		return hoodAngle;
	}

	// Climber
	public static double getHookAngle() {
		return hookEncoder.getDistance();
	}

	public static double getFeedyWheelVel() {
		return feedyWheelEncoder.getDistance();
	}

}