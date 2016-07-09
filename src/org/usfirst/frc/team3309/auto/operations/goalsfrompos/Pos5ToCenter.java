package org.usfirst.frc.team3309.auto.operations.goalsfrompos;

import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;

public class Pos5ToCenter extends Operation {

	@Override
	public void perform() throws InterruptedException, TimedOutException {
		this.turnToAngle(mDrive.getAngle() - 30, 4);
		this.toVisionLong(2000);

	}

}
