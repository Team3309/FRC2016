package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.drive.DriveAngleController;
import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersController;
import org.team3309.lib.controllers.drive.FaceVisionTargetController;
import org.team3309.lib.controllers.drive.equations.DriveCheezyDriveEquation;
import org.team3309.lib.controllers.generic.BlankController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.SensorDoesNotReturnException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drive Train
 * 
 * @author Krager
 *
 */
public class Drive extends ControlledSubsystem {
	/**
	 * Used to give a certain gap that the drive would be ok with being within
	 * its goal encoder averageÃ�.
	 */
	private static final double DRIVE_ENCODER_LENIENCY = 9;

	/**
	 * Used to give a certain gap that the drive would be ok with being within
	 * its goal angle
	 */
	private static final double DRIVE_GYRO_LENIENCY = .5;

	private int LOOP_TIME = 20;

	private static Drive instance;
	// Actuators
	private Spark left = new Spark(RobotMap.LEFT_DRIVE);
	private Spark right = new Spark(RobotMap.RIGHT_DRIVE);
	private Solenoid sol = new Solenoid(RobotMap.SHIFTER);

	private Shot desiredShot;
	private boolean isLowGear = true;

	public boolean lowGearInAuto = false;
	boolean isReset = false;

	private static boolean isLocked = false;

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Drive getInstance() {
		if (instance == null && !isLocked) {
			isLocked = true;
			instance = new Drive("Drive");
		}
		return instance;
	}

	private Drive(String name) {
		super(name);

		this.print("What ");
		this.setTeleopController(new DriveCheezyDriveEquation(this));
		this.setAutoController(new BlankController(this));
	}

	@Override
	public void initTeleop() {
		if (autoController.thread != null) {
			autoController.thread.stop();
		}
		this.setTeleopController(new DriveCheezyDriveEquation(Drive.getInstance()));
		isReset = false;

	}

	@Override
	public void initAuto() {
		if (teleopController.thread != null) {
			teleopController.thread.stop();
		}
		isLowGear = false;
		this.setAutoController(new BlankController(Drive.getInstance()));
	}

	public void toVision() {
		this.desiredShot = Vision.getInstance().getShotToAimTowards();
		// if (this.desiredShot != null) {
		// revert this poop
		if (true) {

			double offset = SmartDashboard.getNumber("ANGLE I AM TURNING ( ADDED TO OTHER)");
			// mDrive.setHighGear(true);
			DriveAngleController angleVel = new DriveAngleController(Drive.getInstance(), this.getAngle() + offset);
			angleVel.setCompletable(false);
			FaceVisionTargetController x = new FaceVisionTargetController();
			x.setName("VISION");
			x.reset();
			x.setCompletable(false);

			// System.out.println("Vision started");
			if (DriverStation.getInstance().isAutonomous())
				this.setAutoController(x);
			else
				this.setTeleopController(x);
			isReset = true;
		}
	}

	@Override
	public void updateTeleop() {
		if (Controls.operatorController.getBack() && !isReset) {
			this.desiredShot = Vision.getInstance().getShotToAimTowards();
			Vision.getInstance().setLight(Vision.getInstance().BRIGHTNESS);
			if (this.desiredShot != null && !isReset) {
				// System.out.println("ACQUIRE NEW ANGLE");
				toVision();
				Controls.driverController.setRumble(1);
			} else {
				// System.out.println("Vision does not see anything");
				isReset = false;
			}
		} else if (Controls.operatorController.getBack()) {

		} else if (Controls.operatorController.getLB() && !isReset) {
			DriveAngleVelocityController driveAngleHardCore = new DriveAngleVelocityController(this, this.getAngle());
			driveAngleHardCore.setCompletable(false);
			driveAngleHardCore.turningController.setConstants(6, 0, 16);
			this.setTeleopController(driveAngleHardCore);
			isReset = true;
		} else if (Controls.operatorController.getLB()) {

		} else {
			isReset = false;
			Vision.getInstance().setLight(Vision.getInstance().BRIGHTNESS);
			this.setTeleopController(new DriveCheezyDriveEquation(this));
			Controls.driverController.setRumble(0);
		}
		
		Vision.getInstance().setLight(Vision.getInstance().BRIGHTNESS);

		if (Controls.driverController.getLB()) {
			isLowGear = true;
			sol.set(false);
		} else {
			isLowGear = false;
			sol.set(true);
		}
		if (teleopController.isCompleted() && !DriverStation.getInstance().isAutonomous()) {
			this.setTeleopController(new DriveCheezyDriveEquation(this));
			
		}
		OutputSignal output = teleopController.getOutputSignal();
		System.out.println("POWER: " + output);
		setLeftRight(output.getLeftMotor(), output.getRightMotor());
	}

