package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.KragerSystem;
import org.usfirst.frc.team3309.subsystems.shooter.FeedyWheel;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;

public class Shooter extends KragerSystem {

	public Flywheel mFlywheel = Flywheel.getInstance();
	public Hood mHood = Hood.getInstance();
	public FeedyWheel mFeedyWheel = FeedyWheel.getInstance();

	public Shooter(String name) {
		super(name);

	}

	@Override
	public void update() {

	}

	@Override
	public void sendToSmartDash() {

	}

}
