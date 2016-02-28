package org.usfirst.frc.team3309.vision;

public class Shot {
	private double goalRPS = 0;
	private double goalHoodAngle = 0;
	private double azimuth = 0;

	public Shot(double goalRPS, double goalHoodAngle, double azimuth) {
		this.goalRPS = goalRPS;
		this.goalHoodAngle = goalHoodAngle;
		this.setAzimuth(azimuth);
	}

	public double getGoalRPS() {
		return goalRPS;
	}

	public void setGoalRPS(double goalRPS) {
		this.goalRPS = goalRPS;
	}

	public double getGoalHoodAngle() {
		return goalHoodAngle;
	}

	public void setGoalHoodAngle(double goalHoodAngle) {
		this.goalHoodAngle = goalHoodAngle;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}
}
