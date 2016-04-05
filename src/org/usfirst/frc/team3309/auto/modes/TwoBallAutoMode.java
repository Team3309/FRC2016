package org.usfirst.frc.team3309.auto.modes;

import java.util.LinkedList;

import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.auto.operations.shooter.SetRPSAndHoodOperation;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;

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
		operations.add(new SetRPSAndHoodOperation(140, 170, 42));

		this.driveEncoder(282, 150, 5, w, operations, true);

		this.turnToAngle(mDrive.getAngle() + 61, 4);

		double angleBeforeVision = mDrive.getAngle();
		// Shoot First Ball
		try {
			this.toVision(1);
		} catch (Exception e) {
			FeedyWheel.getInstance().setFeedyWheel(1);
			KragerTimer.delayMS(400);
			FeedyWheel.getInstance().setFeedyWheel(0);
		}
		Hood.getInstance().setGoalAngle(4);
		Flywheel.getInstance().setAimVelRPSAuto(0);
		this.turnToAngle(firstAngle, 4);

		LinkedList<VelocityChangePoint> goBackVels = new LinkedList<VelocityChangePoint>();
		goBackVels.add(new VelocityChangePoint(60, 100));
		goBackVels.add(new VelocityChangePoint(190, 230));

		this.driveEncoder(-230, 150, 5, goBackVels);
		mDrive.stopDrive();

		this.turnToAngle(mDrive.getAngle() - 20, 3);
		mDrive.stopDrive();

		this.driveEncoder(-20, 100, 3);
		mDrive.stopDrive();

		// IntakePivot.getInstance().toIntakePosition();
		this.driveEncoder(-20, 100, 3);
		mDrive.stopDrive();

		this.turnToAngle(mDrive.getAngle() + 20, 3);
		mDrive.stopDrive();

		LinkedList<VelocityChangePoint> velToGoBackUnder = new LinkedList<VelocityChangePoint>();
		velToGoBackUnder.add(new VelocityChangePoint(80, 17));
		velToGoBackUnder.add(new VelocityChangePoint(190, 120));
		velToGoBackUnder.add(new VelocityChangePoint(190, 150));

		LinkedList<Operation> operationToGoBackUnder = new LinkedList<Operation>();
		operationToGoBackUnder.add(new SetRPSAndHoodOperation(140, 170, 42));
		this.driveEncoder(230, 150, 5, velToGoBackUnder, operationToGoBackUnder);
	}
}