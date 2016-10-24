package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;

public class Climber extends ControlledSubsystem {
	private static Climber instance;

	private DoubleSolenoid latches = new DoubleSolenoid(RobotMap.CLIMBER_RIGHT_DOWN, RobotMap.CLIMBER_RIGHT_UP);;
	private DoubleSolenoid leftCarriage = new DoubleSolenoid(RobotMap.CLIMBER_LEFT_UP, RobotMap.CLIMBER_LEFT_DOWN);

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Climber getInstance() {
		if (instance == null)
			instance = new Climber("Climber");
		return instance;
	}

	private Climber(String name) {
		super(name);
		latches.set(Value.kForward);
		leftCarriage.set(Value.kReverse);

	}

	@Override
	public void initTeleop() {
		latches.set(Value.kForward);
		// ONLY IF NOT USING CLIMBER
		leftCarriage.set(Value.kReverse);
	}

	@Override

	public void initAuto() {
		// TODO Auto-generated method stub
		leftCarriage.set(Value.kReverse);
	}

	public void retract() {
		// latches.set(Value.kForward);
		KragerTimer.delayMS(50);
		leftCarriage.set(Value.kReverse);
	}

	boolean hasPressed = false;

	@Override
	public void updateTeleop() {
		// System.out.println(Controls.operatorController.getPOV());
		if (Controls.operatorController.getPOV() == 0) { // Go Up
			latches.set(Value.kReverse);

		} else if (Controls.operatorController.getPOV() == 180 && !hasPressed && latches.get().equals(Value.kReverse)) { // Down
			hasPressed = true;
			leftCarriage.set(Value.kForward);
		} else if (Controls.operatorController.getPOV() == 180 && hasPressed) {
		} else if (Controls.operatorController.getPOV() == 270) {
			//latches.set(Value.kForward);
		} else {
			hasPressed = false;
		}
		if (DriverStation.getInstance().isFMSAttached() && DriverStation.getInstance().getMatchTime() < 1.5) {
			//retract();
		}
		if (Controls.operatorController.getPOV() == 90) {
			leftCarriage.set(Value.kReverse);
		}
	}
	
	@Override
	public void updateAuto() {

	}

	@Override
	public void sendToSmartDash() {

	}

	@Override
	public void manualControl() {

	}

	@Override
	public InputState getInputState() {
		// TODO Auto-generated method stub
		return null;
	}
}