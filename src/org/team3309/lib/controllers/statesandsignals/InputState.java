package org.team3309.lib.controllers.statesandsignals;

import java.util.HashMap;

public class InputState extends HashMap<String, Double> {

	public InputState() {
		super();
		this.setX(0.0);
		this.setY(0.0);
		this.setError(0.0);
	}
	
	// Default Key Methods
	public void setError(double error) {
		this.put("error", error);
	}
	
	public double getError() {
		return this.get("error");
	}
	
	public void setX(double x) {
		this.put("x", x);
	}
	
	public double getX() {
		return this.get("x");
	}
	
	public void setY(double y) {
		this.put("y", y);
	}
	
	public double getY() {
		return this.get("y");
	}
	
}
