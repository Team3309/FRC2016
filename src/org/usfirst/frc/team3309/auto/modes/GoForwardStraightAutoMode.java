package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.DriveEncodersController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;

public class GoForwardStraightAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		DriveEncodersController x = new DriveEncodersController(8000);
		Drive.getInstance().setTeleopController(x);
		this.waitForDrive(10000000);
		System.out.println("Auto is done");
	}

}
