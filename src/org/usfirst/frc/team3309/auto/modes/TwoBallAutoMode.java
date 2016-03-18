package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

public class TwoBallAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		double firstAngle = Sensors.getAngle();
		// TODO Auto-generated method stub
		Drive.getInstance().setHighGear(true);

		DriveEncodersVelocityController x = new DriveEncodersVelocityController(230);
		x.setRampUp(true);
		x.setMAX_ENCODER_VEL(80);
		Drive.getInstance().setAutoController(x);
		try {
			this.waitForController(x, 7);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		DriveAngleVelocityController turnToGoal = new DriveAngleVelocityController(mDrive.getAngle() + 45.5);
		Drive.getInstance().setAutoController(turnToGoal);
		try {
			this.waitForController(turnToGoal, 4);
		} catch (Exception e) {

		}
		mDrive.stopDrive();
		
		// NOW Get Back
		Shot shot = Vision.getInstance().getShot();
		while (shot == null) {
			shot = Vision.getInstance().getShot();
			System.out.println("LOOKING");
		}

		mDrive.toVision();
		System.out.println("RPS: " + shot.getGoalRPS() + " angke: " + shot.getGoalHoodAngle());

		Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
		Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle());
		Thread.sleep(500);
		mDrive.toVision();
		shot = Vision.getInstance().getShot();
		Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
		Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle());
		Thread.sleep(500);
		shot = Vision.getInstance().getShot();
		Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
		Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle());
		Thread.sleep(500);
		System.out.println("BANG");
		Thread.sleep(1000);
		System.out.println("BANG BANG");
		Thread.sleep(2000);
		FeedyWheel.getInstance().setFeedyWheel(1);
		Thread.sleep(500);
		FeedyWheel.getInstance().setFeedyWheel(0);
		
		DriveAngleVelocityController turnToStartingPos = new DriveAngleVelocityController(firstAngle);
		Drive.getInstance().setAutoController(turnToStartingPos);
		try {
			this.waitForController(turnToStartingPos, 4);
		} catch (Exception e) {

		}
		
		DriveEncodersVelocityController back = new DriveEncodersVelocityController(-230);
		//x.setRampUp(true);
		back.setMAX_ENCODER_VEL(80);
		Drive.getInstance().setAutoController(back);
		try {
			this.waitForController(back, 7);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

	}

}
