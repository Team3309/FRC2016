package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.DriverStation;
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
		this.mController = new PIDPositionController(0.51, 0.001, .014); // .072, .002, .013
		((PIDController) this.mController).kILimit = .2;
		this.mController.setName("Hood Angle");
		((PIDController) this.mController).setTHRESHOLD(.4);
		// this.mController.
		SmartDashboard.putNumber("Test Angle", 15);

	}

	@Override
	public void update() {
		System.out.println("Start Hood");
		curAngle = Sensors.getHoodAngle();
		System.out.println("ADFASSDFAA AANGE:");
		double output = 0;
		// Find aim angle
		if (Controls.operatorController.getA()) {
			goalAngle = 14.65;
		} else if (Controls.operatorController.getB()) {
			goalAngle = 28.6;
		} else if (Controls.operatorController.getXBut()) {
			goalAngle = 42.85;
		} else if (Controls.operatorController.getYBut()) {
			 goalAngle = 28.6;
			//goalAngle = SmartDashboard.getNumber("Test Angle");
		} else if (Controls.operatorController.getStart()) {
			if (Vision.getInstance().getShot() != null)
				goalAngle = Vision.getInstance().getShot().getGoalHoodAngle();
			else
				goalAngle = 4;
		} /*else if (KragerMath.threshold(Controls.operatorController.getRightY()) != 0) {
			output = KragerMath.threshold(Controls.operatorController.getRightY());
			//goalAngle = -1000;
		} */else if (!DriverStation.getInstance().isAutonomous()) {
			goalAngle = 4;
			System.out.println("NOT AUTO");
			
		}

		if (goalAngle >= 0) {
			output = this.mController.getOutputSignal(getInputState()).getMotor();
		}

		if ((curAngle > 59 && output > -1) || (curAngle < -20 && output < 0) || this.isOnTarget()) {
			output = 0;
		}
		System.out.println("DONE WITH HOOD");
		this.setHood(output);
	}

	@Override
	public void sendToSmartDash() {
		this.mController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " angle", Sensors.getHoodAngle());
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
		curAngle = Sensors.getHoodAngle();
		/*
		 * if (Controls.driverController.getA()) { this.setHood(.4); } else if
		 * (Controls.driverController.getB()) { this.setHood(-.4); } else {
		 * this.setHood(0); }
		 */

		this.setHood(KragerMath.threshold(Controls.operatorController.getRightY()));
	}

	public double getGoalAngle() {
		return goalAngle;
	}

	public void setGoalAngle(double goalAngle) {
		this.goalAngle = goalAngle;
	}
}