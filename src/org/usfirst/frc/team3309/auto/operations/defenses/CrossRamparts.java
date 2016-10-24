package org.usfirst.frc.team3309.auto.operations.defenses;

import java.util.LinkedList;

import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.DriveWhileOnADefenseController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

public class CrossRamparts extends Operation {

	@Override
	public void perform() {
		mDrive.setHighGear(false);
		double starting = Sensors.getAngle();
		IntakePivot.getInstance().toUpPosition();
		LinkedList<VelocityChangePoint> a = new LinkedList<VelocityChangePoint>();
		a.add(new VelocityChangePoint(400, 40));
		a.add(new VelocityChangePoint(400, 50));
		a.add(new VelocityChangePoint(4000, 250));
		DriveEncodersVelocityController x = new DriveEncodersVelocityController(1200);
		x.setMAX_ENCODER_VEL(250);
		x.setEncoderChanges(a);
		mDrive.setAutoController(x);

		try {
			this.waitForController(x, 2);
		} catch (TimedOutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DriveAngleVelocityController turnBackToAngle = new DriveAngleVelocityController(starting);
		mDrive.setAutoController(turnBackToAngle);
		try {
			this.waitForController(turnBackToAngle, 2);
		} catch (TimedOutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// DriveWhileOnADefenseController driveOverWall = new
		// DriveWhileOnADefenseController(x);
		// mDrive.setAutoController(driveOverWall);
		// try {
		// this.waitForController(driveOverWall, 50);
		// } catch (Exception e) {
		// }
		this.driveEncoder(20, 40, 1);

	}
}