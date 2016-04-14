package org.team3309.lib.controllers.drive;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAngleController extends PIDPositionController {
	double startingAngle = 0;
	double goalAngle = 0;

	public DriveAngleController(ControlledSubsystem subsystem, double goal) {
		super(subsystem, false, 0.044, 0.00, 0.004);
		this.setName("Angle");
		SmartDashboard.putNumber(this.getName() + " goal(set me)", goal);
		this.setTHRESHOLD(.5);
		this.kILimit = .3;

		startingAngle = Sensors.getAngle();
		goalAngle = goal;
	}

	private double lastTime = 0;
	private final double MIN_POW = .13;

	public OutputSignal getOutputSignal(InputState inputState) {
		double error = goalAngle - inputState.getAngularPos();
		if (Math.abs(error) > 180) {
			error = -KragerMath.sign(error) * (360 - Math.abs(error));
			//System.out.println("New Error: " + error);
		}
		SmartDashboard.putNumber("VISION ErRROR", error);
		InputState state = new InputState();
		state.setError(error);
		
		super.update(state);
		double left = this.lastOutputState.getMotor();
		if (left < 0) {
			left -= MIN_POW;
		} else {
			left += MIN_POW;
		}
		
		if (Math.abs(left) > 1) {
			if (left > 0) {
				left = 1;
			} else {
				left = -1;
			}
		}
		OutputSignal signal = new OutputSignal();
		signal.setLeftRightMotor(left, -left);
		// System.out.println("Time: " + (System.currentTimeMillis() -
		// lastTime));
		lastTime = System.currentTimeMillis();

		return signal;
	}

	public void sendToSmartDash() {
		super.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " AIM ANGLE", this.goalAngle);
		/*
		 * try { if (this.goalAngle != SmartDashboard.getNumber(this.getName() +
		 * " goal(set me)")) { this.goalAngle =
		 * SmartDashboard.getNumber(this.getName() + " goal(set me)");
		 * this.reset(); } } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	public void setGoalAngle(double angle) {
		this.goalAngle = angle;
	}

	public double getGoalAngle() {
		return this.goalAngle;
	}
}