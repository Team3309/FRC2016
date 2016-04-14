package org.team3309.lib.controllers.generic;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

/**
 * PID Controller used for velocity. It adds the PID value to the previous one,
 * so it maintains value.
 * 
 * @author TheMkrage
 *
 */
public class PIDVelocityController extends PIDController {
	/**
	 * Variable to store running velocity so the feedback can be added to the
	 * previous one.
	 */
	private double runningVelocity = 0.0;

	public PIDVelocityController(ControlledSubsystem system, double kP, double kI, double kD) {
		super(system, kP, kI, kD);
	}

	public PIDVelocityController(ControlledSubsystem system, double kP, double kI, double kD, double kILimit) {
		super(system, kP, kI, kD, kILimit);
	}

	@Override
	public OutputSignal getOutputSignal() {
		return super.getOutputSignal();
	}
	
	@Override
	public void update(InputState inputState) {
		OutputSignal signal = new OutputSignal();
		super.update(inputState);
		runningVelocity += super.getOutputSignal().getMotor();
		signal.setMotor(runningVelocity);
		this.lastOutputState = signal;
	}
}
