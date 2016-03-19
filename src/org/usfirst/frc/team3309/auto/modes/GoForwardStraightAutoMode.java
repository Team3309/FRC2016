package org.usfirst.frc.team3309.auto.modes;

import java.util.LinkedList;

import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.auto.operations.shooter.SetRPSAndHoodOperation;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

public class GoForwardStraightAutoMode extends AutoRoutine {

	private double DISTANCE_TO_GOAL = 55;

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		Drive.getInstance().setHighGear(true);
		double startAngle = mDrive.getAngle();
		Sensors.resetDrive();
		DriveEncodersVelocityController goFast = new DriveEncodersVelocityController(395); // 282,
																							// 340
		goFast.setRampUp(true);
		goFast.setMAX_ENCODER_VEL(80);
		LinkedList<VelocityChangePoint> w = new LinkedList<VelocityChangePoint>();
		w.add(new VelocityChangePoint(50, 100));
		w.add(new VelocityChangePoint(150, 150));
		w.add(new VelocityChangePoint(80, 150, 220));
		w.add(new VelocityChangePoint(150, 330));
		goFast.setEncoderChanges(w);

		LinkedList<Operation> operations = new LinkedList<Operation>();
		operations.add(new SetRPSAndHoodOperation(200, 140, 30));
		goFast.setOperations(operations);

		Drive.getInstance().setAutoController(goFast);
		try {
			this.waitForController(goFast, 10);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		/*
		 * DriveAngleVelocityController turnToGoal = new
		 * DriveAngleVelocityController(mDrive.getAngle() + 63.5);
		 * Drive.getInstance().setAutoController(turnToGoal); try {
		 * this.waitForController(turnToGoal, 4); } catch (Exception e) {
		 * 
		 * }
		 */
		double angleToGoBack = mDrive.getAngle();
		Flywheel.getInstance().setAimVelRPSAuto(140);
		Hood.getInstance().setGoalAngle(30);
		mDrive.stopDrive();
		/*
		 * Thread.sleep(500); Sensors.resetDrive();
		 * DriveEncodersVelocityController goTowardsGoal = new
		 * DriveEncodersVelocityController(DISTANCE_TO_GOAL);
		 * goTowardsGoal.setMAX_ENCODER_VEL(150);
		 * Drive.getInstance().setAutoController(goTowardsGoal);
		 * 
		 * try { this.waitForController(goTowardsGoal, 90); } catch (Exception
		 * e) {
		 * 
		 * } mDrive.stopDrive();
		 */

		Thread.sleep(1000);
		// NOW Get Back
		double angleBeforeVision = mDrive.getAngle();
		this.toVision(20);
		double errorFromStartingVision = mDrive.getAngle() - angleBeforeVision;
		System.out.println("HERS SOME STUFF Vision: " + angleBeforeVision + " dis: " + this.DISTANCE_TO_GOAL
				+ " angleBeforeVision: " + angleBeforeVision + " error from start " + errorFromStartingVision);
		double distanceToGoBack = (KragerMath.sinDeg(angleBeforeVision) * 110)
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
		xBack.setMAX_ENCODER_VEL(120);
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
