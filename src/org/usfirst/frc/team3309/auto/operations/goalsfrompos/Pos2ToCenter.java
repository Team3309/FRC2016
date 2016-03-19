package org.usfirst.frc.team3309.auto.operations.goalsfrompos;

import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;

public class Pos2ToCenter extends Operation {
	@Override
	public void perform() throws InterruptedException, TimedOutException {
		mDrive.setHighGear(true);
		this.driveEncoder(95, 150, 5, true);
		Thread.sleep(500);
		this.turnToAngle(mDrive.getAngle() + 20, 4);
		this.driveEncoder(20, 100, 5, true);
		this.turnToAngle(mDrive.getAngle() - 20, 4);
		this.toVision(100);
	}
}
