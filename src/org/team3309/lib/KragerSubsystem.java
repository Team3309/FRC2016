package org.team3309.lib;

/**
 * Basic subsystem class
 * 
 * @author TheMkrage
 */
public abstract class KragerSubsystem {
	/**
	 * Name of Subsystem; "Drive", "Intake", etc.
	 */
	private String name = "Unnamed Subsystem";

	/**
	 * 
	 * @param name
	 *            Given name of subsystem
	 */
	public KragerSubsystem(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Method ran through each loop to control a certain subsystem
	 */
	public abstract void update();
}
