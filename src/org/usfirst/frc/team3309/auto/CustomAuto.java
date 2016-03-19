package org.usfirst.frc.team3309.auto;

import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.auto.operations.goalsfrompos.Pos2ToLeft;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;

public class CustomAuto extends AutoRoutine {

	private Operation defense;
	private int startingPosition = 1;

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		// defense.perform();
		// Thread.sleep(200);
		// this.driveEncoder(10, 40, 1); // stopping effect
		mDrive.setHighGear(true);
		Hood.getInstance().setGoalAngle(30);
		Flywheel.getInstance().setAimVelRPSAuto(140);
		Thread.sleep(750);
		(new Pos2ToLeft()).perform();
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
