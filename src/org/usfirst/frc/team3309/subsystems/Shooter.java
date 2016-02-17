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
	public void update() {
		mFlywheel.update();
		mHood.update();
		mFeedyWheel.update();
	}

	@Override
	public void sendToSmartDash() {
		mFlywheel.sendToSmartDash();
		mHood.sendToSmartDash();
		mFeedyWheel.update();
	}
}