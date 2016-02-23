package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Hood extends ControlledSubsystem {

	private static Hood mHood = new Hood("Hood");
	private Spark hoodSpark = new Spark(RobotMap.HOOD_MOTOR);
	private double curAngle = 0;
	private double goalAngle = curAngle;

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Hood getInstance() {
		if (mHood == null) {
			mHood = new Hood("Hood");
		}
		return mHood;
	}

	private Hood(String name) {
		super(name);
		this.mController = new PIDPositionController(0.001, 0, 0);
		this.mController.setName("Hood Angle");
	}

	@Override
	public void update() {
		curAngle = Sensors.getHoodAngle();
		// Find aim angle
		if (Controls.operatorController.getA()) {
			goalAngle = 15.8;
		} else if (Controls.operatorController.getB()) {
			goalAngle = 27.6;
		} else if (Controls.operatorController.getXBut()) {
			goalAngle = 30;
		} else if (Controls.operatorController.getYBut()) {
			goalAngle = 45;
		} else {
			goalAngle = .2;
		}
		double output = this.mController.getOutputSignal(getInputState()).getMotor();
		if ((curAngle > 59 && output > 0) || (curAngle < 0 && output < 0)) {
			output = 0;
		}
		this.hoodSpark.set(output);
	}

	@Override
	public void sendToSmartDash() {
		this.mController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " angle", curAngle);
		SmartDashboard.putNumber(this.getName() + " goal angle", goalAngle);
		SmartDashboard.putNumber(this.getName() + " power", this.hoodSpark.get());
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setError(goalAngle - curAngle);
		return input;
	}

	public void setHood(double power) {
		this.hoodSpark.set(power);
	}

	@Override
	public void manualControl() {
		/*
		 * if (Controls.driverController.getA()) { this.setHood(.4); } else if
		 * (Controls.driverController.getB()) { this.setHood(-.4); } else {
		 * this.setHood(0); }
		 */

		this.setHood(KragerMath.threshold(Controls.operatorController.getRightY()));
	}
}