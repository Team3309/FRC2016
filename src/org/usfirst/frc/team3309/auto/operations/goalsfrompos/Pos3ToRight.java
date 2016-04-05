package org.usfirst.frc.team3309.auto.operations.goalsfrompos;

import org.team3309.lib.KragerTimer;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;

public class Pos3ToRight extends Operation{

	@Override
	public void perform() throws InterruptedException, TimedOutException {
		mDrive.setHighGear(true);
		this.driveEncoder(72, 150, 5, true);
		KragerTimer.delayMS(500);
		this.turnToAngle(mDrive.getAngle() + 15, 4);
		this.toVision(100);
		
	}

}
