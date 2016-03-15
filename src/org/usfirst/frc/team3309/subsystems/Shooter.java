package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.KragerSystem;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;

public class Shooter extends KragerSystem {

	public Flywheel mFlywheel = Flywheel.getInstance();
	public Hood mHood = Hood.getInstance();
	public FeedyWheel mFeedyWheel = FeedyWheel.getInstance();

	private static Shooter instance;

	public static Shooter getInstance() {
		if (instance == null) {
			instance = new Shooter("Shooter");
		}
		return instance;
	}

	private Shooter(String name) {
		super(name);
	}

	@Override
	public void initTeleop() {
		mFlywheel.initTeleop();
		mHood.initTeleop();
		mFeedyWheel.initTeleop();
	}

	@Override
	public void initAuto() {
		mFlywheel.initAuto();
		mHood.initAuto();
		mFeedyWheel.initAuto();
	}

	@Override
	public void updateAuto() {
		mFlywheel.updateAuto();
		mHood.updateAuto();
		mFeedyWheel.updateAuto();
	}

	@Override
	public void updateTeleop() {
		mFlywheel.updateTeleop();
		mHood.updateTeleop();
		mFeedyWheel.manualControl();
	}

	@Override
	public void sendToSmartDash() {
		mFlywheel.sendToSmartDash();
		mHood.sendToSmartDash();
		mFeedyWheel.sendToSmartDash();
	}

	@Override
	public void manualControl() {
		mFlywheel.manualControl();
		mHood.manualControl();
		mFeedyWheel.manualControl();
	}
}