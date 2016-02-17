package org.usfirst.frc.team3309.subsystems.climber;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.DriverStation;

public class Carriage extends ControlledSubsystem {

	private Carriage instance;
	private double goalPosition = 0;

	public Carriage getInstance() {
		if (instance == null) {
			instance = new Carriage("Carriage");
		}
		return instance;
	}

	private Carriage(String name) {
		super(name);
		mController = new PIDPositionController(.01, 0, 0);
	}

	@Override
	public void update() {
		if (Controls.driverController.getA()) {
			goalPosition = 45;
		} else if (Controls.driverController.getB()) {
			goalPosition = 70;
		} else {
			goalPosition = Sensors.getHookAngle();
		}
		double output = mController.getOutputSignal(getInputState()).getMotor();
		this.setCarriage(output);
	}

	@Override
	public InputState getInputState() {
		InputState inputState = new InputState();
		inputState.setError(goalPosition - Sensors.getHookAngle());
		return null;
	}

	@Override
	public void sendToSmartDash() {

	}

	private void setCarriage(double power) {

	}
}
