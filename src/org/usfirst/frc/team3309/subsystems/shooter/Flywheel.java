package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.SensorDoesNotReturnException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.vision.Vision;

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

	private double maxAccRPS = 31.0;
	private double aimVelRPS = 0.0;
	private double aimAccRPS = 0.0;

	private double pastVel = 0;

	private double lastVisionShot;
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
		this.setTeleopController(new FeedForwardWithPIDController(this, .005, 0, .008, 0.000, 0.00));
		this.setAutoController(new FeedForwardWithPIDController(this, .005, 0, .008, 0.000, 0.00));
		this.teleopController.setName("Flywheel");
		this.rightSpark.setInverted(true);
		this.autoController.setName("Flywheel");
		((FeedForwardWithPIDController) this.teleopController).setTHRESHOLD(10);
		((FeedForwardWithPIDController) this.autoController).setTHRESHOLD(10);
		SmartDashboard.putNumber("TEST RPS", 110);
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
	private double autoVel = 0;

	public void setAimVelRPSAuto(double power) {
		this.aimVelRPS = power;
		this.autoVel = this.aimVelRPS;
	}

	@Override
	public void initTeleop() {

	}

	@Override
	public void initAuto() {
		hasGoneBack = false;

	}

	@Override
	public void updateAuto() {
		try {
			curVel = this.getRPS();
		} catch (SensorDoesNotReturnException e) {
			this.setShooter(.7);
		}
		this.aimVelRPS = this.autoVel;
		shootLikeRobie();
	}

	// In front of batter 140 rps 26 degrees
	@Override
	public void updateTeleop() {
		// manualControl();
		try {
			curVel = this.getRPS();
		} catch (SensorDoesNotReturnException e) {
			// this.manualControl();
			// return;
		}
		// Find our base aim vel
		if (Controls.operatorController.getA()) {
			aimVelRPS = 120;
		} else if (Controls.operatorController.getB()) {
			aimVelRPS = 120;
		} else if (Controls.operatorController.getXBut()) {
			aimVelRPS = 160;
		} else if (Controls.operatorController.getYBut()) {
			aimVelRPS = SmartDashboard.getNumber("TEST RPS");
			// aimVelRPS = 120;
		} else if (Controls.operatorController.getStart()) {
			if (Vision.getInstance().getShotToAimTowards() != null) {
				aimVelRPS = Vision.getInstance().getShotToAimTowards().getGoalRPS();
				this.lastVisionShot = Vision.getInstance().getShotToAimTowards().getGoalRPS();
				// aimVelRPS = 140;
			} else {
				aimVelRPS = 140;
			}
		} /*
			 * else if (Controls.operatorController.getPOV() == 0) { aimVelRPS =
			 * this.lastVisionShot; }
			 */ else {
			offset = 0;
			aimVelRPS = 0;
			aimAccRPS = 0;
			hasGoneBack = false;
		}
		shootLikeRobie();

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
			KragerTimer.delayMS(75);
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
		if (this.teleopController instanceof FeedForwardWithPIDController) {
			((FeedForwardWithPIDController) this.teleopController).setAimAcc(aimAccRPS);
			((FeedForwardWithPIDController) this.teleopController).setAimVel(aimVelRPS + offset);
		}
		double output = this.teleopController.getOutputSignal().getMotor();
		if (output > 1) {
			output = 1;
		} else if (output < 0) {
			output = 0;
		}
		if (aimVelRPS != 0 && !hasGoneBack) {
			FeedyWheel.getInstance().setFeedyWheel(-.5);
			KragerTimer.delayMS(120);
			FeedyWheel.getInstance().setFeedyWheel(0);
			hasGoneBack = true;
		}
		// Get value and set to motors
		if (aimVelRPS == 0) {
			this.setShooter(0);
		} else {
			if (curVel < 30) {
				output = .45;
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

	public double getPercent() {
		if (aimVelRPS == 0)
			return 0;
		double x = (double) curVel / (aimVelRPS - 10) * 100;
		if (x > 100) {
			x = 100;
		}
		return x;
	}

	@Override
	public void sendToSmartDash() {
		teleopController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " RPM", curVel * 60);
		SmartDashboard.putNumber(this.getName() + " RPS", curVel);
		SmartDashboard.putNumber(this.getName() + " Goal", this.aimVelRPS);
		SmartDashboard.putNumber(this.getName() + " Left", leftSpark.getSpeed());
		SmartDashboard.putNumber(this.getName() + " Right", rightSpark.getSpeed());
	}

	private double getRPS() throws SensorDoesNotReturnException {
		pastVel = Sensors.getShooterRPS();
		return Sensors.getShooterRPS();
	}

	public double getAimVelRPS() {
		return aimVelRPS;
	}

	public void setAimVelRPS(double aimVelRPS) {
		this.aimVelRPS = aimVelRPS;
	}

	public double getAimAccRPS() {
		return aimAccRPS;
	}

	public void setAimAccRPS(double aimAccRPS) {
		this.aimAccRPS = aimAccRPS;
	}

	public boolean isShooterInRange() {
		try {
			if (this.getRPS() < aimVelRPS + 6 && this.getRPS() > aimVelRPS - 6)
				return true;
		} catch (SensorDoesNotReturnException e) {
			return false;
		}
		return false;
	}

	private double getRPM() throws SensorDoesNotReturnException {
		return 60 * Sensors.getShooterRPS();
	}

	private void setShooter(double power) {
		leftSpark.set(power);
		rightSpark.set(power);
	}

}
