package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;

public class SpyBotAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		mDrive.setHighGear(true);
		DriveEncodersVelocityController x = new DriveEncodersVelocityController(75);
		x.setMAX_ENCODER_VEL(100);
		Drive.getInstance().setAutoController(x);
		try {
			this.waitForController(x, 7);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		Thread.sleep(500);
		DriveAngleVelocityController turnToGoal = new DriveAngleVelocityController(mDrive.getAngle() - 77.53309);
		Drive.getInstance().setAutoController(turnToGoal);
		
		try {
			this.waitForController(turnToGoal, 4);
		} catch (Exception e) {

		}
		mDrive.stopDrive();
		
		Sensors.resetDrive();
		DriveEncodersVelocityController toGo = new DriveEncodersVelocityController(55);
		x.setMAX_ENCODER_VEL(100);
		Drive.getInstance().setAutoController(toGo);
		try {
			this.waitForController(toGo, 7);
		} catch (Exception e) {

		}
		mDrive.stopDrive();
	}

}
