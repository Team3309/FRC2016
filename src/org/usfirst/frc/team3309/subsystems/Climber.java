package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.KragerSystem;
import org.usfirst.frc.team3309.subsystems.climber.Carriage;

public class Climber extends KragerSystem {
	private static Climber instance;
	private static Carriage mCarriage;

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
		
	}

	@Override
	public void sendToSmartDash() {
		
	}
}