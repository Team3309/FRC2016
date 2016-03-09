package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The single-wheel flywheel on the robot.
 * 
 * @author Krager
 *
 */
public class Flywheel extends ControlledSubsystem {

	private Spark leftSpark = new Spark(RobotMap.LEFT_SHOOTER_MOTOR);
	private Spark rightSpark = new Spark(RobotMap.RIGHT_SHOOTER_MOTOR);

	private double maxVelRPS = 155.0;
	private double maxAccRPS = 31.0;
	public double aimVelRPS = 0.0;
	private double aimAccRPS = 0.0;

	private double pastVel = 0;

	private boolean hasGoneBack = false;
	/**
	 * Value added to maxVelRPM
	 */
	private double offset = 0;

	/**
	 * Shooter for singleton pattern
	 */
	private static Flywheel mFlywheel;

	private Flywheel(String name) {
		super(name);
		this.mController = new FeedForwardWithPIDController(.006, 0, .006, 0.000, 0.00);
		this.mController.setName("Flywheel");
		this.rightSpark.setInverted(true);
		SmartDashboard.putNumber("TEST RPS", 100);
	}

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Flywheel getInstance() {
		if (mFlywheel == null) {
			mFlywheel = new Flywheel("Flywheel");
		}
		return mFlywheel;
	}

	double curVel = 0;
	double power = 0;

	// In front of batter 140 rps 26 degrees
	@Override
	public void update() {
		// manualControl();
		curVel = this.getRPS();
		// Find our base aim vel
		if (Controls.operatorController.getA()) {
			aimVelRPS = 110;
		} else if (Controls.operatorController.getB()) {
			aimVelRPS = 130;
		} else if (Controls.operatorController.getXBut()) {
			aimVelRPS = 140;
		} else if (Controls.operatorController.getYBut()) {
			aimVelRPS = SmartDashboard.getNumber("TEST RPS");
		} else if (Controls.operatorController.getStart()) {
			if (Vision.getInstance().getShot() != null) {
				aimVelRPS = Vision.getInstance().getShot().getGoalRPS();
			}
		} else if (Controls.operatorController.getBack()) {
			aimVelRPS = 160;
			
		}else if (!DriverStation.getInstance().isAutonomous()){
			offset = 0;
			aimVelRPS = 0;
			aimAccRPS = 0;
			hasGoneBack = false;
		}

		shootLikeRobie();

		/*
		 * // Based off of cur vel and aim vel, find aim acc double error =
		 * this.aimVelRPS - curVel; if (Math.abs(error) > 1000) { if (error > 0)
		 * { aimAccRPS = maxAccRPS; } else if (error < 0) { aimAccRPS =
		 * -maxAccRPS; } } else { if (error > 0) { aimAccRPS = maxAccRPS / 4; }
		 * else if (error < 0) { aimAccRPS = -maxAccRPS / 4; } }
		 * 
		 * // Find offset (how much should be added to the aim vel) // RB adds,
		 * if (Controls.driverController.getLB()) { offset -= 0; } else if
		 * (Controls.driverController.getRB()) { offset += 0; }
		 * 
		 * if (output != 0 && !hasGoneBack) {
		 * FeedyWheel.getInstance().setFeedyWheel(-.5); try { Thread.sleep(75);
		 * } catch (InterruptedException e) { e.printStackTrace(); }
		 * FeedyWheel.getInstance().setFeedyWheel(0); hasGoneBack = true; }
		 * 
		 * // Send our target velocity to the mController if (this.mController
		 * instanceof FeedForwardWithPIDController) {
		 * ((FeedForwardWithPIDController)
		 * this.mController).setAimAcc(aimAccRPS);
		 * ((FeedForwardWithPIDController) this.mController).setAimVel(aimVelRPS
		 * + offset); }
		 * 
		 * // Get value and set to motors if (aimVelRPS == 0) {
		 * this.setShooter(0); } else { this.setShooter(output); }
		 */
		// double output =
		// this.mController.getOutputSignal(getInputState()).getMotor();

	}

	/**
	 * Raw power values
	 */
	public void manualControl() {
		if (Controls.operatorController.getA()) {
			power = .7;
		} else if (Controls.operatorController.getB()) {
			power = .7;
		} else if (Controls.operatorController.getXBut()) {
			power = .8;
		} else if (Controls.operatorController.getYBut()) {
			power = .9;
		} else {
			hasGoneBack = false;
			power = 0;
		}
		if (power != 0 && !hasGoneBack) {
			FeedyWheel.getInstance().setFeedyWheel(-.5);
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			FeedyWheel.getInstance().setFeedyWheel(0);
			hasGoneBack = true;
		}
		this.rightSpark.set(power);
		this.leftSpark.set(power);
	}

	/**
	 * Feed Forward with dynamic aims
	 */
	private void shootLikeRobie() {
		if (aimVelRPS == 0) {
		} else {
			if (curVel < aimVelRPS - 32) {
				aimAccRPS = maxAccRPS;
				aimVelRPS = curVel + maxAccRPS;

			} else if (curVel > aimVelRPS + 32) {
				aimAccRPS = 0;
				// aimVelRPS = curVel + maxAccRPS;
			} else {
				aimAccRPS = 0;
			}
		}

		// Send our target velocity to the mController
		if (this.mController instanceof FeedForwardWithPIDController) {
			((FeedForwardWithPIDController) this.mController).setAimAcc(aimAccRPS);
			((FeedForwardWithPIDController) this.mController).setAimVel(aimVelRPS + offset);
		}
		double output = this.mController.getOutputSignal(this.getInputState()).getMotor();

		if (output > 1) {
			output = 1;
		} else if (output < 0) {
			output = 0;
		}

		if (aimVelRPS != 0 && !hasGoneBack) {
			FeedyWheel.getInstance().setFeedyWheel(-.5);
			System.out.println("FEEDY WHEEL");
			try {
				Thread.sleep(130);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			FeedyWheel.getInstance().setFeedyWheel(0);
			hasGoneBack = true;
		} else {

		}

		// Get value and set to motors
		if (aimVelRPS == 0) {
			this.setShooter(0);
		} else {
			if (curVel < 18) {
				output = .35;
			}
			this.setShooter(output);
		}
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
		SmartDashboard.putNumber(this.getName() + " Goal", this.aimVelRPS);
		SmartDashboard.putNumber(this.getName() + " Left", leftSpark.getSpeed());
		SmartDashboard.putNumber(this.getName() + " Right", rightSpark.getSpeed());
	}

	private double getRPS() {
		pastVel = Sensors.getShooterRPS();
		return Sensors.getShooterRPS();
	}

	public boolean isShooterInRange() {
		if (this.getRPS() < aimVelRPS + 6 && this.getRPS() > aimVelRPS - 6)
			return true;
		return false;
	}

	private double getRPM() {
		return 60 * Sensors.getShooterRPS();
	}

	private void setShooter(double power) {
		leftSpark.set(power);
		rightSpark.set(power);
	}

}
