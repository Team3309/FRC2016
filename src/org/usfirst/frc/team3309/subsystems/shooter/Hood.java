package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.actuators.SparkMC;
import org.team3309.lib.controllers.generic.PIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.sensors.Sensors;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.SensorDoesNotReturnException;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Hood extends ControlledSubsystem {

	private static Hood mHood = new Hood("Hood");
	private SparkMC hoodSpark = new SparkMC(RobotMap.HOOD_MOTOR);
	private double curAngle = 0;
	private final double HOOD_DOWN_ANGLE = 4;
	private double goalAngle = HOOD_DOWN_ANGLE;

	private double lastVisionAngle = HOOD_DOWN_ANGLE;

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Hood getInstance() {
		if (mHood == null) {
			mHood = new Hood("Hood");
		}
		return mHood;
	}

	private Hood(String name) {
		super(name);
		this.teleopController = new PIDPositionController(0.55, 0.001, .014); // .51
		this.autoController = new PIDPositionController(0.55, 0.001, .014);
		((PIDController) this.teleopController).kILimit = .2;
		this.teleopController.setName("Hood Angle");
		((PIDController) this.teleopController).setTHRESHOLD(.6);
		((PIDController) this.autoController).kILimit = .2;
		this.autoController.setName("Hood Angle");
		((PIDController) this.autoController).setTHRESHOLD(.6);
		SmartDashboard.putNumber("Test Angle", 39.5);
	}

	@Override

	public void initTeleop() {
		goalAngle = HOOD_DOWN_ANGLE;
	}

	@Override
	public void initAuto() {
		goalAngle = HOOD_DOWN_ANGLE;
	}

	private boolean pressBegan = false;
	private double offset = 0;
	private boolean printed = false;

	@Override
	public void updateTeleop() {
		try {
			curAngle = Sensors.getHoodAngle();
		} catch (SensorDoesNotReturnException e) {
			this.manualControl();
			return;
		}
		double output = 0;
		// Find aim angle
		if (Controls.operatorController.getA()) {
			goalAngle = 10.35;
		} else if (Controls.operatorController.getB()) {
			goalAngle = 25.0;
		} else if (Controls.operatorController.getXBut()) {
			goalAngle = 35.5;
		} else if (Controls.driverController.getYBut()) {
			// goalAngle = 28.6;
			goalAngle = SmartDashboard.getNumber("Test Angle");
		} else if (Controls.operatorController.getStart()) {
			if (Vision.getInstance().getShotToAimTowards() != null) {
				goalAngle = Vision.getInstance().getShotToAimTowards().getGoalHoodAngle();
				lastVisionAngle = Vision.getInstance().getShotToAimTowards().getGoalHoodAngle();
				if (!printed && FeedyWheel.getInstance().feedyWheelSpark.getDesiredOutput() < 0) {
					System.out.println("\n SHOOTING --------------- ");
					System.out.println("HOOD: " + goalAngle + " VISION: "
							+ Vision.getInstance().getShotToAimTowards().getYCoordinate());
					System.out.println("--------\n");
					printed = true;
				}
			} else
				goalAngle = 22;
			// System.out.println("Goal Angle: " + goalAngle);
		} else if (Controls.operatorController.getPOV() == 0) {
			goalAngle = lastVisionAngle;
		} else {
			offset = 0;
			printed = false;
			goalAngle = HOOD_DOWN_ANGLE;
		}
		goalAngle += offset;
		if (goalAngle >= 0) {
			output = this.teleopController.getOutputSignal(getInputState()).getMotor();
		}

		if ((curAngle > 50 && output > -1) || (curAngle < 4 && output < 0) || this.isOnTarget()) {
			output = 0;
			// if ((curAngle < 4 && output < 0))
			// ((PIDController) this.teleopController).reset();
		}
		if (Controls.operatorController.getRB() && !pressBegan) {
			pressBegan = true;
			offset += .5;
		} else if (Controls.operatorController.getLB() && !pressBegan) {
			pressBegan = true;
			offset -= .5;
		} else if (!Controls.operatorController.getLB() && !Controls.operatorController.getRB()) {
			pressBegan = false;

		}

		this.setHood(output);
	}

	@Override
	public void updateAuto() {
		double output = 0;
		try {
			curAngle = Sensors.getHoodAngle();
		} catch (SensorDoesNotReturnException e) {
			return;
		}
		if (goalAngle >= 0) {
			output = this.autoController.getOutputSignal(getInputState()).getMotor();
		}
		if ((curAngle > 59 && output > -1) || (curAngle < -20 && output < 0)) {
			output = 0;
		}
		this.setHood(output);
	}

	@Override
	public void sendToSmartDash() {
		if (DriverStation.getInstance().isAutonomous()) {
			autoController.sendToSmartDash();
		} else
			teleopController.sendToSmartDash();
		try {
			SmartDashboard.putNumber(this.getName() + " angle", Sensors.getHoodAngle());
		} catch (SensorDoesNotReturnException e) {
			SmartDashboard.putNumber(this.getName() + " angle", -100000);
		}
		SmartDashboard.putNumber(this.getName() + " goal angle", goalAngle);
		SmartDashboard.putNumber(this.getName() + " power", this.hoodSpark.getDesiredOutput());
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setError(goalAngle - curAngle);
		return input;
	}

	public void setHood(double power) {
		this.hoodSpark.setDesiredOutput(power);
	}

	@Override
	public void manualControl() {
		this.setHood(KragerMath.threshold(Controls.operatorController.getRightY()));
	}

	public double getGoalAngle() {
		return goalAngle;
	}

	public void setGoalAngle(double goalAngle) {
		this.goalAngle = goalAngle;
	}
}