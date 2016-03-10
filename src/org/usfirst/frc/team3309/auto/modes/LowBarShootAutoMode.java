package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.drive.FaceVisionTargetController;
import org.team3309.lib.controllers.generic.BlankController;
import org.team3309.lib.controllers.generic.OnlyPowerController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

public class LowBarShootAutoMode extends AutoRoutine {

	private double firstAngle = 0;

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		firstAngle = mDrive.getAngle();
		LowBarAutoMode x = new LowBarAutoMode();
		x.routine();
		System.out.println("Done going Under");
		/*
		 * while (Math.abs(Sensors.getLeftDrive()) < 8000) { mDrive.setLeft(.4);
		 * mDrive.setRight(.2); }
		 */

		Sensors.resetDrive();
		OnlyPowerController startTurning = new OnlyPowerController();
		startTurning.setLeftPower(.8);
		startTurning.setRightPower(.4);
		mDrive.setController(startTurning);

		while (Math.abs(Sensors.getLeftDrive()) < 13350) {
			Thread.sleep(30);
		}
		mDrive.setController(new BlankController());
		/*
		 * mDrive.setAngleSetpoint(mDrive.getAngle() + 45); try {
		 * this.waitForDrive(15000); } catch (Exception e) {
		 * 
		 * } System.out.println("Turned T"); DriveEncodersControllerBasePower
		 * goForwardToGoCloserToGoal = new
		 * DriveEncodersControllerBasePower(4000, .4);
		 * mDrive.setController(goForwardToGoCloserToGoal); try {
		 * this.waitForDrive(-2000); } catch (Exception e) {
		 * 
		 * } System.out.println("Went Forward A little more");
		 */
		Thread.sleep(1000);
		double counter = 0;
		Shot goal = Vision.getInstance().getShot();
		while (goal == null) {
			System.out.println("Where are you Goal??");
			if (counter > 4)
				mDrive.setAngleSetpoint(mDrive.getAngle() + 20);
			else
				mDrive.setAngleSetpoint(mDrive.getAngle() - 20);
			this.waitForDrive(3000);
			goal = Vision.getInstance().getShot();
			counter++;
		}
		mDrive.stopDrive();

		mDrive.toVision();
		// mDrive.setAngleSetpoint(goal.getAzimuth());
		Thread.sleep(200);
		goal = Vision.getInstance().getShot();
		Flywheel.getInstance().setAimVelRPSAuto(goal.getGoalRPS());
		Hood.getInstance().setGoalAngle(goal.getGoalHoodAngle());
		Thread.sleep(1000);
		goal = Vision.getInstance().getShot();
		Flywheel.getInstance().setAimVelRPSAuto(goal.getGoalRPS());
		Hood.getInstance().setGoalAngle(goal.getGoalHoodAngle());
		mDrive.toVision();
		Thread.sleep(500);
		mDrive.toVision();
		Thread.sleep(500);
		mDrive.toVision();
		System.out.println("FEEDY IS RUNNING");

		while (!Flywheel.getInstance().isOnTarget()) {

		}
		Thread.sleep(1000);
		FeedyWheel.getInstance().setFeedyWheelAuto(-.9);
		Thread.sleep(1000);
		FeedyWheel.getInstance().setFeedyWheelAuto(0);

	}

}
