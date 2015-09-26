package org.usfirst.frc.team3309.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;

public class Sensors {
	public Gyro gyro = new Gyro(0);
	public Encoder leftDrive = new Encoder(0, 1, false);
	public Encoder rightDrive = new Encoder(2, 3, false);
	
	public double getAngularVel() {
		return gyro.getRate();
	}
	
	public double getAngle() {
		return gyro.getAngle();
	}
}