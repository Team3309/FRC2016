package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.drive.DriveAngleController;
import org.team3309.lib.controllers.drive.DriveEncodersController;
import org.team3309.lib.controllers.drive.FaceVisionTargetController;
import org.team3309.lib.controllers.drive.equations.DriveCheezyDriveEquation;
import org.team3309.lib.controllers.generic.BlankController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
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
	private static final double DRIVE_ENCODER_LENIENCY = 40;

	/**
	 * Used to give a certain gap that the drive would be ok with being within
	 * its goal angle
	 */
	private static final double DRIVE_GYRO_LENIENCY = .5;

	private static Drive instance;
	private Spark left = new Spark(RobotMap.LEFT_DRIVE);
	private Spark right = new Spark(RobotMap.RIGHT_DRIVE);
	private Solenoid sol = new Solenoid(0);

	private Shot desiredShot;

	public boolean lowGearInAuto = false;
	boolean isReset = false;
	DriveAngleController x = new DriveAngleController(this.getAngle() + 5);

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Drive getInstance() {
		if (instance == null)
			instance = new Drive("Drive");
		return instance;
	}

	private Drive(String name) {
		super(name);
		mController = new DriveCheezyDriveEquation();
		x.setName("Angle");
		// this.setController(x);
	}

	public void toTeleop() {
		x.setName("Angle");
		// this.setController(x);
		lowGearInAuto = false;
		mController = new DriveCheezyDriveEquation();
	}

	// Sets controller based on what state the remotes and game are in
	private void updateController() {
		// if mController is Completed and has not already been made blank, then
		// make it blank

		if (Controls.operatorController.getBack() && !isReset) {
			this.desiredShot = Vision.getInstance().getShot();
			Vision.getInstance().setLight(.6);
			if (this.desiredShot != null) {
				toVision();
			} else {
				System.out.println("Vision does not see anything");
				isReset = false;
			}
		} else if (Controls.operatorController.getBack()) {

		} else if (!DriverStation.getInstance().isAutonomous()) {
			isReset = false;
			Vision.getInstance().setLight(.70);
			// System.out.println("Vision Ended");
			this.setController(new DriveCheezyDriveEquation());
		}

		if (mController.isCompleted() && !(mController instanceof BlankController)) {
			// System.out.println("BLANK");
			mController = new BlankController();
		} else if (mController.isCompleted() && !DriverStation.getInstance().isAutonomous()) {
			// System.out.println("BLA NK");
			mController = new DriveCheezyDriveEquation();
		}

	}

	public void toVision() {
		this.desiredShot = Vision.getInstance().getShot();
		if (this.desiredShot != null) {
			FaceVisionTargetController x = new FaceVisionTargetController(.058, 0.015, 0.03);
			x.setName("VISION");
			x.reset();
			if (Math.abs(Vision.getInstance().getShot().getAzimuth()) < .5) {
				return;
			}
			// x.setGoalAngle(Vision.getInstance().getShot().getAzimuth());
			System.out.println("Vision started");
			x.setCompletable(false);
			this.setController(x);
			isReset = true;
		}
	}

	@Override
	public void update() {
		updateController();
		if (Controls.driverController.getLB() || lowGearInAuto) {
			// || ((DriverStation.getInstance().getMatchTime() < 1.5) &&
			// !DriverStation.getInstance().isAutonomous()))
			sol.set(false);
		} else {
			sol.set(true);
		}
		// System.out.println("SET MOTORS");
		OutputSignal output = mController.getOutputSignal(getInputState());
		// System.out.println("LEFT: " + output.getLeftMotor());
		setLeftRight(output.getLeftMotor(), output.getRightMotor());
	}

	public void setHighGear(boolean bool) {
		if (!bool)
			lowGearInAuto = true;
		sol.set(bool);
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		try {
			input.setAngularPos(Sensors.getAngle());
		} catch (ExceptionInInitializerError e) {
			for (StackTraceElement w : e.getStackTrace())
				System.out.println(w);
			System.out.println(e.getCause());
		}
		input.setAngularVel(Sensors.getAngularVel());
		input.setLeftPos(Sensors.getLeftDrive());
		input.setLeftVel(Sensors.getLeftDriveVel());
		input.setRightVel(Sensors.getRightDriveVel());
		input.setRightPos(Sensors.getRightDrive());
		return input;
	}

	/**
	 * Creates and runs a controller that gets the drive to the given setoiunt
	 * 
	 * @param encoders
	 *            goal encoder values
	 */

	public void setSetpoint(double encoders) {
		mController = new DriveEncodersController(encoders);
	}

	public void setAngleSetpoint(double goalAngle) {
		mController = new DriveAngleController(goalAngle);
		mController.reset();
		// ((PIDController) mController).setCompletable(false);
		mController.setName("Drive Angle Controller");
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
	public double getDistanceTraveled() {
		return (Math.abs(Sensors.getLeftDrive()) + Math.abs(Sensors.getRightDrive())) / 2;
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
		if (getDistanceTraveled() < encoderGoal + DRIVE_ENCODER_LENIENCY
				&& getDistanceTraveled() > encoderGoal - DRIVE_ENCODER_LENIENCY) {
			return true;
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
		if (getAngle() < angleGoal + DRIVE_GYRO_LENIENCY && getDistanceTraveled() > angleGoal - DRIVE_GYRO_LENIENCY) {
			return true;
		}
		return false;
	}

	/**
	 * Stops current running controller and sets motors to zero
	 */
	public void stopDrive() {
		mController = new BlankController();
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
		mController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " Left Side Pow", left.get());
		SmartDashboard.putNumber(this.getName() + " Right Side Pow", right.get());
		SmartDashboard.putNumber(this.getName() + " Angle", Sensors.getAngle());
		SmartDashboard.putNumber(this.getName() + " Anglular Vel", Sensors.getAngularVel());
		SmartDashboard.putNumber(this.getName() + " Left Encoder", Sensors.getLeftDrive());
		SmartDashboard.putNumber(this.getName() + " Left Rate", Sensors.getLeftDriveVel());
		SmartDashboard.putNumber(this.getName() + " Right Encoder", Sensors.getRightDrive());
		SmartDashboard.putNumber(this.getName() + " Right Rate", Sensors.getRightDriveVel());
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
}
