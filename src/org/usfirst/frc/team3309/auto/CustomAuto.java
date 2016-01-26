package org.usfirst.frc.team3309.auto;

public class CustomAuto extends AutoRoutine {

	

	private Defense defense;
	private int startingPosition = 1;
	private int[] turningAngles = { 0, -45, -30, -10, 10, 45 }; // Give it the
																// startingPosition

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		
	}

	public Defense getDefense() {
		return defense;
	}

	public void setDefense(Defense defense) {
		this.defense = defense;
	}

	public int getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(int startingPosition) {
		this.startingPosition = startingPosition;
	}

}
