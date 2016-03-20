package org.usfirst.frc.team3309.vision;

import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class IndicatingLights {

	private enum IndicatorState {
		OFF, RED, GREEN, BLUE
	}

	private static IndicatingLights instance;
	private double THRESHOLD_FOR_AZIMUTH = 4;

	public static IndicatingLights getInstance() {
		if (instance == null) {
			instance = new IndicatingLights();
		}
		return instance;
	}

	private IndicatingLights() {

	}

	private NetworkTable lightsTable = NetworkTable.getTable("Lights");

	private void setIndicators(IndicatorState state) {
		switch (state) {

		case OFF:
			lightsTable.putString("Status", "Off");
			break;

		case RED:
			lightsTable.putString("Status", "Red");
			break;

		case GREEN:
			lightsTable.putString("Status", "Green");
			break;

		case BLUE:
			lightsTable.putString("Status", "Blue");
			break;

		default:
			lightsTable.putString("Status", "Off");
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
