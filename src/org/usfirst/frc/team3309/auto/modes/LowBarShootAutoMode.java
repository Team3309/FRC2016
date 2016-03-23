package org.usfirst.frc.team3309.auto.modes;

import java.util.LinkedList;

import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

public class LowBarShootAutoMode extends AutoRoutine {

	private double firstAngle = 0;
	private double DISTANCE_TO_GOAL = 110;

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		Drive.getInstance().setHighGear(true);
		IntakePivot.getInstance().toIntakePosition();
		double startAngle = mDrive.getAngle();
		Sensors.resetDrive();
		DriveEncodersVelocityController goFast = new DriveEncodersVelocityController(282); // 282
		goFast.setRampUp(true);
		goFast.setMAX_ENCODER_VEL(80);
		LinkedList<VelocityChangePoint> w = new LinkedList<VelocityChangePoint>();
		w.add(new VelocityChangePoint(50, 100));
		w.add(new VelocityChangePoint(150, 150));
		// w.add(new VelocityChangePoint(50, 100, 282));
		goFast.setEncoderChanges(w);

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
		double angleToGoBack = mDrive.getAngle();
		Flywheel.getInstance().setAimVelRPSAuto(140);
		Hood.getInstance().setGoalAngle(30);
		mDrive.stopDrive();
		Thread.sleep(250);
		Sensors.resetDrive();
		DriveEncodersVelocityController goTowardsGoal = new DriveEncodersVelocityController(DISTANCE_TO_GOAL);
		goTowardsGoal.setMAX_ENCODER_VEL(150);
		Drive.getInstance().setAutoController(goTowardsGoal);

		try {
			this.waitForController(goTowardsGoal, 90);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		// NOW Get Back
		Shot shot = Vision.getInstance().getShotToAimTowards();
		while (shot == null) {
			shot = Vision.getInstance().getShotToAimTowards();
			System.out.println("LOOKING");
		}
		double angleBeforeVision = mDrive.getAngle();

		this.toVision(6);

		/*
		 * mDrive.toVision(); System.out.println("RPS: " + shot.getGoalRPS() +
		 * " angke: " + shot.getGoalHoodAngle());
		 * 
		 * try { shot = Vision.getInstance().getShotToAimTowards();
		 * Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
		 * Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle());
		 * Thread.sleep(500); shot = Vision.getInstance().getShotToAimTowards();
		 * Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
		 * Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle()); } catch
		 * (Exception e) {
		 * 
		 * } Thread.sleep(1000); System.out.println("BANG BANG");
		 * FeedyWheel.getInstance().setFeedyWheel(1); double
		 * errorFromStartingVision = mDrive.getAngle() - angleBeforeVision;
		 * Thread.sleep(500); FeedyWheel.getInstance().setFeedyWheel(0);
		 * Flywheel.getInstance().setAimVelRPSAuto(0);
		 * Hood.getInstance().setGoalAngle(4);
		 */

		double errorFromStartingVision = mDrive.getAngle() - angleBeforeVision;
		System.out.println("HERS SOME STUFF Vision: " + angleBeforeVision + " dis: " + this.DISTANCE_TO_GOAL
				+ " angleBeforeVision: " + angleBeforeVision + " error from start " + errorFromStartingVision);
		double distanceToGoBack = (KragerMath.sinDeg(angleBeforeVision) * this.DISTANCE_TO_GOAL)
				/ (KragerMath.sinDeg(angleBeforeVision + errorFromStartingVision));
		Sensors.resetDrive();
		System.out.println("DISTANCE TO GO BACKL " + distanceToGoBack);
		DriveEncodersVelocityController goTowardsGoalBack = new DriveEncodersVelocityController(-distanceToGoBack - 5);
		goTowardsGoalBack.setMAX_ENCODER_VEL(150);
		Drive.getInstance().setAutoController(goTowardsGoalBack);
		try {
			this.waitForController(goTowardsGoalBack, 90);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		DriveAngleVelocityController turnToGoalBack = new DriveAngleVelocityController(startAngle);
		Drive.getInstance().setAutoController(turnToGoalBack);

		try {
			this.waitForController(turnToGoalBack, 4);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		Sensors.resetDrive();
		DriveEncodersVelocityController xBack = new DriveEncodersVelocityController(-230);
		xBack.setMAX_ENCODER_VEL(90);
		LinkedList<VelocityChangePoint> a = new LinkedList<VelocityChangePoint>();
		a.add(new VelocityChangePoint(60, 90));
		xBack.setEncoderChanges(a);
		Drive.getInstance().setAutoController(xBack);
		try {
			this.waitForController(xBack, 7);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

	}

}
