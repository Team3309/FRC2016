package org.usfirst.frc.team3309.auto.modes;

import java.util.LinkedList;

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

public class TwoBallAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		double firstAngle = mDrive.getAngle();
		this.mDrive.setHighGear(true);
		LinkedList<VelocityChangePoint> w = new LinkedList<VelocityChangePoint>();
		w.add(new VelocityChangePoint(80, 17));
		w.add(new VelocityChangePoint(190, 120));
		w.add(new VelocityChangePoint(190, 150));

		LinkedList<Operation> operations = new LinkedList<Operation>();
		operations.add(new SetRPSAndHoodOperation(140, 140, 30));

		this.driveEncoder(282, 150, 5, w, operations);

		this.turnToAngle(mDrive.getAngle() + 63, 4);

		double angleBeforeVision = mDrive.getAngle();
		try {
			this.toVision(2);
		} catch (Exception e) {
			FeedyWheel.getInstance().setFeedyWheel(1);
			Thread.sleep(400);
			FeedyWheel.getInstance().setFeedyWheel(0);
		}
		Hood.getInstance().setGoalAngle(4);
		Flywheel.getInstance().setAimVelRPSAuto(0);
		this.turnToAngle(firstAngle, 4);
		
		LinkedList<VelocityChangePoint> goBackVels = new LinkedList<VelocityChangePoint>();
		w.add(new VelocityChangePoint(60, 130));
		w.add(new VelocityChangePoint(190, 230));

		this.driveEncoder(-282, 150, 5, goBackVels);
		

	}

}
