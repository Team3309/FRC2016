package org.usfirst.frc.team3309.auto;

import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.subsystems.Drive;

public class CustomAuto extends AutoRoutine {

	private Operation defense;
	private int startingPosition = 1;
	private double[] turningAngles = { 0, 63.5, -30, -10, 10, 45 }; // Give it the
																// startingPosition

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		defense.perform();
		Thread.sleep(500);
		DriveAngleVelocityController turnToGoal = new DriveAngleVelocityController(
				mDrive.getAngle() + turningAngles[startingPosition]);
		Drive.getInstance().setAutoController(turnToGoal);

		try {
			this.waitForController(turnToGoal, 4);
		} catch (Exception e) {

		}
		mDrive.stopDrive();
	}

	public Operation getDefense() {
		return defense;
	}

	public void setDefense(Operation defense) {
		this.defense = defense;
	}

	public int getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(int startingPosition) {
		this.startingPosition = startingPosition;
	}

}
