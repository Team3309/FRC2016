package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;

/**
 * Drive Train
 * 
 * @author TheMkrage
 *
 */
public class Drive extends ControlledSubsystem {
	private static Drive instance;
	private Victor leftFront = new Victor(RobotMap.LEFT_FRONT_MOTOR);
	private Victor rightFront = new Victor(RobotMap.RIGHT_FRONT_MOTOR);
	private Victor leftBack = new Victor(RobotMap.LEFT_BACK_MOTOR);
	private Victor rightBack = new Victor(RobotMap.RIGHT_BACK_MOTOR);

	public static Drive getInstance() {
		if (instance == null)
			instance = new Drive("Drive");
		return instance;
	}

	private Drive(String name) {
		super(name);
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
		rightFront.set(right);
		rightBack.set(right);
	}

	/**
	 * Sets the left side of the drive
	 * 
	 * @param left
	 *            leftMotorSpeed
	 */
	private void setLeft(double left) {
		leftFront.set(left);
		leftBack.set(left);
	}

	// Sets controller based on what state the remotes and game are in
	private void updateController() {

	}

	@Override
	public void update() {
		updateController();
		OutputSignal output = mController.getOutputSignal(getInputState());
		setLeftRight(output.getLeftMotor(), output.getRightMotor());
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setAngularPos(Sensors.getAngle());
		input.setAngularVel(Sensors.getAngularVel());
		input.setLeftPos(Sensors.leftDrive.getDistance());
		input.setLeftVel(Sensors.leftDrive.getRate());
		input.setRightVel(Sensors.rightDrive.getDistance());
		input.setRightPos(Sensors.rightDrive.getRate());
		return input;
	}

}
