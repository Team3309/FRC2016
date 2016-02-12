package org.usfirst.frc.team3309.auto;

import org.usfirst.frc.team3309.auto.operations.defenses.Operation;

public class CustomAuto extends AutoRoutine {

	private Operation defense;
	private int startingPosition = 1;
	private int[] turningAngles = { 0, -45, -30, -10, 10, 45 }; // Give it the
																// startingPosition

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		// Perform Defense
		defense.perform();
	
		
		
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
