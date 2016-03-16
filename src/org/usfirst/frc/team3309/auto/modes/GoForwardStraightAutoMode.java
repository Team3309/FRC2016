package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;

public class GoForwardStraightAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		DriveEncodersVelocityController x = new DriveEncodersVelocityController(700);
		Drive.getInstance().setAutoController(x);
		try {
			waitForDrive(7);
		} catch (Exception e) {

		}
		mDrive.stopDrive();
		System.out.println("NEXT ");
		// mDrive.setHighGear(true);
		DriveAngleVelocityController angleVel = new DriveAngleVelocityController(this.mDrive.getAngle() + 90);
		mDrive.setAutoController(angleVel);
		this.waitForDrive(6000);
		mDrive.stopDrive();
		// Thread.sleep(342515);
		System.out.println("Auto is done");
	}

}
