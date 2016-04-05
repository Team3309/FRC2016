package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.DriveEncodersControllerBasePower;
import org.team3309.lib.controllers.generic.OnlyPowerController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

public class LowBarAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		IntakePivot.getInstance().toIntakePosition();
		mDrive.setHighGear(true);
		//KragerTimer.delayMS(1000);
		// BlankController blankController = new BlankController();
		// blankController.setPower(.4);

		DriveEncodersControllerBasePower x = new DriveEncodersControllerBasePower(23000, .5);
		mDrive.setTeleopController(x);

		IntakePivot.getInstance().toIntakePosition();

		/*
		 * OnlyPowerController x = new OnlyPowerController();
		 * x.setLeftPower(.4); x.setRightPower(.4);
		 * Drive.getInstance().setController(x); //
		 * mDrive.setController(blankController); // KragerTimer.delayMS(4000);
		 * KragerTimer.delayMS(6000); x.setLeftPower(0); x.setRightPower(0);
		 * mDrive.setController(x);
		 */
		//mDrive.stopDrive();

		try {
			this.waitForDrive(6000);
			System.out.println("Completed");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//IntakePivot.getInstance().toUpPosition();

		// blankController.setPower(0);
		// mDrive.setController(blankController);
		// KragerTimer.delayMS(1000);
		/*
		 * while (mDrive.getDistanceTraveled() < 23000) {
		 * mDrive.setLeftRight(.4, .4); }
		 * 
		 */
	}

}
