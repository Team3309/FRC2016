package org.usfirst.frc.team3309.auto.operations.defenses;

import java.util.LinkedList;

import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.DriveWhileOnADefenseController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

public class CrossRockWall extends Operation {

	@Override
	public void perform() {
		mDrive.setHighGear(false);
		IntakePivot.getInstance().toUpPosition();
		LinkedList<VelocityChangePoint> a = new LinkedList<VelocityChangePoint>();
		a.add(new VelocityChangePoint(400, 40));
		a.add(new VelocityChangePoint(400, 50));
		a.add(new VelocityChangePoint(400, 60));
		DriveEncodersVelocityController x = new DriveEncodersVelocityController(Drive.getInstance(), 800);
		x.setMAX_ENCODER_VEL(150);
		x.setEncoderChanges(a);
		DriveWhileOnADefenseController driveOverWall = new DriveWhileOnADefenseController(Drive.getInstance(), x);
		mDrive.setAutoController(driveOverWall);
		try {
			this.waitForController(driveOverWall, 50);
		} catch (Exception e) {
		}
		this.driveEncoder(10, 40, 1); // stopping effect
		// super.driveEncoder(800, 150, 50, a);
	}

}
