package org.team3309.lib.controllers.drive;

import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveEncodersVelocityController extends Controller {

	private FeedForwardWithPIDController leftSideController = new FeedForwardWithPIDController(.006, 0, .003, .001, 0);
	private FeedForwardWithPIDController rightSideController = new FeedForwardWithPIDController(.006, 0, .003, .001, 0);
	private PIDPositionController encodersController = new PIDPositionController(1, 0, 13.015);
	private double goalAngle = 0;

	public DriveEncodersVelocityController() {
		goalAngle = Sensors.getAngle();
		this.setName("DRIVE ENCODER VEL");
		this.leftSideController.setName("LEFT SIDE VEL CONTROLER");
		this.rightSideController.setName("RIGHT SIDE VEL CONTROLER");
		this.encodersController.setName("ENCODER Controller");
		SmartDashboard.putNumber(this.getName() + " Vel to Go At", 0);
		// SmartDashboard.putNumber("" , value);
	}

	@Override
	public void reset() {
		goalAngle = Sensors.getAngle();
		encodersController.reset();
		rightSideController.reset();
		leftSideController.reset();
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double error = goalAngle - inputState.getAngularPos();
		double dashAimTurnVel = SmartDashboard.getNumber(this.getName() + " Vel to Turn At");
		if (Math.abs(error) > 180) {
			error = -KragerMath.sign(error) * (360 - Math.abs(error));
			System.out.println("New Error: " + error);
		}
		SmartDashboard.putNumber("Goal Angle", goalAngle);
		InputState state = new InputState();
		state.setError(error); // sets angle error to be sent in turning PID
		OutputSignal outputOfTurningController = encodersController.getOutputSignal(state); // outputs
		SmartDashboard.putNumber("DRIVE ANGLE VEL Output", outputOfTurningController.getMotor());
		OutputSignal toBeReturnedSignal = new OutputSignal();
		InputState leftState = new InputState();
		InputState rightState = new InputState();
		leftState.setError(outputOfTurningController.getMotor() - inputState.getLeftVel());
		rightState.setError(outputOfTurningController.getMotor() - inputState.getRightVel());
		// leftSideController.setAimVel(dashAimTurnVel);
		// rightSideController.setAimVel(dashAimTurnVel);
		// leftState.setError(dashAimTurnVel - inputState.getLeftVel());
		// rightState.setError(dashAimTurnVel - inputState.getRightVel());
		toBeReturnedSignal.setLeftRightMotor(leftSideController.getOutputSignal(leftState).getMotor(),
				rightSideController.getOutputSignal(rightState).getMotor());

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
		encodersController.sendToSmartDash();
	}

}
