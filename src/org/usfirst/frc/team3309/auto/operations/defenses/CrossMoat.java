package org.usfirst.frc.team3309.auto.operations.defenses;

import java.util.LinkedList;

import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

public class CrossMoat extends Operation {

	@Override
	public void perform() {
		mDrive.setHighGear(true);
		Sensors.resetDrive();
		DriveEncodersVelocityController xBack = new DriveEncodersVelocityController(230);
		xBack.setMAX_ENCODER_VEL(60);
		LinkedList<VelocityChangePoint> a = new LinkedList<VelocityChangePoint>();
		a.add(new VelocityChangePoint(100, 40));
		a.add(new VelocityChangePoint(110, 50));
		a.add(new VelocityChangePoint(120, 60));
		xBack.setEncoderChanges(a);
		Drive.getInstance().setAutoController(xBack);
		try {
			this.waitForController(xBack, 7);
		} catch (Exception e) {

		}
		mDrive.stopDrive();

		Shot shot = Vision.getInstance().getShot();
		while (shot == null) {
			shot = Vision.getInstance().getShot();
			System.out.println("LOOKING");
		}
		double angleBeforeVision = mDrive.getAngle();

		mDrive.toVision();
		System.out.println("RPS: " + shot.getGoalRPS() + " angke: " + shot.getGoalHoodAngle());

		try {
			shot = Vision.getInstance().getShot();
			Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
			Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle());
			Thread.sleep(500);
			shot = Vision.getInstance().getShot();
			Flywheel.getInstance().setAimVelRPSAuto(shot.getGoalRPS());
			Hood.getInstance().setGoalAngle(shot.getGoalHoodAngle());
		} catch (Exception e) {

		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("BANG BANG");
		FeedyWheel.getInstance().setFeedyWheel(1);
		double errorFromStartingVision = mDrive.getAngle() - angleBeforeVision;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		FeedyWheel.getInstance().setFeedyWheel(0);
		Flywheel.getInstance().setAimVelRPSAuto(0);
		Hood.getInstance().setGoalAngle(4);
	}

}
