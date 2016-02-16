package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;

public class FeedyWheel extends ControlledSubsystem {

	private static FeedyWheel instance;

	public static FeedyWheel getInstance() {
		if (instance == null) {
			instance = new FeedyWheel("Feedy Wheel");
		}
		return instance;
	}

	private FeedyWheel(String name) {
		super("Feedy Wheel");
		this.mController = new FeedForwardWithPIDController(.001, 0, 0, .001, 0);
	}

	@Override
	public void update() {

	}

	@Override
	public InputState getInputState() {
		return null;
	}

	@Override
	public void sendToSmartDash() {
		// TODO Auto-generated method stub

	}

}
