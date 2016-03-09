package org.usfirst.frc.team3309.auto.modes;

import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

public class LowBarShootAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		LowBarAutoMode x = new LowBarAutoMode();
		x.start();
		mDrive.setAngleSetpoint(mDrive.getAngle() + 25);
		this.waitForDrive(2000);
		while (Vision.getInstance().getShot() == null) {
			
		}
		mDrive.stopDrive();
		
		Shot goal = Vision.getInstance().getShot();
		mDrive.setAngleSetpoint(goal.getAzimuth());
		Flywheel.getInstance().aimVelRPS = goal.getGoalRPS();
		Hood.getInstance().setGoalAngle(goal.getGoalHoodAngle());
		
	}

}
