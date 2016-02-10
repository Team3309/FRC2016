package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The single-wheel flywheel on the robot.
 * 
 * @author Krager
 *
 */
public class Shooter extends ControlledSubsystem {

	private Victor leftVictor = new Victor(RobotMap.LEFT_SHOOTER_MOTOR);
	private Victor rightVictor = new Victor(RobotMap.RIGHT_SHOOTER_MOTOR);
	private Victor hoodVictor = new Victor(RobotMap.HOOD_MOTOR);

	private double maxVelRPS = 0.0;
	private double maxAccRPS = 0.0;
	private double aimVelRPS = 0.0;
	private double aimAccRPS = 0.0;

	private double pastVel = 0;

	/**
	 * Value added to maxVelRPM
	 */
	private double offset = 0;

	/**
	 * Shooter for singleton pattern
	 */
	private static Shooter mShooter;

	private Shooter(String name) {
		super(name);
		this.mController = new FeedForwardWithPIDController(.008, 0, .005, 0, 0);
		SmartDashboard.putNumber("POWER", power);
	}

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Shooter getInstance() {
		if (mShooter == null) {
			mShooter = new Shooter("Shooter");
		}
		return mShooter;
	}

	double curVel = 0;
	double power = 0;
	@Override
	public void update() {
		//power = SmartDashboard.getNumber("POWER", power);
		//SmartDashboard.putNumber("POWER", power);
		/*if (Controls.driverController.getA()) {
			power = .7;
		}else if (Controls.driverController.getB()) {
			power = .4;
		}else {
			power = 0;
		}*/
		if (Controls.driverController.getA()) {
			power = .7;
		} else if (Controls.driverController.getB()) {
			power = .8;
		} else if (Controls.driverController.getXBut()) {
			power = .9;
		} else if (Controls.driverController.getYBut()) {
			power = 1;
		} else {
			power = 0;
		}
		this.rightVictor.set(power);
		this.leftVictor.set(power);
		this.hoodVictor.set(Controls.driverController.getLeftY());
		/*
		curVel = this.getRPS();

		// Find our base aim vel
		if (Controls.driverController.getA()) {
			aimVelRPS = 200;
		} else if (Controls.driverController.getB()) {
			aimVelRPS = 220;
		} else if (Controls.driverController.getXBut()) {
			aimVelRPS = 260;
		} else if (Controls.driverController.getYBut()) {
			aimVelRPS = 320;
		} else {
			offset = 0;
			aimVelRPS = 0;
			aimAccRPS = 0;
		}

		// Based off of cur vel and aim vel, find aim acc
		double error = this.aimVelRPS - curVel;
		if (Math.abs(error) > 1000) {
			if (error > 0) {
				aimAccRPS = maxAccRPS;
			} else if (error < 0) {
				aimAccRPS = -maxAccRPS;
			}
		} else {
			if (error > 0) {
				aimAccRPS = maxAccRPS / 4;
			} else if (error < 0) {
				aimAccRPS = -maxAccRPS / 4;
			}
		}

		// Find offset (how much should be added to the aim vel)
		// RB adds, LB decreases
		if (Controls.driverController.getLB()) {
			offset -= 50;
		} else if (Controls.driverController.getRB()) {
			offset += 50;
		}

		// Send our target velocity to the mController
		if (this.mController instanceof FeedForwardWithPIDController) {
			((FeedForwardWithPIDController) this.mController).setAimAcc(aimAccRPS);
			((FeedForwardWithPIDController) this.mController).setAimVel(aimVelRPS + offset);
		}

		// Get value and set to motors
		if (aimVelRPS == 0) {
			this.setShooter(0);
		} else {
			this.setShooter(this.mController.getOutputSignal(getInputState()).getMotor());
		}

		this.sendToSmartDash();*/
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setError(aimVelRPS - curVel);
		return input;
	}

	@Override
	public void sendToSmartDash() {
		mController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " RPM", curVel * 60);
		SmartDashboard.putNumber(this.getName() + " RPS", curVel);
		SmartDashboard.putNumber(this.getName() + " Lef", leftVictor.getSpeed());
		SmartDashboard.putNumber(this.getName() + " Right", rightVictor.getSpeed());
	}

	Counter shooterCounter = new Counter(RobotMap.SHOOTER_COUNTER);

	private double getRPS() {

		pastVel = Sensors.shooterEncoder.getRate();
		return Sensors.shooterEncoder.getRate();
	}

	private double getRPM() {
		return 60 / shooterCounter.getPeriod();
	}

	private void setShooter(double power) {
		leftVictor.set(power);
		rightVictor.set(-power);
	}

}
