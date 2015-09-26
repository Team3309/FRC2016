package org.team3309.lib.controllers.generic;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

public class PIDController extends Controller{
	private double kP, kD, kI;
	private double kILimit;
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
	
	@Override
	public void reset() {
		mIntegral = 0;
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double error = inputState.getError();
		// Calculate the derivative and log previous error
		double pidDerivative = error - previousError;
		previousError = error;
		// Add to mIntegral term
		mIntegral = error + mIntegral;

		if(mIntegral > kILimit)
			mIntegral = kILimit;

		if(mIntegral < -kILimit)
			mIntegral = -kILimit;
		
		OutputSignal signal = new OutputSignal();
		signal.setMotor((kP * error) + (kI * mIntegral) + (kD * pidDerivative));
		return signal;
	}

	@Override
	public boolean isCompleted() {
		return false;
	}

}
