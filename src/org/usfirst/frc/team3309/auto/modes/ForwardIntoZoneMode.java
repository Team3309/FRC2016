package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.generic.PIDPositionController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.controllers.drive.DriveEncodersController;

public class ForwardIntoZoneMode extends AutoRoutine{

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		DriveEncodersController x = new DriveEncodersController(5000);
		mDrive.setController(x);
		this.waitForDrive(6000);
	}
}
