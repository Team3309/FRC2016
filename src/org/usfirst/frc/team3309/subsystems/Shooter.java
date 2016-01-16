package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The single-wheel flywheel on the robot.
 * 
 * @author Krager
 *
 */
public class Shooter extends ControlledSubsystem {

	/**
	 * Shooter for singleton pattern
	 */
	private Shooter mShooter;
	private Victor leftVictor = new Victor(RobotMap.LEFT_SHOOTER_MOTOR);
	private Victor rightVictor = new Victor(RobotMap.RIGHT_SHOOTER_MOTOR);

	private double maxVelRPM = 0.0;
	private double maxAccRPM = 0.0;
	private double aimVelRPM = 0.0;
	private double aimAccRPM = 0.0;

	/**
	 * Value added to maxVelRPM
	 */
	private double offset = 0;

	private Shooter(String name) {
		super(name);
		this.mController = new FeedForwardWithPIDController(.008, 0, .005, 0, 0);
	}

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public Shooter getInstance() {
		if (mShooter == null) {
			mShooter = new Shooter("Shooter");
		}
		return mShooter;
	}

	@Override
	public void update() {
		double curVel = this.getRPM();

		// Find our base aim vel
		if (Controls.driverController.getA()) {
			aimVelRPM = 600;
		} else if (Controls.driverController.getB()) {
			aimVelRPM = 800;
		} else if (Controls.driverController.getXBut()) {
			aimVelRPM = 1000;
		} else if (Controls.driverController.getYBut()) {
			aimVelRPM = 1200;
		} else {
			offset = 0;
			aimVelRPM = 0;
			aimAccRPM = 0;
		}

		// Based off of cur vel and aim vel, find aim acc
		double error = this.aimVelRPM - curVel;
		if (Math.abs(error) > 1000) {
			if (error > 0) {
				aimAccRPM = maxAccRPM;
			} else if (error < 0) {
				aimAccRPM = -maxAccRPM;
			}
		} else {
			if (error > 0) {
				aimAccRPM = maxAccRPM/4;
			} else if (error < 0) {
				aimAccRPM = -maxAccRPM/4;
			}
		}

		// Find offset (how much should be added to the aim vel)
		// RB adds, LB decreases
		if (Controls.driverController.getLB()) {
			offset -= 50;
		} else if (Controls.driverController.getRB()) {
			offset += 50;
		}

		// Send our aim's to the mController
		if (this.mController instanceof FeedForwardWithPIDController) {
			((FeedForwardWithPIDController) this.mController).setAimAcc(aimAccRPM);
			((FeedForwardWithPIDController) this.mController).setAimVel(aimVelRPM + offset);
		}

		// Get value and set to motors
		if (aimVelRPM != 0) {
			this.setShooter(0);
		} else {
			this.setShooter(this.mController.getOutputSignal(getInputState()).getMotor());
		}

		this.sendToSmartDash();
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setError(getRPM() - aimVelRPM);
		return input;
	}

	@Override
	public void sendToSmartDash() {
		mController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " RPM", getRPM());
		SmartDashboard.putNumber(this.getName() + " RPS", getRPS());
	}

	private double getRPS() {
		return 1 / Sensors.shooterCounter.getPeriod();
	}

	private double getRPM() {
		return 60 / Sensors.shooterCounter.getPeriod();
	}

	private void setShooter(double power) {
		leftVictor.set(power);
		rightVictor.set(power);
	}

}
