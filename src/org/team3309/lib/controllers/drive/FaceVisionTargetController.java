package org.team3309.lib.controllers.drive;

import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FaceVisionTargetController extends DriveAngleController {

	private boolean sendToDash = true;
	private Shot aimShot;

	public FaceVisionTargetController(double kP, double kI, double kD) {
		super(Sensors.getAngle());
		if (Vision.getInstance().getShot() != null) {
			this.setTIME_TO_BE_COMPLETE_MILLISECONDS(450);
			aimShot = Vision.getInstance().getShot();
			this.setTHRESHOLD(.3);
			this.setName("Turn To Vision");
			SmartDashboard.putNumber(this.getName() + " AIM delta ANGLE", aimShot.getAzimuth());
			this.goalAngle = Drive.getInstance().getAngle() + aimShot.getAzimuth();

		} else {
			this.sendToDash = false;
			this.completable = true;
			this.goalAngle = Drive.getInstance().getAngle();
			System.out.println("THIS IS VERY BAD");
		}

		// this.goalAngle = Drive.getInstance().getAngle() + 5;

	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		// OutputSignal signal = new OutputSignal();
		/*
		 * List<Goal> currentGoals = Vision.getInstance().getGoals(); if
		 * (!currentGoals.isEmpty() && !isAimDecided) { azimuth =
		 * currentGoals.get(0).azimuth; this.goalAngle = Sensors.getAngle() +
		 * azimuth; isAimDecided = true; }
		 */
		/*
		 * if (!currentGoals.isEmpty()) { blankCounts = 0; this.completable =
		 * true; Goal curGoal = currentGoals.get(0); System.out.println("\nX: "
		 * + curGoal.x); System.out.println("current goals: " + currentGoals);
		 * inputState.setError(0 - curGoal.x); double output =
		 * super.getOutputSignal(inputState).getMotor(); System.out.println(
		 * "RUN AT THIS POWER: " + output + " (RIGHT)"); if (isFirstTime) {
		 * lastDirection = KragerMath.sign(output); }
		 * signal.setLeftRightMotor(-output, output); } else { blankCounts++;
		 * 
		 * if (blankCounts > 5) { this.reset(); System.out.println(""); // TIME
		 * TO FIND THE TARGET this.completable = false;
		 * signal.setLeftRightMotor(lastDirection * -.33, .33 * lastDirection);
		 * } }
		 */
		return super.getOutputSignal(inputState);
	}

	@Override
	public void sendToSmartDash() {
		if (this.sendToDash) {
			super.sendToSmartDash();
			SmartDashboard.putNumber(this.getName() + " AIM delta ANGLE", aimShot.getAzimuth());
		}
	}
}