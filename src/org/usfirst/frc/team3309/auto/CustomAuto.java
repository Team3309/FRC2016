package org.usfirst.frc.team3309.auto;

import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.auto.operations.goalsfrompos.Pos2ToLeft;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;

public class CustomAuto extends AutoRoutine {

	private Operation defense;
	private Operation startingPosition;

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		defense.perform();
		Thread.sleep(200);
		
		mDrive.setHighGear(true);
		Hood.getInstance().setGoalAngle(30);
		Flywheel.getInstance().setAimVelRPSAuto(140);
		Thread.sleep(750);
		startingPosition.perform();
		mDrive.stopDrive();
	}

	public Operation getDefense() {
		return defense;
	}

	public void setDefense(Operation defense) {
		this.defense = defense;
	}

	public Operation getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(Operation operation) {
		this.startingPosition = operation;
	}

}
