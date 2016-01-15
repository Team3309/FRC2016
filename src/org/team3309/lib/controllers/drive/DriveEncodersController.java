package org.team3309.lib.controllers.drive;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;

/**
 * Use this class to drive the robot an exact amount of encoders Uses three PID
 * controllers, one for angle, one for left, one for right.
 * 
 * @author Krager
 *
 */
public class DriveEncodersController {

	private PIDPositionController leftController = new PIDPositionController(.3, 0, 0);
	private PIDPositionController rightController = new PIDPositionController(.2, 0, 0);
	protected PIDPositionController angController = new PIDPositionController(.2, 0, 0);
	protected double goalEncoder;
	protected double goalAngle;

	public DriveEncodersController(double goal) {
		leftController.setName("left");
		rightController.setName("right");
		angController.setName("ang");
		goalEncoder = goal;
		goalAngle = Sensors.getAngle();
	}

	@Override
	public void reset() {

	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		// Input States for the three controllers
		InputState inputForLeftVel = inputState;
		InputState inputForRightVel = inputState;
		InputState inputForAng = inputState;
		// Add the error for each controller from the inputState
		inputForLeftVel.setError(goalEncoder - inputState.getLeftPos());
		inputForRightVel.setError(goalEncoder - inputState.getRightPos());
		inputForAng.setError(goalAngle - inputState.getAngularPos());
		OutputSignal leftOutput = leftController.getOutputSignal(inputForLeftVel);
		OutputSignal rightOutput = rightController.getOutputSignal(inputForRightVel);
		OutputSignal angularOutput = angController.getOutputSignal(inputState);
		// Prepare the output
		OutputSignal signal = new OutputSignal();
		signal.setLeftMotor(leftOutput.getMotor() - angularOutput.getMotor());
		signal.setRightMotor(rightOutput.getMotor() + angularOutput.getMotor());
		return signal;
	}

	@Override
	public boolean isCompleted() {
		return leftController.isCompleted() && rightController.isCompleted() && angController.isCompleted();
	}

	@Override
	public void sendToSmartDash() {
		leftController.sendToSmartDash();
		rightController.sendToSmartDash();
		angController.sendToSmartDash();
	}
}
