package org.usfirst.frc.team3309.auto.operations.shooter;

import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.subsystems.shooter.Flywheel;
import org.usfirst.frc.team3309.subsystems.shooter.Hood;

public class SetRPSAndHoodOperation extends Operation {

	private double hoodAngle;
	private double RPS;

	public SetRPSAndHoodOperation(double encoder, double RPS, double hoodAngle) {
		super(encoder);
		this.RPS = RPS;
		this.hoodAngle = hoodAngle;

	}

	@Override
	public void perform() {
		Flywheel.getInstance().setAimVelRPSAuto(RPS);
		Hood.getInstance().setGoalAngle(hoodAngle);
	}

}
