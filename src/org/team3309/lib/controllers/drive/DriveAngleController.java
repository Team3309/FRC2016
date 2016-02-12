package org.team3309.lib.controllers.drive;

import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;

public class DriveAngleController extends PIDPositionController{

	public DriveAngleController(double kP, double kI, double kD) {
		super(kP, kI, kD);
	}

	
	public OutputSignal getOutputSignal(InputState inputState) { 
		double error = inputState.getError();
		(error - Sensors.getAngle());
		return null;
	}
	
}