package org.team3309.lib.actuators;

import edu.wpi.first.wpilibj.TalonSRX;

public class TalonSRXMC extends Actuator {

	private TalonSRX talon;
	private boolean isReversed = false;
	private double desiredOutput = 0.0;

	public TalonSRXMC(int port) {
		talon = new TalonSRX(port);
	}

	@Override
	protected void output() {
		talon.set(desiredOutput * (isReversed ? -1 : 1));
	}

	public boolean isReversed() {
		return isReversed;
	}

	public void setReversed(boolean isReversed) {
		this.isReversed = isReversed;
	}

	public double getDesiredOutput() {
		return desiredOutput;
	}

	public void setDesiredOutput(double desiredOutput) {
		this.desiredOutput = desiredOutput;
	}

	public TalonSRX getTalon() {
		return talon;
	}

}
