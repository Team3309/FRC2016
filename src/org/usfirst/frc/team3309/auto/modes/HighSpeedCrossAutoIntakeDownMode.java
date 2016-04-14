/**
 * 
 */
package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.generic.OnlyPowerController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

/**
 * @author User
 *
 */
public class HighSpeedCrossAutoIntakeDownMode extends AutoRoutine {
	@Override
	public void routine() throws TimedOutException, InterruptedException {
		IntakePivot.getInstance().toIntakePosition();
		OnlyPowerController x = new OnlyPowerController();
		x.setLeftPower(.4);
		x.setRightPower(.4);
		Drive.getInstance().setTeleopController(x);
		KragerTimer.delayMS(6000);
		x.setLeftPower(0);
		x.setRightPower(0);
		mDrive.setTeleopController(x);
		mDrive.stopDrive();
		System.out.println("Auto is done");
	}
}
