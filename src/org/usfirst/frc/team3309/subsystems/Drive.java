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
import org.usfirst.frc.team3309.vision.Vision;

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
	private static final double DRIVE_GYRO_LENIENCY = 10;

	private static Drive instance;
	private Spark left = new Spark(RobotMap.LEFT_DRIVE);
	private Spark right = new Spark(RobotMap.RIGHT_DRIVE);
	private Solenoid sol = new Solenoid(0);

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
	}

	public void toTeleop() {
		mController = new DriveCheezyDriveEquation();
	}

	boolean isReset = false;

	// Sets controller based on what state the remotes and game are in
	private void updateController() {
		// if mController is Completed and has not already been made blank, then
		// make it blank
		if (Controls.operatorController.getStart() && !isReset) {
			Vision.getInstance().setLight(.5);
			FaceVisionTargetController x = new FaceVisionTargetController(.001, 0, 0);
			x.setName("VISION");
			this.setController(x);
			isReset = true;
		} else if (Controls.operatorController.getStart() && isReset) {
			
		}else {
			isReset = false;
			Vision.getInstance().setLight(0);
			this.setController(new DriveCheezyDriveEquation());
		}

		if (mController.isCompleted() && !(mController instanceof BlankController)) {
			mController = new BlankController();
		} else if (mController.isCompleted()) {
			mController = new DriveCheezyDriveEquation();
		}
	}

	@Override
	public void update() {
		updateController();
		if (Controls.driverController.getLB())
			sol.set(true);
		else
			sol.set(false);
		// System.out.println("SET MOTORS");
		OutputSignal output = mController.getOutputSignal(getInputState());
		// System.out.println("LEFT: " + output.getLeftMotor());
		setLeftRight(output.getLeftMotor(), output.getRightMotor());
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
		// input.setLeftPos(Sensors.leftDrive.getDistance());
		// input.setLeftVel(Sensors.leftDrive.getRate());
		// input.setRightVel(Sensors.rightDrive.getDistance());
		// input.setRightPos(Sensors.rightDrive.getRate());
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
		return (Sensors.getLeftDrive() + Sensors.getRightDrive()) / 2;
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
	private void setLeftRight(double left, double right) {
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
	private void setRightLeft(double right, double left) {
		setLeft(left);
		setRight(right);
	}

	/**
	 * Sets the right side of the drive
	 * 
	 * @param right
	 *            rightMotorSpeed
	 */
	private void setRight(double right) {
		this.right.set(-right);
	}

	/**
	 * Sets the left side of the drive
	 * 
	 * @param left
	 *            leftMotorSpeed
	 */
	private void setLeft(double left) {
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
