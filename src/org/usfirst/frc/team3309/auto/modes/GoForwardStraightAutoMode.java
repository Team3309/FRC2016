package org.usfirst.frc.team3309.auto.modes;

import java.util.LinkedList;

import org.team3309.lib.KragerMath;
import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.auto.operations.intakepivot.MoveIntakePivotToHigh;
import org.usfirst.frc.team3309.auto.operations.shooter.SetRPSAndHoodOperation;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.Timer;

public class GoForwardStraightAutoMode extends AutoRoutine {

	private double DISTANCE_TO_GOAL = 55;

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		Drive.getInstance().setHighGear(true);
		double startAngle = mDrive.getAngle();
		IntakePivot.getInstance().toIntakePosition();
		Sensors.resetDrive();
		DriveEncodersVelocityController goFast = new DriveEncodersVelocityController(395); // 282,
																							// 340
		goFast.setRampUp(true);
		goFast.setMAX_ENCODER_VEL(80);
		LinkedList<VelocityChangePoint> w = new LinkedList<VelocityChangePoint>();
		w.add(new VelocityChangePoint(50, 70));
		w.add(new VelocityChangePoint(150, 155));
		w.add(new VelocityChangePoint(90, 150, 230));
		w.add(new VelocityChangePoint(150, 330));
		goFast.setEncoderChanges(w);

		LinkedList<Operation> operations = new LinkedList<Operation>();
		// operations.add(new SetRPSAndHoodOperation(200, 140, 30));
		//operations.add(new MoveIntakePivotToHigh(150, true));
		goFast.setOperations(operations);

		Drive.getInstance().setAutoController(goFast);
		try {
			this.waitForController(goFast, 15);
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
		 * KragerTimer.delayMS(500); Sensors.resetDrive();
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

		KragerTimer.delayMS(50);
		// NOW Get Back
		double angleBeforeVision = mDrive.getAngle();

		/// THIS IS ALL VISION
		System.out.println("\n Vision Part \n");
		Shot shot = Vision.getInstance().getShotToAimTowards();
		Timer waitTimer = new Timer();
		waitTimer.start();
		double countInDirection = 0;
		double direction = 1; // left first
		double MAX_DIRECTION_TO_TURN = 3;
		while (shot == null) {
			if (waitTimer.get() > 20)
				throw new TimedOutException();
			this.turnToAngle(mDrive.getAngle() + (60 * direction), 1);
			countInDirection++;
			if (countInDirection == MAX_DIRECTION_TO_TURN) {
				countInDirection = 0;
				MAX_DIRECTION_TO_TURN = 7;
				direction = -direction;
			}
			KragerTimer.delayMS(200);
			shot = Vision.getInstance().getShotToAimTowards();

			// System.out.println("LOOKING");
		}

		mDrive.toVision();
		// System.out.println("RPS: " + shot.getGoalRPS() + " angle: " +
		// shot.getGoalHoodAngle());

		try {
			shot = Vision.getInstance().getShotToAimTowards();
			Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
			Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle());
			KragerTimer.delayMS(1000);
			mDrive.toVision();
			shot = Vision.getInstance().getShotToAimTowards();
			Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
			Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle());
		} catch (Exception e) {

		}
		KragerTimer.delayMS(3000);
		try {
			mDrive.toVision();
			Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
			Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle() + 1);
		} catch (Exception e) {

		}
		KragerTimer.delayMS(2000);
		System.out.println("BANG BANG");
		mDrive.toVision();
		// FeedyWheel.getInstance().setFeedyWheel(1);
		// KragerTimer.delayMS(500);
		// FeedyWheel.getInstance().setFeedyWheel(0);
		// Flywheel.getInstance().setAimVelRPSAuto(0);
		// Hood.getInstance().setGoalAngle(4);

		// VISION ENDED
		//mDrive.stopDrive();
		/*
		 * KragerTimer.delayMS(15000);
		 * 
		 * this.toVision(20); double errorFromStartingVision = mDrive.getAngle()
		 * - angleBeforeVision; System.out.println("HERS SOME STUFF Vision: " +
		 * angleBeforeVision + " dis: " + this.DISTANCE_TO_GOAL +
		 * " angleBeforeVision: " + angleBeforeVision + " error from start " +
		 * errorFromStartingVision); double distanceToGoBack =
		 * (KragerMath.sinDeg(angleBeforeVision) * 110) /
		 * (KragerMath.sinDeg(angleBeforeVision + errorFromStartingVision));
		 * mDrive.stopDrive(); KragerTimer.delayMS(15000); /*
		 * Sensors.resetDrive(); System.out.println("DISTANCE TO GO BACKL " +
		 * distanceToGoBack); DriveEncodersVelocityController goTowardsGoalBack
		 * = new DriveEncodersVelocityController( -Math.abs(distanceToGoBack) -
		 * 5); goTowardsGoalBack.setMAX_ENCODER_VEL(150);
		 * Drive.getInstance().setAutoController(goTowardsGoalBack); try {
		 * this.waitForController(goTowardsGoalBack, 90); } catch (Exception e)
		 * {
		 * 
		 * } mDrive.stopDrive();
		 * 
		 * DriveAngleVelocityController turnToGoalBack = new
		 * DriveAngleVelocityController(startAngle);
		 * Drive.getInstance().setAutoController(turnToGoalBack);
		 * 
		 * try { this.waitForController(turnToGoalBack, 4); } catch (Exception
		 * e) {
		 * 
		 * } mDrive.stopDrive();
		 * 
		 * KragerTimer.delayMS(15000); /*Sensors.resetDrive();
		 * DriveEncodersVelocityController xBack = new
		 * DriveEncodersVelocityController(-230); xBack.setMAX_ENCODER_VEL(120);
		 * LinkedList<VelocityChangePoint> a = new
		 * LinkedList<VelocityChangePoint>(); a.add(new VelocityChangePoint(60,
		 * 90)); xBack.setEncoderChanges(a);
		 * Drive.getInstance().setAutoController(xBack); try {
		 * this.waitForController(xBack, 7); } catch (Exception e) {
		 * 
		 * } mDrive.stopDrive();
		 */
	}

}
