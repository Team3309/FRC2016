package org.usfirst.frc.team3309.auto.operations.defenses;

import java.util.LinkedList;

import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.team3309.lib.controllers.drive.VelocityChangePoint;
import org.team3309.lib.controllers.generic.OnlyPowerController;
import org.usfirst.frc.team3309.auto.operations.intakepivot.MoveIntakePivotToHigh;
import org.usfirst.frc.team3309.auto.operations.shooter.SetRPSAndHoodOperation;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

public class CrossChevelDeFrise extends Operation {

	@Override
	public void perform() {
		mDrive.setHighGear(true);
		this.driveEncoder(30, 100, 3);
		try {
			this.waitForDrive(3);
		} catch (Exception e) {
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e1) {
			}
		}
		OnlyPowerController x = new OnlyPowerController();
		x.setPower(.5);
		IntakePivot.getInstance().setAutoController(x);
		try {
			Thread.sleep(400);
		} catch (Exception e) {
		}
		x.setPower(0);

		DriveEncodersVelocityController goOverCheval = new DriveEncodersVelocityController(200); 
		goOverCheval.setMAX_ENCODER_VEL(50);

		LinkedList<Operation> operations = new LinkedList<Operation>();
		operations.add(new MoveIntakePivotToHigh(50, true));
		goOverCheval.setOperations(operations);
	}

}
