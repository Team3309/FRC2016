package org.team3309.lib.controllers.drive.equations;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.PIDVelocityController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.subsystems.Drive;

public class DriveAngularVelocityEquationController extends Controller {
	public DriveAngularVelocityEquationController(ControlledSubsystem subsystem) {
		super(subsystem);
	}

	public DriveAngularVelocityEquationController(ControlledSubsystem subsystem, boolean hasSeparateThread) {
		super(subsystem, hasSeparateThread);
	}

	private double aimAngularVelocity = 0.0;
	private final double MAX_ANGULAR_VELOCITY = 720;
	private PIDVelocityController angController = new PIDVelocityController(Drive.getInstance(), .4, 0, .01);

	@Override
	public void reset() {

	}

	@Override
	public OutputSignal getOutputSignal() {
		return super.getOutputSignal();
	}

	@Override
	public void update(InputState inputState) {
		aimAngularVelocity = MAX_ANGULAR_VELOCITY * Controls.driverController.getLeftX();
		inputState.setError(aimAngularVelocity - inputState.getAngularVel());
		angController.update(inputState);
		OutputSignal angularOutput = angController.getOutputSignal();
		OutputSignal signal = new OutputSignal();
		signal.setLeftMotor(Controls.driverController.getLeftY() - angularOutput.getMotor());
		signal.setRightMotor(Controls.driverController.getLeftY() + angularOutput.getMotor());
		this.lastOutputState = signal;
	}

	@Override
	public boolean isCompleted() {
		return false;
	}

}
