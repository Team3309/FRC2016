package org.team3309.lib.actuators;

public abstract class Actuator {
	protected abstract void output();
	public void actuate() {
		output();
	}
}
