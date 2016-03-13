package org.team3309.lib.controllers.drive;

import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

public class DriveAngleFeedForwardController extends FeedForwardWithPIDController {

	private double aimAngle = 0;
	public DriveAngleFeedForwardController(double aim) {
		super(0, 0, 0, 0, 0); // kv ka pid
		aimAngle = aim;
	}

	@Override
	public void reset() {

	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		OutputSignal signal = new OutputSignal();
		InputState state = new InputState(); 
		state.setError(aimAngle - inputState.getAngularPos());
		OutputSignal signalFromSuper = super.getOutputSignal(state);
		signal.setLeftRightMotor(signalFromSuper.getMotor(), -signalFromSuper.getMotor());
		return signal;
	}

	@Override
	public boolean isCompleted() {
		return false;
	}

	public void sendToSmartDash() {
		super.sendToSmartDash();
	}
}
