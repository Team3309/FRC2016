package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;

public class GoForwardStraightAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		Drive.getInstance().setHighGear(true);
		DriveEncodersVelocityController x = new DriveEncodersVelocityController(150);
		x.setRampUp(true);
		x.setMAX_ENCODER_VEL(70);
		Drive.getInstance().setAutoController(x);
		try {
			this.waitForController(x, 7);
		} catch (Exception e) {
			
		}
		mDrive.stopDrive();
		
		Sensors.resetDrive();
		DriveEncodersVelocityController goFast = new DriveEncodersVelocityController(132);
		goFast.setMAX_ENCODER_VEL(150);
		Drive.getInstance().setAutoController(goFast);
		try {
			this.waitForController(goFast, 7);
		} catch (Exception e) {
			
		}
		mDrive.stopDrive();

		DriveAngleVelocityController turnToGoal = new DriveAngleVelocityController(mDrive.getAngle() + 63.5);
		Drive.getInstance().setAutoController(turnToGoal);
		try {
			this.waitForController(turnToGoal, 4);
		} catch (Exception e) {

		}
		mDrive.stopDrive();
		Sensors.resetDrive();
		DriveEncodersVelocityController goTowardsGoal = new DriveEncodersVelocityController(110);
		goTowardsGoal.setMAX_ENCODER_VEL(150);
		Drive.getInstance().setAutoController(goTowardsGoal);
		
		try {
			this.waitForController(goTowardsGoal, 90);
		} catch (Exception e) {

		}
		mDrive.stopDrive();
		
		// NOW Get Back
		Thread.sleep(2000);
		Sensors.resetDrive();
		DriveEncodersVelocityController goTowardsGoalBack = new DriveEncodersVelocityController(-110);
		goTowardsGoalBack.setMAX_ENCODER_VEL(150);
		Drive.getInstance().setAutoController(goTowardsGoalBack);
		try {
			this.waitForController(goTowardsGoalBack, 90);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		DriveAngleVelocityController turnToGoalBack = new DriveAngleVelocityController(mDrive.getAngle() - 63.5);
		Drive.getInstance().setAutoController(turnToGoalBack);

		try {
			this.waitForController(turnToGoalBack, 4);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		Sensors.resetDrive();
		DriveEncodersVelocityController xBack = new DriveEncodersVelocityController(-290);
		xBack.setMAX_ENCODER_VEL(60);
		Drive.getInstance().setAutoController(xBack);
		try {
			this.waitForController(xBack, 7);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

	}

}
