package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.SensorDoesNotReturnException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Hood extends ControlledSubsystem {

	private static Hood mHood = new Hood("Hood");
	private Spark hoodSpark = new Spark(RobotMap.HOOD_MOTOR);
	private double curAngle = 0;
	private double goalAngle = 7.5;

	private double lastVisionAngle = 7.5;

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
		this.teleopController = new PIDPositionController(0.51, 0.001, .014);
		this.autoController = new PIDPositionController(0.51, 0.001, .014);
		((PIDController) this.teleopController).kILimit = .2;
		this.teleopController.setName("Hood Angle");
		((PIDController) this.teleopController).setTHRESHOLD(.6);
		((PIDController) this.autoController).kILimit = .2;
		this.autoController.setName("Hood Angle");
		((PIDController) this.autoController).setTHRESHOLD(.6);
		SmartDashboard.putNumber("Test Angle", 15);
	}

	@Override
	public void initTeleop() {
		goalAngle = 7.5;
	}

	@Override
	public void initAuto() {
		goalAngle = 7.5;
	}

	@Override
	public void updateTeleop() {
		try {
			curAngle = Sensors.getHoodAngle();
		} catch (SensorDoesNotReturnException e) {
			this.manualControl();
			return;
		}
		double output = 0;
		// Find aim angle
		if (Controls.operatorController.getA()) {
			goalAngle = 18.7;
		} else if (Controls.operatorController.getB()) {
			goalAngle = 35.5;
		} else if (Controls.operatorController.getXBut()) {
			goalAngle = 50;
		} else if (Controls.operatorController.getYBut()) {
			// goalAngle = 28.6;
			goalAngle = SmartDashboard.getNumber("Test Angle");
		} else if (Controls.operatorController.getStart()) {
			if (Vision.getInstance().getShotToAimTowards() != null) {
				goalAngle = Vision.getInstance().getShotToAimTowards().getGoalHoodAngle();
				lastVisionAngle = Vision.getInstance().getShotToAimTowards().getGoalHoodAngle();
			}else
				goalAngle = 25;
			System.out.println("Goal Angle: " + goalAngle);
		} else if (Controls.operatorController.getPOV() == 0) {
			goalAngle = lastVisionAngle;
		} else {
			goalAngle = 7.5;
		}
		if (goalAngle >= 0) {
			output = this.teleopController.getOutputSignal(getInputState()).getMotor();
		}

		if ((curAngle > 59 && output > -1) || (curAngle < -20 && output < 0) || this.isOnTarget()) {
			output = 0;
		}
			
		this.setHood(output);
	}

	@Override
	public void updateAuto() {
		double output = 0;
		try {
			curAngle = Sensors.getHoodAngle();
		} catch (SensorDoesNotReturnException e) {
			return;
		}
		if (goalAngle >= 0) {
			output = this.autoController.getOutputSignal(getInputState()).getMotor();
		}
		if ((curAngle > 59 && output > -1) || (curAngle < -20 && output < 0)) {
			output = 0;
		}
		this.setHood(output);
	}

	@Override
	public void sendToSmartDash() {
		if (DriverStation.getInstance().isAutonomous()) {
			autoController.sendToSmartDash();
		} else
			teleopController.sendToSmartDash();
		try {
			SmartDashboard.putNumber(this.getName() + " angle", Sensors.getHoodAngle());
		} catch (SensorDoesNotReturnException e) {
			SmartDashboard.putNumber(this.getName() + " angle", -100000);
		}
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
		this.setHood(KragerMath.threshold(Controls.operatorController.getRightY()));
	}

	public double getGoalAngle() {
		return goalAngle;
	}

	public void setGoalAngle(double goalAngle) {
		this.goalAngle = goalAngle;
	}
}