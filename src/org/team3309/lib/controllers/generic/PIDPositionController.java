package org.team3309.lib.controllers.generic;

public class PIDPositionController extends PIDController{

	public PIDPositionController(double kP, double kI, double kD) {
		super(kP, kI, kD);
	}
	
	public PIDPositionController(double kP, double kI, double kD, double kILimit) {
		super(kP, kI, kD, kILimit);
	}

	@Override
	public boolean isCompleted() {
		return false;
	}

}
