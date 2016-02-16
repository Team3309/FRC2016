package org.team3309.lib.controllers.drive;

import java.util.List;

import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.vision.Goal;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FaceVisionTargetController extends DriveAngleController {

	private boolean isFirstTime = true;
	private double lastDirection = 1;
	private double blankCounts = 0;
	private double azimuth = 0;
	private boolean isAimDecided = false;

	public FaceVisionTargetController(double kP, double kI, double kD) {
		super(Sensors.getAngle());
		this.setTHRESHOLD(.02);
		this.setName("Turn To Vision");
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		//OutputSignal signal = new OutputSignal();
		List<Goal> currentGoals = Vision.getInstance().getGoals();
		if (!currentGoals.isEmpty() && !isAimDecided) {
			azimuth = currentGoals.get(0).azimuth;
			this.goalAngle = Sensors.getAngle() + azimuth;
			isAimDecided = true;
		}
		/*if (!currentGoals.isEmpty()) {
			blankCounts = 0;
			this.completable = true;
			Goal curGoal = currentGoals.get(0);
			System.out.println("\nX: " + curGoal.x);
			System.out.println("current goals: " + currentGoals);
			inputState.setError(0 - curGoal.x);
			double output = super.getOutputSignal(inputState).getMotor();
			System.out.println("RUN AT THIS POWER: " + output + " (RIGHT)");
			if (isFirstTime) {
				lastDirection = KragerMath.sign(output);
			}
			signal.setLeftRightMotor(-output, output);
		} else {
			blankCounts++;

			if (blankCounts > 5) {
				this.reset();
				System.out.println("");
				// TIME TO FIND THE TARGET
				this.completable = false;
				signal.setLeftRightMotor(lastDirection * -.33, .33 * lastDirection);
			}
		}*/
		return super.getOutputSignal(inputState);
	}
	
	@Override 
	public void sendToSmartDash() {
		super.sendToSmartDash();
		SmartDashboard.putNumber("AIM delta ANGLE", azimuth);
	}
}