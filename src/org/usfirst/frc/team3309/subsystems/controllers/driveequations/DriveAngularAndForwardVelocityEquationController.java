package org.usfirst.frc.team3309.subsystems.controllers.driveequations;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.PIDVelocityController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.driverstation.Controls;

public class DriveAngularAndForwardVelocityEquationController extends Controller {
	// Ang Vel Control
	private double aimAngularVelocity = 0.0;
	private final double MAX_ANGULAR_VELOCITY = 720;
	private PIDVelocityController angController = new PIDVelocityController(.4, 0, .01);

	// Left Vel Control
	private double aimLeftVelocity = 0.0;
	private final double MAX_LEFT_VELOCITY = 720;
	private PIDVelocityController leftController = new PIDVelocityController(.4, 0, .01);

	// Right Vel Control
	private double aimRightVelocity = 0.0;
	private final double MAX_RIGHT_VELOCITY = 720;
	private PIDVelocityController rightController = new PIDVelocityController(.4, 0, .01);

	@Override
	public void reset() {

	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		aimAngularVelocity = MAX_ANGULAR_VELOCITY * Controls.driverController.getLeftX();
		InputState inputForLeftVel = inputState;
		InputState inputForAng = inputState;
		inputForLeftVel.setError(aim);
		inputForAng.setError(aimAngularVelocity - inputState.getAngularVel());
		OutputSignal angularOutput = angController.getOutputSignal(inputState);
		OutputSignal signal = new OutputSignal();
		signal.setLeftMotor(Controls.driverController.getLeftY() - angularOutput.getMotor());
		signal.setRightMotor(Controls.driverController.getLeftY() + angularOutput.getMotor());
		return signal;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

}
