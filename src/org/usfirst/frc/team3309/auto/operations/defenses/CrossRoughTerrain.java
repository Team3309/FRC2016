package org.usfirst.frc.team3309.auto.operations.defenses;

import java.util.LinkedList;

import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;

public class CrossRoughTerrain extends Operation{

	@Override
	public void perform() {
		CrossRockWall cross = new CrossRockWall();
		cross.perform();
		this.driveEncoder(50, 50, 1); // stopping effect
	}
	
}
