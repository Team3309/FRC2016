package org.team3309.lib.controllers.drive;

import org.team3309.lib.KragerTimer;
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
	private KragerTimer doneTimer = new KragerTimer(.5);

	private double goalAngle = 0;
	private double goalEncoder = 0;
	private double pastAim = 0;
	private final double MAX_ACC = 5;
	private double MAX_ENCODER_VEL = 100;
	private boolean isRampUp = false;

	public DriveEncodersVelocityController(double encoderGoal) {
		System.out.println("IS LOW GEAR: " + Drive.getInstance().isLowGear());
		if (Drive.getInstance().isLowGear()) {
			encodersController.setConstants(2, 0, 1.015);

			turningController.setConstants(.06, 0, 0);
			leftSideController.setConstants(.006, 0, .003, .001, 0);
			rightSideController.setConstants(.006, 0, .004, .001, 0);
		} else {
			encodersController.setConstants(2.9, 0, .215);
			turningController.setConstants(.04, 0, 0);
			leftSideController.setConstants(.006, 0, .009, .001, 0);
			rightSideController.setConstants(.006, 0, .009, .001, 0);
		}
		System.out.println("IS LOW GEAR: " + Drive.getInstance().isLowGear());
		turningController.printConstants();
		leftSideController.printConstants();
		rightSideController.printConstants();
		encodersController.printConstants();
		goalAngle = Sensors.getAngle();
		// if (Drive.getInstance().isLowGear())
		this.goalEncoder = encoderGoal;
		this.setName("DRIVE ENCODER VEL");
		this.leftSideController.setName("LEFT IDE VEL CONTROLER");
		this.rightSideController.setName("RIGHT IDE VEL CONTROLER");
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
		double currentEncoder = (Math.abs(inputState.getRightPos()) + Math.abs(inputState.getLeftPos())) / 2;
		if (goalEncoder < 0) {
			currentEncoder = -currentEncoder;
		}

		double error = goalEncoder - currentEncoder;
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
		System.out.println("DRIVE: ");
		turningController.printConstants();
		leftSideController.printConstants();
		rightSideController.printConstants();
		encodersController.printConstants();
		if (Math.abs(outputOfTurningController.getMotor()) > MAX_ENCODER_VEL) {
			if (outputOfTurningController.getMotor() > 0) {
				outputOfTurningController.setMotor(MAX_ENCODER_VEL);
			} else {
				outputOfTurningController.setMotor(-MAX_ENCODER_VEL);
			}
		}
		double aimVel = outputOfTurningController.getMotor();
		if (isRampUp) {
			if (aimVel < 0)
				aimVel = pastAim - MAX_ACC;
			else
				aimVel = pastAim + MAX_ACC;
			if (Math.abs(aimVel) > Math.abs(this.MAX_ENCODER_VEL)) {
				if (aimVel > 0) {
					aimVel = MAX_ENCODER_VEL;
				} else {
					aimVel = -this.MAX_ENCODER_VEL;
				}
				isRampUp = false;
			}
		}
		leftState.setError(aimVel - inputState.getLeftVel());
		rightState.setError(-aimVel - inputState.getRightVel());
		// leftSideController.setAimVel(dashAimTurnVel);
		// rightSideController.setAimVel(-dashAimTurnVel);
		// leftState.setError(dashAimTurnVel - inputState.getLeftVel());
		// rightState.setError(-dashAimTurnVel - inputState.getRightVel());
		InputState turningState = new InputState();
		turningState.setError(goalAngle - inputState.getAngularPos());
		double turn = turningController.getOutputSignal(turningState).getMotor();
		double sideOutput = leftSideController.getOutputSignal(leftState).getMotor();
		toBeReturnedSignal.setLeftRightMotor(sideOutput + turn, sideOutput - turn);
		pastAim = aimVel;
		return toBeReturnedSignal;
	}

	public double getGoalAngle() {
		return goalAngle;
	}

	public void setGoalAngle(double goalAngle) {
		this.goalAngle = goalAngle;
	}

	public double getGoalEncoder() {
		return goalEncoder;
	}

	public void setGoalEncoder(double goalEncoder) {
		this.goalEncoder = goalEncoder;
	}

	public double getMAX_ENCODER_VEL() {
		return MAX_ENCODER_VEL;
	}

	public void setMAX_ENCODER_VEL(double mAX_ENCODER_VEL) {
		MAX_ENCODER_VEL = mAX_ENCODER_VEL;
	}

	public void setRampUp(boolean bool) {
		this.isRampUp = bool;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return doneTimer.isConditionMaintained(Drive.getInstance().isEncoderCloseTo(goalEncoder));
	}

	public void sendToSmartDash() {
		System.out.println("FDASFDSAFDSAFASD");
		leftSideController.sendToSmartDash();
		rightSideController.sendToSmartDash();
		encodersController.sendToSmartDash();
		turningController.sendToSmartDash();
	}

}
