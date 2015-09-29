package org.team3309.lib.controllers.generic;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

/**
 * Basic PID Controller. The isCompleted Method will always return false.
 * @author TheMkrage
 *
 */
public abstract class PIDController extends Controller {
	/**
	 * Gains
	 */
	private double kP, kD, kI;
	/**
	 * Limit of the Integral. mIntegral is capped off at the kILimit.
	 */
	private double kILimit;
	/**
	 * Stores previous error and running Integral term to use between loops.
	 */
	private double previousError = 0, mIntegral = 0;

	public PIDController(double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	public PIDController(double kP, double kI, double kD, double kILimit) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kILimit = kILimit;
	}

	// You would want to set the mIntegral and previousError to zero when reusing a PID Loop
	@Override
	public void reset() {
		mIntegral = 0;
		previousError = 0;
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double error = inputState.getError();
		// Calculate the derivative and log previous error
		double pidDerivative = error - previousError;
		previousError = error;
		// Add to mIntegral term
		mIntegral = error + mIntegral;

		// Check for integral hitting the limit
		if (mIntegral > kILimit)
			mIntegral = kILimit;

		if (mIntegral < -kILimit)
			mIntegral = -kILimit;
		
		// Make OutputSignal and fill it with calculated values
		OutputSignal signal = new OutputSignal();
		signal.setMotor((kP * error) + (kI * mIntegral) + (kD * pidDerivative));
		return signal;
	}

	@Override
	public abstract boolean isCompleted();

}
