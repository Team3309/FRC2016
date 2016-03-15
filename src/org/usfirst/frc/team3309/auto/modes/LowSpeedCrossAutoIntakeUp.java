package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.generic.OnlyPowerController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

public class LowSpeedCrossAutoIntakeUp extends AutoRoutine{

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		IntakePivot.getInstance().toUpPosition();
		mDrive.setHighGear(false);
		// DriveEncodersControllerBasePower x = new
		// DriveEncodersControllerBasePower(25000, 4);
		// Drive.getInstance().setController(x);
		OnlyPowerController x = new OnlyPowerController();
		x.setLeftPower(.4);
		x.setRightPower(.42);
		Drive.getInstance().setTeleopController(x);
		Thread.sleep(6000);
		x.setLeftPower(0);
		x.setRightPower(0);
		mDrive.setTeleopController(x);
		mDrive.stopDrive();
		//this.waitForDrive(10000000);
		System.out.println("Auto is done");
		
	}

}
