package org.usfirst.frc.team3309.vision;

import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;

public class IndicatingLights {

	private enum IndicatorState {
		OFF, RED, GREEN, BLUE
	}

	private static IndicatingLights instance;
	private DigitalOutput bit0 = new DigitalOutput(RobotMap.INDICATOR_LIGHTS_BIT_0);
	private DigitalOutput bit1 = new DigitalOutput(RobotMap.INDICATOR_LIGHTS_BIT_1);
	private double THRESHOLD_FOR_AZIMUTH = 4;

	public static IndicatingLights getInstance() {
		if (instance == null) {
			instance = new IndicatingLights();
		}
		return instance;
	}

	private IndicatingLights() {

	}

	private void setIndicators(IndicatorState state) {
		switch (state) {

		case OFF:
			bit0.set(false);
			bit1.set(false);
			break;

		case RED:
			bit0.set(true);
			bit1.set(false);
			break;

		case GREEN:
			bit0.set(false);
			bit1.set(true);
			break;

		case BLUE:
			bit0.set(true);
			bit1.set(true);
			break;

		default:
			bit0.set(false);
			bit1.set(false);
			break;

		}
	}

	public void update() {
		if (Vision.getInstance().hasShot()) {
			double azimuth = Vision.getInstance().getShotToAimTowards().getAzimuth();
			if (Math.abs(azimuth) < THRESHOLD_FOR_AZIMUTH) {
				this.setIndicators(IndicatorState.BLUE);
			} else {
				this.setIndicators(IndicatorState.GREEN);
			}
		} else {
			this.setIndicators(IndicatorState.RED);
		}
	}
}
