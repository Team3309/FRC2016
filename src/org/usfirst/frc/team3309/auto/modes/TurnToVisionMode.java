package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.FaceVisionTargetController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;

public class TurnToVisionMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		FaceVisionTargetController x = new FaceVisionTargetController();
		Drive.getInstance().setTeleopController(x);
		waitForDrive(100000);
	}

}
