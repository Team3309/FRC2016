package org.team3309.lib.controllers.drive;

import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveEncodersVelocityController extends Controller {

	private FeedForwardWithPIDController leftSideController = new FeedForwardWithPIDController(.006, 0, .003, .001, 0);
	private FeedForwardWithPIDController rightSideController = new FeedForwardWithPIDController(.006, 0, .004, .001, 0);
	private PIDPositionController encodersController = new PIDPositionController(1, 0, 0);
	private PIDPositionController turningController = new PIDPositionController(.06, 0, 0);
	private double goalAngle = 0;
	private double goalEncoder = 0;

	public DriveEncodersVelocityController(double encoderGoal) {
		if (Drive.getInstance().isLowGear()) {
			encodersController.setConstants(2, 0, 1.015);
			turningController.setConstants(.06, 0, 0);
			leftSideController.setConstants(.006, 0, .003, .001, 0);
			rightSideController.setConstants(.006, 0, .004, .001, 0);
		}else {
			encodersController.setConstants(2, 0, 1.015);
			turningController.setConstants(.06, 0, 0);
			leftSideController.setConstants(.006, 0, .003, .001, 0);
			rightSideController.setConstants(.006, 0, .004, .001, 0);
		}
		goalAngle = Sensors.getAngle();
		//if (Drive.getInstance().isLowGear())
		this.goalEncoder = encoderGoal;
		this.setName("DRIVE ENCODER VEL");
		this.leftSideController.setName("LEFT SIDE VEL CONTROLER");
		this.rightSideController.setName("RIGHT SIDE VEL CONTROLER");
		this.encodersController.setName("ENCODER Controller");
		this.turningController.setName("Turning Controller");
		SmartDashboard.putNumber(this.getName() + " Vel to Go At", 0);
	}

	@Override
	public void reset() {
		goalAngle = Sensors.getAngle();
		encodersController.reset();
		rightSideController.reset();
		leftSideController.reset();
		turningController.reset();
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double error = goalEncoder - (Math.abs(inputState.getRightPos()) + Math.abs(inputState.getLeftPos())) / 2;
		double dashAimTurnVel = SmartDashboard.getNumber(this.getName() + " Vel to Go At");
		/*
		 * if (Math.abs(error) > 180) { error = -KragerMath.sign(error) * (360 -
		 * Math.abs(error)); System.out.println("New Error: " + error); }
		 */
		SmartDashboard.putNumber("Goal Angle", goalAngle);
		InputState state = new InputState();
		state.setError(error); // sets angle error to be sent in turning PID
		OutputSignal outputOfTurningController = encodersController.getOutputSignal(state); // outputs
		SmartDashboard.putNumber("DRIVE Encoder VEL Output", outputOfTurningController.getMotor());
		OutputSignal toBeReturnedSignal = new OutputSignal();
		InputState leftState = new InputState();
		InputState rightState = new InputState();
		if (Math.abs(outputOfTurningController.getMotor()) > 150) {
			if (outputOfTurningController.getMotor() > 0) {
				outputOfTurningController.setMotor(150);
			} else {
				outputOfTurningController.setMotor(-150);
			}
		}
		leftState.setError(outputOfTurningController.getMotor() - inputState.getLeftVel());
		rightState.setError(-outputOfTurningController.getMotor() - inputState.getRightVel());
		// leftSideController.setAimVel(dashAimTurnVel);
		// rightSideController.setAimVel(-dashAimTurnVel);
		// leftState.setError(dashAimTurnVel - inputState.getLeftVel());
		// rightState.setError(-dashAimTurnVel - inputState.getRightVel());
		InputState turningState = new InputState();
		turningState.setError(goalAngle - inputState.getAngularPos());
		double turn = turningController.getOutputSignal(turningState).getMotor();
		double sideOutput = leftSideController.getOutputSignal(leftState).getMotor();
		toBeReturnedSignal.setLeftRightMotor(sideOutput + turn, sideOutput - turn);

		return toBeReturnedSignal;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return Drive.getInstance().isEncoderCloseTo(goalEncoder);
	}

	public void sendToSmartDash() {
		leftSideController.sendToSmartDash();
		rightSideController.sendToSmartDash();
		encodersController.sendToSmartDash();
		turningController.sendToSmartDash();
	}

}
