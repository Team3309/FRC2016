package org.usfirst.frc.team3309.subsystems.climber;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.robot.Sensors;

public class Hook extends ControlledSubsystem {

	private double goalPosition = Sensors.getHookAngle();

	private Hook(String name) {
		super(name);
	}

	@Override
	public void initTeleop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initAuto() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTeleop() {

	}

	@Override
	public void updateAuto() {
		// TODO Auto-generated method stub

	}

	@Override
	public InputState getInputState() {
		InputState inputState = new InputState();
		inputState.setError(goalPosition - Sensors.getHookAngle());
		return null;
	}

	@Override
	public void sendToSmartDash() {
		// TODO Auto-generated method stub

	}

	@Override
	public void manualControl() {

	}
}