	public void updateAuto() {
		// System.out.println("DRIVE ANDLE: " + this.getAngle());
		OutputSignal output = autoController.getOutputSignal();
		setLeftRight(output.getLeftMotor(), output.getRightMotor());
	}

	public void setHighGear(boolean bool) {
		if (bool) {
			isLowGear = false;
		} else {
			isLowGear = true;
		}
		sol.set(bool);
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setAngularPos(Sensors.getAngle());
		input.setAngularVel(Sensors.getAngularVel());
		// Check and send off leftSide
		try {
			input.setLeftPos(Sensors.getLeftDrive());
			input.setLeftVel(Sensors.getLeftDriveVel());
		} catch (Exception e) {
			// Way to show that the value is bad
			input.setLeftPos(Double.MIN_VALUE);
			input.setLeftVel(Double.MIN_VALUE);
		}
		// Check and send off rightSide
		try {
			input.setRightVel(Sensors.getRightDriveVel());
			input.setRightPos(Sensors.getRightDrive());
		} catch (Exception e) {
			// Way to show that the value is bad
			input.setRightVel(Double.MIN_VALUE);
			input.setRightPos(Double.MIN_VALUE);
		}
		return input;
	}

	/**
	 * Creates and runs a controller that gets the drive to the given setpoint
	 * 
	 * @param encoders
	 *            goal encoder values
	 */

	public void setSetpoint(double encoders) {
		this.setTeleopController(new DriveEncodersController(Drive.getInstance(), encoders));
	}

	public void setAngleSetpoint(double goalAngle) {
		this.setTeleopController(new DriveAngleController(Drive.getInstance(), goalAngle));
		teleopController.reset();
		teleopController.setName("Drive Angle Controller");
	}

	/**
	 * Returns the Angle the robot is at
	 * 
	 * @return Angle robot is at
	 */
	public double getAngle() {
		return Sensors.getAngle();
	}

	/**
	 * Returns the average of the two encoders to see the ditstance traveled
	 * 
	 * @return the average of the left and right to get the distance traveled
	 */
	public double getDistanceTraveled() throws SensorDoesNotReturnException {
		return (Math.abs(Sensors.getLeftDrive()) + Math.abs(Sensors.getRightDrive())) / 2;
	}

	public double getLeftPower() {
		return this.left.get();
	}

	public double getRightPower() {
		return this.right.get();
	}

