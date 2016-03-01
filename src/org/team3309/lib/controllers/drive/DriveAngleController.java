package org.team3309.lib.controllers.drive;

import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAngleController extends PIDPositionController {
	double startingAngle = 0;
	double goalAngle = 0;

	public DriveAngleController(double goal) {
		super(.01, 0, 0);
		this.setName("Angle");
		SmartDashboard.putNumber(this.getName() + " goal(set me)", goal);
		this.setTHRESHOLD(.5);
		startingAngle = Sensors.getAngle();
		goalAngle = goal;
	}

	private double lastTime = 0;

	public OutputSignal getOutputSignal(InputState inputState) {
		double error = goalAngle - inputState.getAngularPos();
		if (Math.abs(error) > 180) {
			error = -KragerMath.sign(error) * (360 - Math.abs(error));
			System.out.println("New Error: " + error);
		}
		InputState state = new InputState();
		state.setError(error);
		double left = super.getOutputSignal(state).getMotor();
		OutputSignal signal = new OutputSignal();
		if (Math.abs(left) > .5) {
			if (left > 0) {
				left = .5;
			} else {
				left = -.5;
			}
		}
		signal.setLeftRightMotor(left, -left);
		// System.out.println("Time: " + (System.currentTimeMillis() -
		// lastTime));
		lastTime = System.currentTimeMillis();

		return signal;
	}

	public void sendToSmartDash() {
		super.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " AIM ANGLE", this.goalAngle);
		try {
			if (this.goalAngle != SmartDashboard.getNumber(this.getName() + " goal(set me)")) {
				this.goalAngle = SmartDashboard.getNumber(this.getName() + " goal(set me)");
				this.reset();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setGoalAngle(double angle) {
		this.goalAngle = angle;
	}
}