package org.usfirst.frc.team3309.auto.operations.defenses;

public class CrossRamparts extends Operation {

	@Override
	public void perform() {
		CrossRockWall cross = new CrossRockWall();
		cross.perform();
		this.driveEncoder(60, 400, 2);

	}
}