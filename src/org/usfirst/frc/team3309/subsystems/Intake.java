package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.KragerSystem;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

public class Intake extends KragerSystem {

	private static Intake instance;
	private static IntakePivot mIntakePivot = IntakePivot.getInstance();

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Intake getInstance() {
		if (instance == null)
			instance = new Intake("Intake");
		return instance;
	}

	private Intake(String name) {
		super(name);
	}

	@Override
	public void initTeleop() {
		mIntakePivot.initTeleop();
	}

	@Override
	public void initAuto() {
		mIntakePivot.initAuto();
	}

	@Override
	public void updateAuto() {
		mIntakePivot.updateAuto();
	}

	@Override
	public void updateTeleop() {
		mIntakePivot.updateTeleop();
	}

	@Override
	public void sendToSmartDash() {
		mIntakePivot.sendToSmartDash();
	}

	@Override
	public void manualControl() {
		mIntakePivot.manualControl();
	}

}