package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;

public class TurnInPlaceLAAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		mDrive.setHighGear(true);
		//double offset = SmartDashboard.getNumber("ANGLE I AM TURNING ( ADDED TO OTHER)");
		//mDrive.setHighGear(true);
		DriveAngleVelocityController angleVel = new DriveAngleVelocityController(this.mDrive.getAngle() + 200);
		angleVel.setCompletable(false);
		mDrive.setAutoController(angleVel);
		KragerTimer.delayMS(342515);
	}

}
