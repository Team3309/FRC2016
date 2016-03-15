package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.KragerSystem;
import org.team3309.lib.controllers.drive.equations.DriveCheezyDriveEquation;
import org.usfirst.frc.team3309.subsystems.climber.Carriage;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;

public class Climber extends KragerSystem {
	private static Climber instance;
	private static Carriage mCarriage = Carriage.getInstance();
	private static Hood mHood = Hood.getInstance();

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Climber getInstance() {
		if (instance == null)
			instance = new Climber("Climber");
		return instance;
	}

	private Climber(String name) {
		super(name);
	}

	@Override
	public void initTeleop() {

	}

	@Override
	public void initAuto() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTeleop() {
		mCarriage.updateTeleop();
		mHood.updateTeleop();
	}

	@Override
	public void updateAuto() {
		mCarriage.updateAuto();
		mHood.updateAuto();
	}

	@Override
	public void sendToSmartDash() {
		mCarriage.sendToSmartDash();
		mHood.sendToSmartDash();
	}

	@Override
	public void manualControl() {
		mCarriage.manualControl();
		mHood.manualControl();
	}
}