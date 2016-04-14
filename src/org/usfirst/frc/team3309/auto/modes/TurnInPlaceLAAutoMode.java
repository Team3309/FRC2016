package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.drive.DriveAngleController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnInPlaceLAAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		mDrive.setHighGear(true);
		double offset = SmartDashboard.getNumber("ANGLE I AM TURNING ( ADDED TO OTHER)");
		//mDrive.setHighGear(true);
		DriveAngleController angleVel = new DriveAngleController(Drive.getInstance(), this.mDrive.getAngle() + offset);
		angleVel.setCompletable(false);
		mDrive.setAutoController(angleVel);
		KragerTimer.delayMS(342515);
	}

}
