package org.team3309.lib.controllers.statesandsignals;

import java.util.HashMap;

public class OutputSignal extends HashMap<String, Double> {

	public OutputSignal() {
		super();
		this.setMotor(0);
	}

	// Default Key Methods
	public void setMotor(double motor) {
		this.put("motor", motor);
	}

	public double getMotor() {
		return this.get("motor");
	}
}
