package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.DriveEncodersController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;

/**
 * 
 * @author Krager Move Forward into the other zone.
 */
public class ForwardIntoZoneMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		DriveEncodersController x = new DriveEncodersController(Drive.getInstance(), 5000);
		mDrive.setTeleopController(x);
		this.waitForDrive(6000);
	}
}