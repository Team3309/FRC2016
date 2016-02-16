package org.team3309.lib;

/**
 * Basic subsystem class
 * 
 * @author TheMkrage
 */
public abstract class KragerSystem {
	/**
	 * Name of Subsystem; "Drive", "Intake", etc.
	 */
	private String name = "Unnamed Subsystem";

	/**
	 * 
	 * @param name
	 *            Given name of subsystem
	 */
	public KragerSystem(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Method ran through each loop to control a certain subsystem
	 */
	public abstract void update();
	
	/**
	 * Send to smart dash to see all of the aspects of this subsystem
	 */
	public abstract void sendToSmartDash();
	
	/** 
	 * Prints logged with what subsystem printed it
	 * @param print string to print
	 */
	public void print(String print) {
		System.out.println(this.getName() + " " + print);
	}
}
