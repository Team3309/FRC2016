package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.generic.BlankController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;

public class LowBarAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		BlankController blankController = new BlankController();
		blankController.setPower(4);
		mDrive.setController(blankController);
		Thread.sleep(4000);
		blankController.setPower(0);
		mDrive.setController(blankController);
	}

}
