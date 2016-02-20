package org.usfirst.frc.team3309.subsystems.intake;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.robot.Sensors;

public class IntakePivot extends ControlledSubsystem {

	private static IntakePivot instance;
	private double goalAngle = 0;

	public static IntakePivot getInstance() {
		if (instance == null) {
			instance = new IntakePivot("Intake Pivot");
		}
		return instance;
	}

	private IntakePivot(String name) {
		super(name);
		mController = new PIDPositionController(.001, 0, 0);
	}

	@Override
	public void update() {
		double output = mController.getOutputSignal(getInputState()).getMotor();
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
		state.setError(goalAngle - Sensors.getIntakePivotAngle());
		return state;
	}

	@Override
	public void sendToSmartDash() {
		// TODO Auto-generated method stub

	}

	@Override
	public void manualControl() {

	}

}
