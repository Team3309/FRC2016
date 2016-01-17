package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.DriveEncodersController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;

/**
 * 
 * @author Krager Move Forward into the other zone.
 */
public class ForwardIntoZoneMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		DriveEncodersController x = new DriveEncodersController(5000);
		mDrive.setController(x);
		this.waitForDrive(6000);
	}
}