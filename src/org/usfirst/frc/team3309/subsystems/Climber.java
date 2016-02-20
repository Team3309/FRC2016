package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.KragerSystem;
import org.usfirst.frc.team3309.subsystems.climber.Carriage;

public class Climber extends KragerSystem {
	private static Climber instance;
	private static Carriage mCarriage = Carriage.getInstance();

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
	public void update() {
		mCarriage.update();
	}

	@Override
	public void sendToSmartDash() {
		mCarriage.sendToSmartDash();
	}

	@Override
	public void manualControl() {
		mCarriage.manualControl();
	}
}