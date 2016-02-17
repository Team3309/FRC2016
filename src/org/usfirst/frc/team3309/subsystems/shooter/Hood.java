package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
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
		if (Controls.driverController.getA()) {
			goalAngle = 10;
		} else if (Controls.driverController.getB()) {
			goalAngle = 20;
		} else if (Controls.driverController.getXBut()) {
			goalAngle = 30;
		} else if (Controls.driverController.getYBut()) {
			goalAngle = 45;
		}
		double output = this.mController.getOutputSignal(getInputState()).getMotor();
		this.hoodSpark.set(output);
	}

	@Override
	public void sendToSmartDash() {
		this.mController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " angle", curAngle);
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setError(goalAngle - curAngle);
		return input;
	}

}
