package org.team3309.lib.controllers.drive;

import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;

public class DriveAngleController extends PIDPositionController {
	double startingAngle = 0;
	double goalAngle = 0;

	public DriveAngleController(double goal) {
		super(.01, 0, 0);
		this.setTHRESHOLD(2);
		startingAngle = Sensors.getAngle();
		goalAngle = goal;
	}

	public OutputSignal getOutputSignal(InputState inputState) {
		double error = goalAngle - inputState.getAngularPos();
		if (Math.abs(error) > 180) {
			error = -KragerMath.sign(error) * (360 - Math.abs(error));
			System.out.println("New Error: " + error);
		}
		InputState state = new InputState();
		state.setError(error);
		double left = super.getOutputSignal(state).getMotor();
		OutputSignal signal = new OutputSignal();
		signal.setLeftRightMotor(left, -left);
		return signal;
	}

}