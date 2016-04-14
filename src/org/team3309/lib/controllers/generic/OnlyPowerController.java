package org.team3309.lib.controllers.generic;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

public class OnlyPowerController extends Controller {

	public OnlyPowerController(ControlledSubsystem subsystem) {
		super(subsystem);
	}

	public OnlyPowerController(ControlledSubsystem subsystem, boolean hasSeparateThread) {
		super(subsystem, hasSeparateThread);
	}

	/**
	 * Controller that returns only zero values. Used when a subsystem is
	 * disabled, but still requires a controller.
	 * 
	 * @author TheMkrage
	 * 
	 */
	private double power = 0;
	private double leftPower = 0;
	private double rightPower = 0;

	public double getLeftPower() {
		return leftPower;
	}

	public void setLeftPower(double leftPower) {
		this.leftPower = leftPower;
	}

	public double getRightPower() {
		return rightPower;
	}

	public void setRightPower(double rightPower) {
		this.rightPower = rightPower;
	}

	@Override
	public void reset() {

	}

	@Override
	public OutputSignal getOutputSignal() {
		return super.getOutputSignal();
	}

	@Override
	public void update(InputState inputState) {
		OutputSignal signal = new OutputSignal();
		signal.setLeftRightMotor(leftPower, rightPower);
		signal.setMotor(power);
		this.lastOutputState = signal; // Returns only zeros for everything
	}

	@Override
	public boolean isCompleted() {
		return false; // Is always complete
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
		this.rightPower = power;
		this.leftPower = power;
	}

}
