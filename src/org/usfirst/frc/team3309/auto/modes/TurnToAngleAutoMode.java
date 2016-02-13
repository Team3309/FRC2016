package org.usfirst.frc.team3309.auto.modes;

import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;

public class TurnToAngleAutoMode extends AutoRoutine{

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		mDrive.setAngleSetpoint(359);
		System.out.println("Starting Auto");
		this.waitForDrive(6000000);
		System.out.println("DONE TURNING");
		this.waitForEndOfAuto();
		System.out.println("AUTO HAS ENDED");
	}

}
