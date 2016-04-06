package org.team3309.lib.controllers.drive;

import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.vision.Shot;
import org.usfirst.frc.team3309.vision.Vision;

public class FaceVisionTargetController extends DriveAngleVelocityController {

	private boolean sendToDash = true;
	private Shot aimShot;

	public FaceVisionTargetController() {
		super(Sensors.getAngle());
		if (Vision.getInstance().getShotToAimTowards() != null) {
			aimShot = Vision.getInstance().getShotToAimTowards();
			this.setName("Turn To Vision");
			this.goalAngle = aimShot.getAzimuth() + Sensors.getAngle();
		} else {
			this.sendToDash = false;
			System.out.println("THIS IS VERY BAD");
		}
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		return super.getOutputSignal(inputState);
	}

	@Override
	public void sendToSmartDash() {
		if (this.sendToDash) {
			super.sendToSmartDash();
		}
	}
}