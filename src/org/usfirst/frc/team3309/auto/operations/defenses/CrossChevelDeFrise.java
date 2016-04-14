package org.usfirst.frc.team3309.auto.operations.defenses;

import java.util.LinkedList;

import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.team3309.lib.controllers.generic.OnlyPowerController;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.operations.intakepivot.MoveIntakePivotToHigh;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

public class CrossChevelDeFrise extends Operation {

	@Override
	public void perform() {
		mDrive.setHighGear(true);
		IntakePivot.getInstance().toUpPosition();
		this.driveEncoder(-70, -70, 3, true);
		KragerTimer.delayMS(500);

		OnlyPowerController x = new OnlyPowerController(Drive.getInstance());
		x.setPower(.5);
		IntakePivot.getInstance().setAutoController(x);
		IntakePivot.getInstance().setGoalAngle(120);
		try {
			KragerTimer.delayMS(1000);
		} catch (Exception e) {
		}
		x.setPower(0);
		IntakePivot.getInstance().setAutoController(x);

		DriveEncodersVelocityController goOverCheval = new DriveEncodersVelocityController(Drive.getInstance(), -200);
		goOverCheval.setMAX_ENCODER_VEL(-50);
		goOverCheval.setRampUp(true);

		LinkedList<Operation> operations = new LinkedList<Operation>();
		operations.add(new MoveIntakePivotToHigh(40, true));
		goOverCheval.setOperations(operations);

		LinkedList<VelocityChangePoint> velocityPoints = new LinkedList<VelocityChangePoint>();
		velocityPoints.add(new VelocityChangePoint(-25, -75));
		velocityPoints.add(new VelocityChangePoint(-35, -170));
		goOverCheval.setEncoderChanges(velocityPoints);

		mDrive.setAutoController(goOverCheval);
		try {
			this.waitForController(goOverCheval, 100);
		} catch (TimedOutException | InterruptedException e) {
			KragerTimer.delayMS(15000);
		}
		KragerTimer.delayMS(500);

		/*
		 * DriveEncodersVelocityController goAllTheWayOver = new
		 * DriveEncodersVelocityController(-150);
		 * goOverCheval.setMAX_ENCODER_VEL(-10);
		 * 
		 * DriveWhileOnADefenseController goAllTheWayOverUntil = new
		 * DriveWhileOnADefenseController(goAllTheWayOver);
		 * mDrive.setAutoController(goAllTheWayOverUntil); try {
		 * this.waitForController(goAllTheWayOverUntil, 100); } catch
		 * (TimedOutException | InterruptedException e) { try {
		 * KragerTimer.delayMS(15000); } catch (InterruptedException e1) { } }
		 */
		KragerTimer.delayMS(1000);
		mDrive.stopDrive();
		System.out.println("\nANGLE NOW: " + mDrive.getAngle() + " AFTER: " + (mDrive.getAngle() + 180.0));
		this.turnToAngle((mDrive.getAngle() + 180.0), 50);
		System.out.println("\nDONE TURNING\n");
	}

}
