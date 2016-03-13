package org.team3309.lib.controllers.drive;

import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAngleVelocityController extends Controller {
	private FeedForwardWithPIDController leftSideController = new FeedForwardWithPIDController(0, 0, 0, 0, 0);
	private FeedForwardWithPIDController rightSideController = new FeedForwardWithPIDController(0, 0, 0, 0, 0);
	private PIDPositionController turningController = new PIDPositionController(0, 0, 0);
	private double goalAngle = 0;

	public DriveAngleVelocityController(double aimAngle) {
		this.setName("DRIVE ANGLE VEL");
		this.leftSideController.setName("LEFT IDE VEL CONTROLER");
		this.rightSideController.setName("RIGHT IDE VEL CONTROLER");
		goalAngle = aimAngle;
		SmartDashboard.putNumber(this.getName() + " Vel to Turn At", 0);
		//SmartDashboard.putNumber("" , value);
	}

	@Override
	public void reset() {
		this.leftSideController.reset();
		this.rightSideController.reset();
		this.turningController.reset();

	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double error = goalAngle - inputState.getAngularPos();
		double dashAimTurnVel = SmartDashboard.getNumber(this.getName() + " Vel to Turn At");
		if (Math.abs(error) > 180) {
			error = -KragerMath.sign(error) * (360 - Math.abs(error));
			System.out.println("New Error: " + error);
		}
		SmartDashboard.putNumber("VISION ErRROR", error);
		InputState state = new InputState();
		state.setError(error); // sets angle error to be sent in turning PID
		OutputSignal outputOfTurningController = turningController.getOutputSignal(state); // outputs
																							// which
																							// "power"/ vel
																							// to
																							// send
		
		OutputSignal toBeReturnedSignal = new OutputSignal();
		InputState leftState = new InputState();
		InputState rightState = new InputState();
		// leftState.setError(outputOfTurningController.getMotor() -
		// inputState.getLeftVel());
		// rightState.setError(outputOfTurningController.getMotor() -
		// inputState.getRightVel());
		leftSideController.setAimVel(dashAimTurnVel);
		rightSideController.setAimVel(dashAimTurnVel);
		leftState.setError(dashAimTurnVel - inputState.getLeftVel());
		rightState.setError(dashAimTurnVel - inputState.getRightVel());
		toBeReturnedSignal.setLeftRightMotor(leftSideController.getOutputSignal(leftState).getMotor(),
				rightSideController.getOutputSignal(rightState).getRightMotor());

		return toBeReturnedSignal;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	public void sendToSmartDash() {
		leftSideController.sendToSmartDash();
		rightSideController.sendToSmartDash();
		turningController.sendToSmartDash();
	}
}