	/**
	 * returns if the current average of encoders (aka distance traveled) is
	 * close to the encoderGoal. Uses DRIVE_ENCODER_LENIENCY to tell if it is
	 * close.
	 * 
	 * @param encoderGoal
	 *            Encoder drive should be at
	 * @return
	 */
	public boolean isEncoderCloseTo(double encoderGoal) {
		double factor = 1;
		if (encoderGoal < 0) {
			factor = -1;
		}
		try {
			if (getDistanceTraveled() * factor < encoderGoal + DRIVE_ENCODER_LENIENCY
					&& factor * getDistanceTraveled() > encoderGoal - DRIVE_ENCODER_LENIENCY) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * returns if the current angle is close to the angleGoal. Uses
	 * DRIVE_ANGLE_LENIENCY to tell if it is close.
	 * 
	 * @param angleGoal
	 *            Angle drive should be at
	 * @return
	 */
	public boolean isAngleCloseTo(double angleGoal) {
		try {
			if (getAngle() < angleGoal + DRIVE_GYRO_LENIENCY
					&& getDistanceTraveled() > angleGoal - DRIVE_GYRO_LENIENCY) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Stops current running controller and sets motors to zero
	 */
	public void stopDrive() {
		this.setAutoController( new BlankController(Drive.getInstance()));
		setLeftRight(0, 0);
	}

	/**
	 * Sets motors left then right
	 * 
	 * @param left
	 *            leftMotorSpeed
	 * @param right
	 *            rightMotorSpeed
	 */
	public void setLeftRight(double left, double right) {
		setRightLeft(right, left);
	}

	/**
	 * Sets motors right then left
	 * 
	 * @param right
	 *            rightMotorSpeed
	 * @param left
	 *            leftMotorSpeed
	 */
	public void setRightLeft(double right, double left) {
		setLeft(left);
		setRight(right);
	}

	/**
	 * Sets the right side of the drive
	 * 
	 * @param right
	 *            rightMotorSpeed
	 */
	public void setRight(double right) {
		this.right.set(-right);
	}

	/**
	 * Sets the left side of the drive
	 * 
	 * @param left
	 *            leftMotorSpeed
	 */
	public void setLeft(double left) {
		this.left.set(left);
	}

	@Override
	public void sendToSmartDash() {

		if (!DriverStation.getInstance().isAutonomous()) {
			teleopController.sendToSmartDash();
		} else {

			autoController.sendToSmartDash();
		}

		SmartDashboard.putNumber(this.getName() + " Left Side Pow", left.get());
		SmartDashboard.putNumber(this.getName() + " Right Side Pow", right.get());
		SmartDashboard.putNumber(this.getName() + " Angle", Sensors.getAngle());
		SmartDashboard.putNumber(this.getName() + " Anglular Vel", Sensors.getAngularVel());
		SmartDashboard.putNumber(this.getName() + " Roll", Sensors.getRoll());
		// Default to 0
		try {
			SmartDashboard.putNumber(this.getName() + " Left Encoder", Sensors.getLeftDrive());
			SmartDashboard.putNumber(this.getName() + " Left Rate", Sensors.getLeftDriveVel());
		} catch (Exception e) {
			System.out.println("BAD");
			e.printStackTrace();
			SmartDashboard.putNumber(this.getName() + " Left Encoder", 0);
			SmartDashboard.putNumber(this.getName() + " Left Rate", 0);
		}
		try {
			SmartDashboard.putNumber(this.getName() + " Right Encoder", Sensors.getRightDrive());
			SmartDashboard.putNumber(this.getName() + " Right Rate", Sensors.getRightDriveVel());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("BAD");
			SmartDashboard.putNumber(this.getName() + " Right Encoder", 0);
			SmartDashboard.putNumber(this.getName() + " Right Rate", 0);
		}
	}

	@Override
	public void manualControl() {
		if (Controls.driverController.getLB())
			sol.set(true);
		else
			sol.set(false);
		double throttle = Controls.driverController.getLeftY();
		double turn = Controls.driverController.getRightX();
		this.setLeftRight(throttle + turn, throttle - turn);
	}

	public boolean isLowGear() {
		return isLowGear;
	}

	/*
	 * @Override public void run() { double pastTime =
	 * System.currentTimeMillis(); while (true) { double startTime =
	 * System.currentTimeMillis(); if (DriverStation.getInstance().isEnabled())
	 * { if (!DriverStation.getInstance().isAutonomous()) this.updateTeleop();
	 * else this.updateAuto(); } // Loop Speed
	 * 
	 * double changeInTime = System.currentTimeMillis(); double timeItTook =
	 * changeInTime - startTime; long overhead = (long) (LOOP_TIME -
	 * (timeItTook)); // System.out.println("IT TOOK: " + timeItTook); try { if
	 * (overhead > 2) { // System.out.println("Time for Loop: " + overhead);
	 * KragerTimer.delayMS(overhead); } else { KragerTimer.delayMS(5);
	 * System.out.println("Loop Speed too fast!!! " + overhead); } } catch
	 * (Exception e) { e.printStackTrace(); } }
	 * 
	 * }
	 */
}
