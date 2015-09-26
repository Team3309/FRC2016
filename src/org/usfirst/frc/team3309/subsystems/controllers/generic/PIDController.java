package org.usfirst.frc.team3309.subsystems.controllers.generic;

import org.usfirst.frc.team3309.subsystems.controllers.Controller;
import org.usfirst.frc.team3309.subsystems.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.subsystems.controllers.statesandsignals.OutputSignal;

public class PIDController extends Controller{
	private double kP, kD, kI;
	private double kILimit;
	
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
	public OutputSignal getOutputState(InputState inputState) {
		return null;
	}

	@Override
	public boolean isCompleted() {
		return false;
	}

}
